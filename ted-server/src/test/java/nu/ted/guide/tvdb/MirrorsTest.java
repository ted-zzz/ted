package nu.ted.guide.tvdb;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import nu.ted.guide.tvdb.datasource.Mirrors;
import nu.ted.guide.tvdb.datasource.Mirrors.Mask;
import nu.ted.guide.tvdb.datasource.Mirrors.NoMirrorException;

import org.junit.Before;
import org.junit.Test;


public class MirrorsTest
{

	private TestMirrorXml xml;

	@Before
	public void setUp() {
		this.xml = new TestMirrorXml();
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
