
<div id="nav_bar">
	<div class="nav_bar_item">
			<?php
				echo "<a href='/" . __SITE_ROOT . "'>Home</a>";
				echo "<a href='/" . __SITE_ROOT . "/watching'>Watching</a>";
				echo "<a href='/" . __SITE_ROOT . "/sources'>Sources</a>";
			?>
		<form method='post' action='search'>
			<input class="search-textbox" name="searchText" type="text" />
			<input type="image" src="/<?php echo __SITE_ROOT; ?>/img/zoom.png" alt="Search"/>
		</form>
	</div>
</div>
