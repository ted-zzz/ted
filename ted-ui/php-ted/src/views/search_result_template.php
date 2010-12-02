<hr />
<div class='search_result'>
	<div class='search_result_img'>
		<img src='display_img.php?guideId=<?php echo $serie->searchUID; ?>' width='300px' height='55px'/>
	</div>
	<div class='search_result_details'>
		<h1><?php echo $serie->name; ?></h1>
		<p><?php echo $overviews[$serie->searchUID]; ?></p>

		<a href="watching/startWatching/<?php echo $serie->searchUID; ?>" class="button">
			<img src="<?php echo '/' . __SITE_ROOT . '/img/famfam/television_add.png'?>" alt="" />
			Start Watching
		</a>
	</div>
</div>