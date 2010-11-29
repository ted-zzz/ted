<?php

	abstract class baseController {
		protected $registry;

		function __construct($registry) {
			$this->registry = $registry;
		}

		protected function makeSafe($post_get_array) {
			$safe = array();
			foreach ($post_get_array as $key=>$value) {
				$safe_string = strip_tags($value);
				$safe_string = stripcslashes($safe_string);
				$safe[$key] = $safe_string;
			}
			return $safe;
		}

		abstract function index();
	}

?>