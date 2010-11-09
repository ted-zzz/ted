<?php
	// Fetches all events from ted for the watching.php page. Events are transformed into JSON
	// that the UI page can translate and update UI components.
	require_once 'TedSession.php';

	sleep(1);
	$lastPolled = $_POST['lastPolledOn'];
	try {
		$events = $client->getEvents(new TDate((int)$lastPolled));
	} catch (TException $tx) {
		echo json_encode($tx->getMessage());
		return;
	}

	$json = array();
	$eventsJson = array();

	$latestEventDate = (int)$lastPolled;
	foreach ($events as $event) {
		if ($event->type == EventType::WATCHED_SERIES_ADDED || $event->type == EventType::EPISODE_ADDED) {
			ob_start();
			$series = $event->series;
			include 'pages/watching_content.php';
			$seriesHtml = ob_get_clean();
			array_push($eventsJson, array('type'=>$event->type,
							'uid'=>$event->series->uid,
							'seriesHtml'=>$seriesHtml));
		}
		else if ($event->type == EventType::WATCHED_SERIES_REMOVED) {
			array_push($eventsJson, array('type'=>$event->type, 'uid'=>$event->series->uid));
		}

		$eventMillis = $event->registeredOn->value;
		if ($latestEventDate < $eventMillis) {
			$latestEventDate = $eventMillis;
		}
	}

	array_push($json, array('lastPolled'=>$latestEventDate, 'events'=>$eventsJson));
	echo json_encode($json);
?>
