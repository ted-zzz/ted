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
	}
?>