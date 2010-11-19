<p>
	<form method="post" action="<?php echo '/' . __SITE_ROOT . '/sources/' . $action; ?>">
		Type:&nbsp;<input type="text" name="type" value="<?php echo $source->type; ?>"/><br />
		Name:&nbsp;<input type="text" name="name" value="<?php echo $source->name; ?>"/><br />
		Location:&nbsp;<input type="text" name="location" value="<?php echo $source->location; ?>"/><br />
		<input type="submit" value="submit" /><br />
	</form>

</p>