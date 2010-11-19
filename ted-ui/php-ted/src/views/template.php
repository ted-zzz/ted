<html>
	<head>
		<title>TED - <?php echo $view_title; ?> </title>
		<link href="/<?php echo __SITE_ROOT; ?>/views/ted.css" rel="stylesheet" type="text/css" />
		<script src="lib/jquery.js" language="javascript" type="text/javascript"></script>
	</head>
	<body>
		<div id="container">
			<?php include 'site_header.php'; ?>
			<div id="page_content">
				<div id="page_title"><?php echo $view_title; ?></div>
				<?php include $content; ?>
			</div>
		</div>
	</body>
</html>
