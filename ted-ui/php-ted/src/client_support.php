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

	class ClientSupport {
		private static $instance;
		private $transport;
		private $client;

		private function __construct() {
			try {
				$socket = new TSocket('localhost', '9030');
				$socket->setRecvTimeout(50000);
				$this->transport = new TBufferedTransport($socket);
				$protocol = new TBinaryProtocol($this->transport);
				$this->client = new TedServiceClient($protocol);
				$this->transport->open();
			} catch (TException $tx) {
				// a general thrift exception, like no such server
				echo "ThriftException: ".$tx->getMessage()."\r\n";
			}
		}

		public static function client() {
			if (!self::$instance) {
				self::$instance = new ClientSupport();
			}
			return self::$instance->client;
		}

	}
?>