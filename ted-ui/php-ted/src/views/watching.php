<?php
	function createNotWatchingDiv() {
		return "<div id='not_watching'>You are not watching any shows.</div>";
	}

	// Create necessary JS functions.
	$script = "
		function showLocalDate(timestamp)
		{
			var dt = new Date(timestamp);
			document.write(dt.toLocaleString());
		}

		function process(data) {
			if (data.length <= 0) {
				poll(new Date().getTime());
				return;
			}

			var lastPollTime = data.lastPolled;

			var i;
			var events = data.events;
			for (i in events) {
				// Watch added.
				if (1 == events[i].type) {
					if ($('#not_watching').length) {
						$('#not_watching').remove();
					}
					$('#watch_list').append(events[i].seriesHtml);
				}
				// Remove series.
				else if (2 == events[i].type) {
					$('#' + events[i].uid ).remove();
					if( $('#watch_list').children().length == 0) {
						$('#watch_list').append(\"" . createNotWatchingDiv() . "\");
					}

				}
				// episode added.
				else if (3 == events[i].type) {
					if ($('#' + events[i].uid ).length) {
						$('#' + events[i].uid ).replaceWith(events[i].seriesHtml);
					}
					else {
						$('#watch_list').append(events[i].seriesHtml);
					}
				}

			}

			// Wait here. Since we share a connection to the ted
			// server, waiting on the server ties up the connection,
			// as the call is not asynchronous.
			setTimeout('poll(' + lastPollTime + ')', " . __POLL_INTERVAL .");
		}

		function poll(lastPolled) {
			$.post('watching/poll/' + lastPolled, {}, process, 'json');
		}
	";
	include 'javascript.php';

	echo "<div id='watch_list'>";
	if (sizeof($watch_list) == 0) {
		echo createNotWatchingDiv();
	}
	else {
		foreach ($watch_list as $series) {
			include 'watching_content.php';
		}
	}
	echo "</div>";
?>

<?php
	$script = "window.onload = poll(new Date().getTime());";
	include 'javascript.php';
?>