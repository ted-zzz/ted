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
			if (sizeof($sources) == 0) {
				echo "<tr>";
					echo "<td colspan='4'>";
						echo "No Sources Exist.";
					echo "</td>";
			}
			else {

				foreach ($sources as $source) {
					echo "<tr>";
						echo "<td class='type'>" . $source->type . "</td>";
						echo "<td class='name'>" . $source->name . "</td>";
						echo "<td class='location'>" . $source->location . "</td>";
						echo "<td class='actions'>";
						echo "<a class='edit' href='/" . __SITE_ROOT . "/sources/edit/" . $source->uid .
								"' title='Edit this source.'></a>";
						echo "<a class='remove' href='/" . __SITE_ROOT . "/sources/remove/" . $source->uid .
								"' title='Remove this source.'></a>";
						echo "</td>";
					echo "</tr>";
				}
			}
		?>
		<tr>
			<td class="sources-new-button" colspan="4">
			<?php
					echo "<a href='/" . __SITE_ROOT . "/sources/create' class='button'>\n";
						echo "<img src='" . "/" . __SITE_ROOT . "/img/famfam/add.png' alt='' />\n";
						echo "New";
					echo "</a>";
			?>
			</td>
		</tr>
	</tbody>
</table>
