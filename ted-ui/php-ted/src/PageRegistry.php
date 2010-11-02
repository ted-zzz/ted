<?php

	class Registry {
		private static $instance;

		private $pages;

		private function __construct() {
			$this->registerPage("home", new Home());
			$this->registerPage("search", new SearchResults());
			$this->registerPage("watching", new Watching());
//			$this->registerPage("seriesdetails", new SeriesDetails());
		}

		private function registerPage($pageId, $page_instance) {
			$this->pages[$pageId] = $page_instance;
		}

		public function instance() {
			if (!self::$instance) {
				self::$instance = new Registry();
			}
			return self::$instance;
		}

		public function getPage($key) {
			// TODO: Throw exception.
			return $this->pages[$key];
		}

		public function getNavKeys() {
			return array_keys($this->pages);
		}

		public static function load($key) {
			return self::instance()->getPage($key);
		}

		public static function nav_keys() {
			return self::instance()->getNavKeys();
		}
	}

	class RegisteredPage {
		private $headerTitle;
		private $pageTitle;
		private $contentFile;
		private $includeInNavBar;

		public function __construct($headerTitle, $pageTitle, $contentFile, $includeInNavBar = true) {
			$this->headerTitle = $headerTitle;
			$this->pageTitle = $pageTitle;
			$this->contentFile = $contentFile;
			$this->includeInNavBar = $includeInNavBar;
		}

		public function getHeaderTitle() {
			return $this->headerTitle;
		}

		public function getPageTitle() {
			return $this->pageTitle;
		}

		public function getContentFile() {
			return $this->contentFile;
		}

		public function includeInNavBar() {
			return $this->includeInNavBar;
		}
	}

	class Home extends RegisteredPage {
		public function __construct() {
			parent::__construct("Home", "Home", "home_content.php");
		}
	}

	class SearchResults extends RegisteredPage {
		public function __construct() {
			parent::__construct("Search Results", "Search Results", "search_results.php", false);
		}
	}

	class Watching extends RegisteredPage {
		public function __construct() {
			parent::__construct("Watching", "Watching", "watching.php");
		}
	}

	class SeriesDetails extends RegisteredPage {
		public function __construct() {
			parent::__construct("Series Details", "Series Details", "series_details.php");
		}
	}

?>