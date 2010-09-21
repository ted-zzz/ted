#!/usr/bin/env python

import logging
import code
import readline
import shlex

try:
    from thrift.protocol import TBinaryProtocol
    from thrift.transport import TTransport
    from thrift.transport.TTransport import TBufferedTransport, TTransportException
    from thrift.transport.TSocket import TSocket
except ImportError:
    print "Thrift not in PYTHONPATH, perhaps you want: export PYTHONPATH=$PYTHONPATH:<location to thrift"
    exit(-1)

try:
    from ted.ttypes import *
    import ted.TedService
    from ted.TedService import Iface
except ImportError:
    print "Ted not in PYTHONPATH, perhaps you want: export PYTHONPATH=$PYTHONPATH:<location>/ted/ted-api/target/generated-sources/gen-py/"
    exit(-2)

class TedUI():

    def __init__(self):
        self.connected = False
        readline.parse_and_bind('tab: complete')
        readline.set_completer(self.tab_complete)
        readline.parse_and_bind('set bell-style none')
        readline.set_completer_delims(' ')

        # Get all non-private functions
        self.funs = [x for x in Iface.__dict__.keys() if not x[0] == '_']
        self.funs.sort()

        self.builtins = {}
        self.builtins['connect'] = self.connect
        self.builtins['disconnect'] = self.disconnect
        self.builtins['help'] = self.help
        self.builtins['?'] = self.help

    def tab_complete(self, text, index):
        matches = [word for word in self.funs if word.startswith(text)]
        try:
            return matches[index]
        except IndexError:
            return None

    def _show_commands(self):
        print "Commands"
        print "--------"
        for key in self.builtins:
            print key
        print "--------"
        for fun in self.funs:
            print fun

    def connect(self):
        self.transport = TBufferedTransport(TSocket('localhost', 9030))
        self.transport.open()
        protocol = TBinaryProtocol.TBinaryProtocol(self.transport)

        self.client = ted.TedService.Client(protocol)
        self.connected = True

    def disconnect(self):
        self.client.logout()
        self.transport.close()
        self.client = None
        self.connected = False

    def help(self):
        self._show_commands()

    def main(self):

        logger = logging.getLogger('ted')
        logger.addHandler(logging.StreamHandler())

        print "Welcome to ted. Type 'help' or '?' for a list of commands"

        while 1:
            try:
                line = raw_input("> ")
            except KeyboardInterrupt:
                break
            except EOFError:
                break

            if len(line) == 0:
                continue

            # Split lines using shlex, attempt to convert ints
            # int's can be escaped using quotes
            parser = shlex.shlex(line)
            line_split = list(parser)
            formatted_split = []
            for item in line_split:
                try:
                    formatted_split.append(int(item))
                except ValueError:
                    if item[0] in parser.quotes:
                        #Ignore Quotes
                        formatted_split.append(item[1:-1])
                    else:
                        formatted_split.append(item)

            command = formatted_split[0]
            args = formatted_split[1:]
            
            if command in self.builtins:
                self.builtins[command]()
            elif command in self.funs:
                if self.connected:
                    c = getattr(self.client, command)
                    print c(*args)
                else:
                    print "Unable to run while not connected"
            else:
                print "Invalid Command: " + command

        print
        if self.connected:
            self.disconnect()

if __name__ == "__main__":
	TedUI().main()
