<?php

	abstract class baseController {
		protected $registry;

		function __construct($registry) {
			$this->registry = $registry;
		}

		abstract function index();
	}

?>