<?php
	require_once 'PageRegistry.php';

	$page = Registry::load($_REQUEST['id']);
?>

<html>
	<head>
		<title>TED - <?php echo $page->getHeaderTitle();	?> </title>
		<link href="ted.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div id="container">
			<?php include 'site_header.php'; ?>
			<div id="page_content">
				<div id="page_title"><?php echo $page->getPageTitle(); ?></div>
				<?php include("pages/" . $page->getContentFile()); ?>
			</div>
		</div>
	</body>
</html>