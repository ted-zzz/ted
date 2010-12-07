
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
						echo "<LI class='watching-episode'>Season " . $episode->season . " - Episode ". $episode->number . " [";
						$date_string = $episode->aired->value;

						// TODO This needs to be part of a date lib.
						date_default_timezone_set('America/Halifax');
						$date = new DateTime();
						$date->setTimestamp($episode->aired->value / 1000);
						echo $date->format('Y-m-d');
						echo "]";

						if (EpisodeStatus::UNKNOWN == $episode->status) {
							$image_dir = "";
							$image = "question.gif";
							$desc = "Unknown";
						}
						else if (EpisodeStatus::SEARCHING == $episode->status) {
							$image_dir = "famfam/";
							$image = "find.png";
							$desc = "Searching";
						}
						else if (EpisodeStatus::FOUND == $episode->status) {
							$image_dir = "famfam/";
							$image = "accept.png";
							$desc = "Found";
						}
						else if (EpisodeStatus::OLD == $episode->status) {
							$image_dir = "famfam/";
							$image = "accept_gray.png";
							$desc = "Old Episode";
						}
						echo "<img class='watching-status-img' src='/" . __SITE_ROOT . "/img/" . $image_dir . $image . "' title='". $desc . "' />";
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

