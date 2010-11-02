<?php
	require_once 'client_support.php';

	if (!isset($_GET['searchText'])) {
		echo "No results found.";
		return;
	}

	try {
		$result = ClientSupport::client()->search($_GET['searchText']);
		foreach ($result as $serie) {
			echo "<div class='search_result'>";
				echo "<div class='search_result_img'>";
					print "<img src='display_img.php?guideId=$serie->searchUID' width='300px' height='55px'/>";
				echo "</div>";
				echo "<div class='search_result_details'>";
					echo "<h1>", "$serie->name<h1/>";
					echo "<h2>", $serie->searchUID, "</h2>";
					$overview = ClientSupport::client()->getOverview($serie->searchUID);
					print "<p>$overview</p>";
					print "<a href='start_watching.php?searchUID=$serie->searchUID'>Start Watching</a>";
				echo "</div>";
			echo "</div>";
		}
	} catch (TException $tx) {
		// a general thrift exception, like no such server
		echo "ThriftException: ".$tx->getMessage()."\r\n";
	}
?>