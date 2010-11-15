
<?php

	class Registry {

		private $vars = array();

		public function __set($index, $value) {
			$this->vars[$index] = $value;
		}

		public function __get($index)
		{
			return $this->vars[$index];
		}

	}

?>