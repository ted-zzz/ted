package nu.ted.guide.tvdb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TestMirrorXml {
	private StringBuffer xml;
	private int i = 1;

	public TestMirrorXml()
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

	public InputStream toStream()
	{
		try {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Invalid Encoding - testing error", e);
		}
	}
}
