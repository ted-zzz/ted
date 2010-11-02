<?php
	require_once 'client_support.php';

	try {
		ClientSupport::client()->stopWatching($_REQUEST['uid']);
		header("Location: page.php?id=watching");
	} catch (TException $tx) {
		// a general thrift exception, like no such server
		echo "ThriftException: ".$tx->getMessage()."\r\n";
	}
?>