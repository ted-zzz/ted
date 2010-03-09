package nu.ted.guide.tvdb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import nu.ted.guide.tvdb.Mirrors;
import nu.ted.guide.tvdb.Mirrors.Mask;
import nu.ted.guide.tvdb.Mirrors.NoMirrorException;

import org.junit.Before;
import org.junit.Test;


public class MirrorsTest
{
	private static class TestMirrorXML
	{
		private StringBuffer xml;
		private int i = 1;

		public TestMirrorXML()
		{
			xml = new StringBuffer();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			xml.append("<Mirrors>\n");
		}

		public void addMirror(String path, int mask)
		{
			xml.append("  <Mirror>\n");

			xml.append("    <id>" + i + "</id>\n");
			xml.append("    <mirrorpath>" + path + "</mirrorpath>\n");
			xml.append("    <typemask>" + mask + "</typemask>\n");

			xml.append("  </Mirror>\n");

			i++;
		}

		public String toString()
		{
			return xml.toString() + "</Mirrors>";
		}

		public InputStream toStream() throws UnsupportedEncodingException
		{
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}

	}

	private TestMirrorXML xml;

	@Before
	public void setUp() {
		this.xml = new TestMirrorXML();
	}

	@Test(expected=NoMirrorException.class)
	public void zeroMirror() throws UnsupportedEncodingException, NoMirrorException
	{
		Mirrors mirrors = Mirrors.createMirrors(xml.toStream());

		mirrors.getXMLMirror();
	}

	@Test
	public void oneMirror() throws UnsupportedEncodingException, NoMirrorException
	{
		xml.addMirror("http://site-a.invalid", 7);

		Mirrors mirrors = Mirrors.createMirrors(xml.toStream());

		Assert.assertEquals("http://site-a.invalid", mirrors.getXMLMirror());
		Assert.assertEquals("http://site-a.invalid", mirrors.getZipMirror());
	}

	@Test
	public void twoMirrorOneForEachType() throws UnsupportedEncodingException, NoMirrorException
	{
		xml.addMirror("http://site-xml.invalid", Mask.xml.value);
		xml.addMirror("http://site-zip.invalid", Mask.zip.value);

		Mirrors mirrors = Mirrors.createMirrors(xml.toStream());

		Assert.assertEquals("http://site-xml.invalid", mirrors.getXMLMirror());
		Assert.assertEquals("http://site-zip.invalid", mirrors.getZipMirror());
	}

	@Test(expected=NoMirrorException.class)
	public void noValidMirrors() throws UnsupportedEncodingException, NoMirrorException
	{
		xml.addMirror("http://site-banner1.invalid", Mask.banner.value);
		xml.addMirror("http://site-banner2.invalid", Mask.banner.value);

		Mirrors mirrors = Mirrors.createMirrors(xml.toStream());

		mirrors.getZipMirror();
	}
}
