
<script language='javascript'>

	function processForm() {
		$.post("<?php echo '/' . __SITE_ROOT . '/sources/validate'; ?>",
				{
					type: document.source_form.type.value,
					name: document.source_form.name.value,
					location: document.source_form.location.value
				},
				function(data) {
					validateResponse(data);
				},
				"json");
	}

	function validateResponse(data) {
		if (data.is_valid) {
			document.source_form.submit();
		}
		else {

			for (var i = 0; i < data.fields.length; i++) {
				var field = data.fields[i];

				var fieldElement = $('#' + field.name);
				var fieldErrorIcon = $('#' + field.name + '-error-icon');
				var fieldErrorMessage = $('#' + field.name + '-error-message');
				if (field.is_valid) {
					fieldElement.removeClass('ted-form-input-error');
					fieldErrorIcon.css('display', 'none');
					fieldErrorMessage.css('display', 'none');
				}
				else {
					fieldElement.addClass('ted-form-input-error');
					fieldErrorIcon.css('display', 'block');
					fieldErrorMessage.css('display', 'block');
					fieldErrorMessage.html(field.message);
				}

			}
		}
	}

</script>

<form class="ted-form" name="source_form" method="post" action="<?php echo '/' . __SITE_ROOT . '/sources/' . $action; ?>">
	<table>
		<tr>
			<td class="ted-form-label">
				<label for="type">Type:</label>
			</td>
			<td>
				<input id="type" class="ted-form-text-input" type="text" name="type" value="<?php echo $source->type; ?>"/>
			</td>
			<td class="ted-form-error-icon">
				<img id="type-error-icon" src="<?php echo '/' . __SITE_ROOT . '/img/famfam/exclamation.png' ?>" style="display: none;"/>
			</td>
			<td class="ted-form-error-message">
				<div id="type-error-message" style="display: none;">This is the message.</div>
			</td>
		</tr>
		<tr>
			<td class="ted-form-label">
				<label for="name">Name:</label>
			</td>
			<td>
				<input id="name" class="ted-form-text-input" type="text" name="name" value="<?php echo $source->name; ?>"/>
			</td>
			<td class="ted-form-error-icon">
				<img id="name-error-icon" src="<?php echo '/' . __SITE_ROOT . '/img/famfam/exclamation.png' ?>" style="display: none;" />
			</td>
			<td class="ted-form-error-message">
				<div id="name-error-message" style="display: none;">Error message goes here.</div>
			</td>
		</tr>
		<tr>
			<td class="ted-form-label">
				<label for="location">Location:</label>
			</td>
			<td>
				<input id="location" class="ted-form-text-input" type="text" name="location" value="<?php echo $source->location; ?>"/>
			</td>
			<td class="ted-form-error-icon">
				<img id="location-error-icon" src="<?php echo '/' . __SITE_ROOT . '/img/famfam/exclamation.png' ?>" style="display: none;" />
			</td>
			<td class="ted-form-error-message">
				<div id="location-error-message"  style="display: none;">Error message goes here.</div>
			</td>
		</tr>
		<tr>
			<td class="ted-form-button-bar" colspan="4">
				<a class="button" onclick="processForm();">
					<img src="<?php echo '/' . __SITE_ROOT . '/img/famfam/' . $button_icon;?>" alt="" />
					<?php echo $button_name; ?>
				</a>
			</td>
		</tr>
	</table>
</form>
