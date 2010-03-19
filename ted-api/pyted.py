#!/usr/bin/env python

# Quick test of the ted-server
#   python doesn't like '-' in imports
#   so first 'mv gen-py py'

from thrift.protocol import TBinaryProtocol
from thrift.transport import TTransport
from py.ted.ttypes import *
from thrift.transport.TTransport import TBufferedTransport
from thrift.transport.TSocket import TSocket
import py.ted.TedService

transport = TBufferedTransport(TSocket('localhost', 9030))
transport.open()
protocol = TBinaryProtocol.TBinaryProtocol(transport)
client = py.ted.TedService.Client(protocol)

results = client.search("chuck")

for series in results:
    print series.name

print "First Series: " + results[0].name

id = results[0].searchUID

print "First Search ID: " + id

banner = client.getBanner(id)

print "Banner length: " + str(len(banner))


