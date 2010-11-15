<?php

	class IndexController extends baseController {
		public function index() {
			$this->registry->template->welcome = 'Welcome to ted.';
			$this->registry->template->view_title = "Home";
			$this->registry->template->show('index');
		}
	}

?>
