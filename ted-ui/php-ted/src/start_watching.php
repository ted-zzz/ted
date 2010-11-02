<?php
		// Setup the path to the thrift library folder
	$GLOBALS['THRIFT_ROOT'] = 'thrift';

	// Load up all the thrift stuff
	require_once $GLOBALS['THRIFT_ROOT'].'/Thrift.php';
	require_once $GLOBALS['THRIFT_ROOT'].'/protocol/TBinaryProtocol.php';
	require_once $GLOBALS['THRIFT_ROOT'].'/transport/TSocket.php';
	require_once $GLOBALS['THRIFT_ROOT'].'/transport/TBufferedTransport.php';

	// Load the generated files.
	require_once $GLOBALS['THRIFT_ROOT'].'/packages/ted/ted_constants.php';
	require_once $GLOBALS['THRIFT_ROOT'].'/packages/ted/TedService.php';
	require_once $GLOBALS['THRIFT_ROOT'].'/packages/ted/ted_types.php';

	include("search.php");

	try {
		$socket = new TSocket('localhost', '9030');
		$socket->setRecvTimeout(1000);
		$transport = new TBufferedTransport($socket);
		$protocol = new TBinaryProtocol($transport);
		$client = new TedServiceClient($protocol);

		$transport->open();

		$client->startWatching($_GET['searchUID']);
		$transport->close();

		header('Location: page.php?id=watching');
	} catch (TException $tx) {
		// a general thrift exception, like no such server
		echo "ThriftException: ".$tx->getMessage()."\r\n";
	}
?>