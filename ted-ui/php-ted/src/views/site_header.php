
<div id="header">
	<div id="header-logo">
		<a href="/<?php echo __SITE_ROOT; ?>"></a>
	</div>
	<div id="search">
		<div id="search-button" onclick="document.searchForm.submit();"></div>
		<form id="search-form" name="searchForm" method="post" action="/<?php echo __SITE_ROOT; ?>/search">
			<input id="search-box" type="text" name="searchText"/>
		</form>
	</div>

</div>
