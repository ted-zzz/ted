<?php
	class Template {
		private $registry;
		private $vars = array();

		function __construct($registry) {
			$this->registry = $registry;
		}

		public function __set($index, $value)
		{
			$this->vars[$index] = $value;
		}

		public function show($name) {
			$template_file = __SITE_PATH . '/views/template.php';
			$content = __SITE_PATH . '/views' . '/' . $name . '.php';

			if (!file_exists($content))
			{
					throw new Exception('Template not found in '. $content);
					return false;
			}

			// Load variables
			foreach ($this->vars as $key => $value)
			{
				$$key = $value;
			}

			// Load the site template. The content will be loaded
			// via the template file.
			include ($template_file);
		}
	}
?>