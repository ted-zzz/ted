<?php

	error_reporting(E_ALL);
	$site_path = realpath(dirname(__FILE__));
	define ('__SITE_PATH', $site_path);

	$path_parts = explode('/', $site_path);
	define ('__SITE_ROOT', $path_parts[sizeof($path_parts) - 1]);

	/*** include the init.php file ***/
	include 'includes/init.php';

	$registry->router = new router($registry);
	$registry->router->setPath(__SITE_PATH . "/controller");
	$registry->template = new Template($registry);
	$registry->router->loader();

?>