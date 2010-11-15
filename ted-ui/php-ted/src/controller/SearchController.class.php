<?php
	require_once 'WatchingController.class.php';

	class SearchController extends baseController {

		public function index() {

			$searchText = "";
			if (isset($_POST['searchText'])) {
				$searchText = $_POST['searchText'];
			}

			$this->search($searchText);
			$this->registry->template->view_title = "Search Results";
			$this->registry->template->show('search_results');
		}

		public function startWatching($id) {
			$watchingController = new WatchingController($this->registry);
			$watchingController->startWatching($id);
		}

		private function search($text) {
			$overviews = array();
			$results = $this->registry->client->search($text);
			foreach ($results as $serie) {
				$searchUID = $serie->searchUID;
				$overview = $this->registry->client->getOverview($searchUID);
				$overviews[$searchUID] = $overview;
			}

			$this->registry->template->results = $results;
			$this->registry->template->overviews = $overviews;
		}

	}
?>