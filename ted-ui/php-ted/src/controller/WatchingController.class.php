<?php
	class WatchingController extends baseController {
		public function index() {
			$this->registry->template->view_title = "Watching";
			$this->registry->template->watch_list = $this->registry->client->getWatching();
			$this->registry->template->show('watching');
		}

		public function startWatching($id) {
			$this->registry->client->startWatching($id);
			header('Location: /' . __SITE_ROOT . '/watching');
			exit;
		}

		public function stopWatching($id) {
			$this->registry->client->stopWatching($id);
			header('Location: /' . __SITE_ROOT . '/watching');
			exit;
		}

		public function poll($lastPollTime) {
			$latestEventDate = (int)$lastPollTime;

			$pollDate = new TDate(array('value'=>$latestEventDate));
			$events = $this->registry->client->getEvents($pollDate);
			$eventsJson = array();
			foreach ($events as $event) {
				if ($event->type == EventType::WATCHED_SERIES_ADDED || $event->type == EventType::EPISODE_ADDED) {
					ob_start();
					$series = $event->series;
					include 'views/watching_content.php';
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

			echo json_encode(array('lastPolled'=>$latestEventDate, 'events'=>$eventsJson));
		}
	}
?>