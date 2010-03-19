package nu.ted.guide.tvdb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Mirrors")
public class Mirrors {
	private static class Mirror {
		@XmlElement
		private String mirrorpath;
		@XmlElement
		private int typemask;

		public String getMirrorpath() {
			return mirrorpath;
		}

		public boolean isValidForMask(Mask mask) {
			return ((typemask & mask.value) != 0) ? true : false;
		}
	}

	public enum Mask {
		xml(1), banner(2), zip(4);

		public final int value;

		Mask(int value) {
			this.value = value;
		}
	}

	public static class NoMirrorException extends Exception {
		private static final long serialVersionUID = 1L;

		public NoMirrorException() {
			super();
		}

		public NoMirrorException(String message, Throwable cause) {
			super(message, cause);
		}

		public NoMirrorException(String message) {
			super(message);
		}

		public NoMirrorException(Throwable cause) {
			super(cause);
		}
	}

	@XmlElement(name = "Mirror")
	private List<Mirror> mirrorList = new ArrayList<Mirror>();

	private Mirror xmlmirror = null;
	private Mirror zipmirror = null;
	private Mirror bannermirror = null;

	public static Mirrors createMirrors(InputStream is)
			throws NoMirrorException {
		try {
			JAXBContext context = JAXBContext.newInstance(Mirrors.class);
			Unmarshaller um = context.createUnmarshaller();
			return (Mirrors) um.unmarshal(is);
		} catch (JAXBException e) {
			throw new NoMirrorException(e);
		}
	}

	public String getXMLMirror() throws NoMirrorException {
		if (xmlmirror == null) {
			xmlmirror = getMirror(Mask.xml);
		}
		return xmlmirror.getMirrorpath();
	}

	public String getZipMirror() throws NoMirrorException {
		if (zipmirror == null) {
			zipmirror = getMirror(Mask.zip);
		}
		return zipmirror.getMirrorpath();
	}

	public String getBannerMirror() throws NoMirrorException {
		if (bannermirror == null) {
			bannermirror = getMirror(Mask.banner);
		}
		return bannermirror.getMirrorpath();
	}

	private Mirror getMirror(Mask mask) throws NoMirrorException {
		// If we're already using one mirror, use it again if possible.
		if (xmlmirror != null && xmlmirror.isValidForMask(mask)) {
			return xmlmirror;
		} else if (zipmirror != null && zipmirror.isValidForMask(mask)) {
			return zipmirror;
		} else if (bannermirror != null && bannermirror.isValidForMask(mask)) {
			return bannermirror;
		}

		List<Mirror> validMirrors = new LinkedList<Mirror>();
		for (Mirror mirror : mirrorList) {
			if (mirror.isValidForMask(mask)) {
				validMirrors.add(mirror);
			}
		}

		if (validMirrors.size() == 0) {
			throw new NoMirrorException();
		} else {
			Random r = new Random();
			return validMirrors.get(r.nextInt(validMirrors.size()));
		}
	}
}
