#!/usr/bin/env python

import logging
import code

from thrift.protocol import TBinaryProtocol
from thrift.transport import TTransport
from ted.ttypes import *
from thrift.transport.TTransport import TBufferedTransport, TTransportException
from thrift.transport.TSocket import TSocket
import ted.TedService
from ted.TedService import Iface

def start():

    logger = logging.getLogger('ted')
    logger.addHandler(logging.StreamHandler())

    transport = TBufferedTransport(TSocket('localhost', 9030))
    transport.open()

    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    global client
    client = ted.TedService.Client(protocol)

    shell = code.InteractiveConsole(globals())
    shell.interact("Run client.<command> where command is a Ted command (eg: getWatching())\nSee dir(Iface) for commands")

if __name__ == "__main__":
    start()
