package nu.ted.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TestSeriesXml {
	public static final String EXPECTED_OVERVIEW = "This is the overview.";

	private StringBuffer xml;

	public TestSeriesXml()
	{
		this("graphical/73244-g9.jpg");
	}

	public TestSeriesXml(String banner) {
		xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xml.append("<Data>\n");
		xml.append("  <Series>\n");
		xml.append("    <id>1000</id>\n");
		xml.append("    <Actors>people</Actors>\n");
		xml.append("    <Overview>" + EXPECTED_OVERVIEW + "</Overview>\n");
		xml.append("    <banner>" + banner + "</banner>\n");
		xml.append("  </Series>\n");
	}

	public void addEpisode(int season, int episode, String aired, String name)
	{
		xml.append("  <Episode>\n");
		xml.append("    <SeasonNumber>" + season + "</SeasonNumber>\n");
		xml.append("    <EpisodeNumber>" + episode + "</EpisodeNumber>\n");
		xml.append("    <FirstAired>" + aired + "</FirstAired>\n");
		xml.append("    <EpisodeName>" + name + "</EpisodeName>\n");
		xml.append("  </Episode>\n");
	}

	public String toString()
	{
		return xml.toString() + "</Data>";
	}

	public InputStream toStream() throws UnsupportedEncodingException
	{
		return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
	}

}
