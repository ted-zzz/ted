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
			$values = $this->makeSafe($_POST);
			$type = $values['type'];
			$name = $values['name'];
			$location = $values['location'];

			$newSource = $this->createSource($type, $name, $location);
			$this->registry->client->addTorrentSource($newSource);
			header('Location: /' . __SITE_ROOT . '/sources');
		}

		public function remove($id) {
			$this->registry->client->removeTorrentSource($id);
			header('Location: /' . __SITE_ROOT . '/sources');
		}

		public function update($id) {
			$values = $this->makeSafe($_POST);
			$type = $values['type'];
			$name = $values['name'];
			$location = $values['location'];

			$source = $this->createSource($type, $name, $location);
			$source->uid = $id;

			$this->registry->client->updateTorrentSource($source);
			header('Location: /' . __SITE_ROOT . '/sources');
		}

		public function validate() {
			$fields = array();
			if (!isset($_POST['type']) || $_POST['type'] == "") {
				array_push($fields, array('is_valid'=>false, 'name'=>'type',
					'message'=>'Required value'));
			}
			// TODO Verify type with ted once multiple exist (there will be multiple).
			else if ($_POST['type'] != 'RSS') {
				array_push($fields, array('is_valid'=>false, 'name'=>'type',
					'message'=>'Invalid type. Supported types: RSS'));
			}
			else {
				array_push($fields, array('is_valid'=>true, 'name'=>'type'));
			}

			if (!isset($_POST['name']) || $_POST['name'] == "") {
				array_push($fields, array('is_valid'=>false, 'name'=>'name',
					'message'=>'Required value'));
			}
			else if ($this->registry->client->torrentSourceExists($_POST['name'])) {
				array_push($fields, array('is_valid'=>false, 'name'=>'name',
					'message'=>'A source with this name already exists.'));
			}
			else {
				array_push($fields, array('is_valid'=>true, 'name'=>'name'));
			}

			if (!isset($_POST['location']) || $_POST['location'] == "") {
				array_push($fields, array('is_valid'=>false, 'name'=>'location',
					'message'=>'Required value'));
			}
			else if (!$this->validateUrl($_POST['location'])) {
				array_push($fields, array('is_valid'=>false, 'name'=>'location',
					'message'=>'Invalid location. Must begin with http(s)://'));
			}
			else {
				array_push($fields, array('is_valid'=>true, 'name'=>'location'));
			}


			$is_valid = !$this->fieldDataHasErrors($fields);

			echo json_encode(array('is_valid'=>$is_valid, 'fields'=> $fields));
		}

		public function sayhi() {
			json_encode($this->validate());
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

		private function fieldDataHasErrors($data) {
			foreach ($data as $fieldDataArray) {
				if (!$fieldDataArray['is_valid']) {
					return true;
				}
			}
			return false;
		}

		private function validateUrl($url) {
			return preg_match('|^http(s)?://[a-z0-9-]+(.[a-z0-9-]+)*(:[0-9]+)?(/.*)?$|i', $url);
		}
	}
?>
