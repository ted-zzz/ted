<?php
	if (sizeof($results) == 0) {
		echo "No results found.";
		return;
	}

	foreach ($results as $serie) {
		include 'search_result_template.php';
	}
?>