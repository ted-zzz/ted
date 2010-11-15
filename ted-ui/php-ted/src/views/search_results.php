<?php
	if (sizeof($results) == 0) {
		echo "No results found.";
		return;
	}

	foreach ($results as $serie) {
		echo "<div class='search_result'>";
			echo "<div class='search_result_img'>";
				print "<img src='display_img.php?guideId=$serie->searchUID' width='300px' height='55px'/>";
			echo "</div>";
			echo "<div class='search_result_details'>";
				echo "<h1>", "$serie->name<h1/>";
				echo "<h2>", $serie->searchUID, "</h2>";
				$overview = $overviews[$serie->searchUID];
				print "<p>$overview</p>";
				print "<a href='search/startWatching/$serie->searchUID'>Start Watching</a>";
			echo "</div>";
		echo "</div>";
	}
?>