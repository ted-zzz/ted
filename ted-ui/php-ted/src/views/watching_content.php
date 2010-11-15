<?php
	// TODO [MS] This has reuse potential. Maby add to a Util library.
	echo "<script language='javascript'>";
	echo "
			function showLocalDate(timestamp)
			{
				var dt = new Date(timestamp);
				document.write(dt.toLocaleString());
			}
		";
	echo "</script>";

?>

<h3><?php echo $series->name ?></h3>
<ul>
	<?php
		if (sizeof($series->episodes) == 0) {
			echo "No episodes found.";
		}
		else {
			foreach ($series->episodes as $episode) {
				echo "<LI>Season " . $episode->season . " - Episode ". $episode->number . " [";
				$date_string = $episode->aired->value;
				$script = "showLocalDate($date_string);";
				include 'javascript.php'; // Uses $script
				echo "]";
			}

		}
	?>
</ul>
<a href="watching/stopWatching/<?php echo $series->uid; ?>">Stop Watching</a>