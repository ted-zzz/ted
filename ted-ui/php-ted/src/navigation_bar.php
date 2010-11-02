<?php
	require_once 'PageRegistry.php';
	$nav_keys = Registry::nav_keys();
?>
<div id="nav_bar">
	<div class="nav_bar_item">
		<form method='get' action='/ted/page.php'>
			<?php
				foreach ($nav_keys as $key) {
					$nextPage = Registry::load($key);
					if ($nextPage->includeInNavBar()) {
						echo "<a href='page.php?id=" . $key . "'>". $nextPage->getPageTitle() . "</a>";
					}
				}
			?>
			<input type="hidden" name="id" value="search" />
			<input class="search-textbox" name="searchText" type="text" />
			<input type="image" src="img/zoom.png" alt="Search"/>
		</form>
	</div>
</div>
