<?php
	require_once 'client_support.php';

	$watch_list = ClientSupport::client()->getWatching();
	if (sizeof($watch_list) == 0) {
		echo "You are currently not watching any shows.";
	}

	foreach ($watch_list as $series) {
		include 'watching_content.php';
	}
?>

