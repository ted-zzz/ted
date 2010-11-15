<?php

	class router {
		private $registry;
		private $path;
		private $args = array();

		public $file;
		public $controller;
		public $action;
		public $action_parameter;

		function __construct($registry) {
			$this->registry = $registry;
		}

		function setPath($path) {
			if (! is_dir($path)) {
				throw new Exception("Invalid controller path: " . $path);
			}
			$this->path = $path;
		}

		public function loader() {
			$this->getController();

			if (!is_readable($this->file)) {
				echo $this->file;
				die("404 Not Found.");
			}

			include $this->file;

			$class = $this->convertClassName($this->controller) . "Controller";
			$controller = new $class($this->registry);

			if (!is_callable(array($controller, $this->action))) {
				$action = "index";
			}
			else {
				$action = $this->action;
			}

			if (empty($this->action_parameter)) {
				$controller->$action();
			}
			else {
				$controller->$action($this->action_parameter);
			}
		}

		private function convertClassName($paramClass) {
			return  ucfirst(strtolower($paramClass));
		}

		private function getController() {
			$route = ( empty($_GET['rt']) ) ? '' : $_GET['rt'];

			if (empty($route)) {
				$route = 'index';
			}
			else {
				$parts = explode('/', $route);
				$this->controller = $parts[0];

				if (isset($parts[1])) {
					$this->action = $parts[1];
				}

				if (isset($parts[2])) {
					$this->action_parameter = $parts[2];
				}
			}

			if ( empty($this->controller) ) {
				$this->controller = 'index';
			}

			if ( empty($this->action) ) {
				$this->action = 'index';
			}

			$this->file = $this->path . '/' . $this->convertClassName($this->controller) . 'Controller.class.php';
		}
	}
?>
