<?php
	class SourcesController extends baseController {
		public function index() {
			$this->registry->template->view_title = "Sources";
			$this->registry->template->sources =
				$this->registry->client->getTorrentSources();
			$this->registry->template->show("sources");
		}

		public function create() {
			$source = new TorrentSource();
			$source->type = "RSS";
			$this->setupFormParameters("Create New Source", "add", $source);
			$this->registry->template->show("edit_source");
		}

		public function edit($id) {
			$source = $this->registry->client->getTorrentSource($id);
			$this->setupFormParameters("Edit Source", "update/" . $id, $source);
			$this->registry->template->show("edit_source");
		}

		public function add() {
			$type = $_POST['type'];
			$name = $_POST['name'];
			$location = $_POST['location'];

			$newSource = $this->createSource($type, $name, $location);
			$this->registry->client->addTorrentSource($newSource);
			header('Location: /' . __SITE_ROOT . '/sources');
		}

		public function remove($id) {
			$this->registry->client->removeTorrentSource($id);
			header('Location: /' . __SITE_ROOT . '/sources');
		}

		public function update($id) {
			$type = $_POST['type'];
			$name = $_POST['name'];
			$location = $_POST['location'];

			$source = $this->createSource($type, $name, $location);
			$source->uid = $id;

			$this->registry->client->updateTorrentSource($source);
			header('Location: /' . __SITE_ROOT . '/sources');
		}

		private function createSource($type, $name, $location) {
			$source = new TorrentSource();
			$source->type = $type;
			$source->name = $name;
			$source->location = $location;
			return $source;
		}

		private function setupFormParameters($title, $action, $source) {
			$this->registry->template->view_title = $title;
			$this->registry->template->action = $action;
			$this->registry->template->source = $source;
		}

	}
?>