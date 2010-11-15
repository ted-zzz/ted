<?php
	if (sizeof($watch_list) == 0) {
		echo "You are not watching any shows.";
	}

	foreach ($watch_list as $series) {
		include 'watching_content.php';
	}
?>