<?php
	require_once 'client_support.php';
	try {
		$image_src = ClientSupport::client()->getImageByGuideId(
			$_REQUEST['guideId'], ImageType::BANNER_THUMBNAIL);

		$im = imagecreatefromstring($image_src->data);
		header("Content-type: image/jpeg");
		imagejpeg($im);
		imagedestroy($im);
	} catch (TException $tx) {
		// a general thrift exception, like no such server
		echo "ThriftException: ".$tx->getMessage()."\r\n";
	}

?>