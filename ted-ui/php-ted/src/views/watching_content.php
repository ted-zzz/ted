
<div id='<?php echo $series->uid; ?>'>
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

					// TODO This needs to be part of a date lib.
					date_default_timezone_set('America/Halifax');
					$date = new DateTime();
					$date->setTimestamp($episode->aired->value / 1000);
					echo $date->format('Y-m-d H:i:s');
					echo "]";
				}

			}
		?>
	</ul>
	<a href="watching/stopWatching/<?php echo $series->uid; ?>">Stop Watching</a>
</div>