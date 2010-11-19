<?php

	function createNewLink() {
		return "<a href='/" . __SITE_ROOT . "/sources/create'>New</a>";
	}

	if (sizeof($sources) == 0) {
		echo "No Sources Exist.";
		echo "<p>" . createNewLink() . "</p>";
		return;
	}
?>

<table id='sources_table'>
	<thead>
		<tr>
			<th class='type'>Type</th>
			<th class='name'>Name</th>
			<th class='location'>Location</th>
			<th class='actions'></th>
		</tr>
	</thead>
	<tbody>
		<?php
			foreach ($sources as $source) {
				echo "<tr>";
					echo "<td class='type'>" . $source->type . "</td>";
					echo "<td class='name'>" . $source->name . "</td>";
					echo "<td class='location'>" . $source->location . "</td>";
					echo "<td class='actions'>";
					echo "<a href='/" . __SITE_ROOT . "/sources/edit/" . $source->uid . "'>E</a>";
					echo "&nbsp;";
					echo "<a href='/" . __SITE_ROOT . "/sources/remove/" . $source->uid . "'>X</a>";
					echo "</td>";
				echo "</tr>";
			}
		?>
	</tbody>
</table>

<?php echo "<p>" . createNewLink() . "</p>"; ?>
