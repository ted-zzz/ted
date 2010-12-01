
<table id='<?php echo $series->uid; ?>' class='watching-table'>
	<tr>
		<td class="watched-series-header"><h3><?php echo $series->name ?></h3></td>
	</tr>
	<tr>
		<td class="watched-series-details">
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
		</td>
	<tr>
	<tr>
		<td>
			<a href="watching/stopWatching/<?php echo $series->uid; ?>" class="button">
				<img src="<?php echo '/' . __SITE_ROOT . '/img/famfam/television_delete.png'?>" alt="" />
				Stop Watching
			</a>
		</td>
	</tr>
</table>

