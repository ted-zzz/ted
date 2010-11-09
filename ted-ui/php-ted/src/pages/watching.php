<?php

	function createNotWatchingDiv() {
		return "<div id='not_watching'>You are currently not watching any shows.</div>";
	}

	echo "<div id='watch_list'>";
		$watch_list = $page->getWatching();
		if (sizeof($watch_list) == 0) {
			echo createNotWatchingDiv();
		}
		else {
			foreach ($watch_list as $series) {
				include 'watching_content.php';
			}
		}

	echo "</div>";

	$script = "
		function showLocalDate(timestamp)
		{
			var dt = new Date(timestamp);
			document.write(dt.toLocaleString());
		}

		function process(data) {
			if (data.length < 0) {
				poll(new Date().getTime());
				return
			}
			var result = data[0];
			var lastPollTime = result.lastPolled;

			var i;
			var events = result.events;
			for (i in events) {
				// Watch added.
				if (1 == events[i].type) {
					var id = '#' + events[i].uid;
					// $('#not_watching').remove();

					//alert('Will replace with: ' + events[i].seriesHtml);
					//if ($(id) != null) {
						alert('Replacing...');
					//	$(id).replaceWith(events[i].seriesHtml);
					//}
					//else {

						$('#watch_list').append(events[i].seriesHtml);
					//}
				}
				// Remove series.
				else if (2 == events[i].type) {
					if ($('#' + events[i].uid) != null) {
						if ($('#watch_list > div').size() == 1) {
							$('#' + events[i].uid).replaceWith(\"" . createNotWatchingDiv() . "\");
						}
						else {
							$('#' + events[i].uid).remove();
						}
					}
				}
				// episode added.
				else if (3 == events[i].type) {
					$('#' + events[i].uid).replaceWith(events[i].seriesHtml);
				}

			}
			poll(lastPollTime);
		}

		function poll(lastPolled) {
			$.post('watching_event_poller.php', {lastPolledOn: lastPolled}, process, 'json');
		}

		window.onload = poll(new Date().getTime());
	";

	include 'javascript.php';
?>

