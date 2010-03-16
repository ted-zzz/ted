package nu.ted.guide.tvdb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.xml.bind.DatatypeConverter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import nu.ted.guide.tvdb.EpisodeList.Episode;

public class EpisodeListTest
{
    private static class TestEpisodeListXML
    {
        private StringBuffer xml;

        public TestEpisodeListXML()
        {
            xml = new StringBuffer();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<Data>\n");

            // Add a serie, as this appears in the results (but we don't need it)
            xml.append("  <Serie>\n");
            xml.append("    <id>1000</id>\n");
            xml.append("    <Actors>people</Actors>\n");
            xml.append("  </Serie>\n");
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

    private TestEpisodeListXML xml;

    @Before
    public void setUp()
    {
        this.xml = new TestEpisodeListXML();
    }

    private void assertEpisode(Episode episode, int season, int epnum, String aired, String name) throws ParseException
    {
        Assert.assertEquals(season, episode.getSeason());
        Assert.assertEquals(epnum, episode.getEpisode());
        Assert.assertEquals(DatatypeConverter.parseDate(aired), episode.getFirstAired());
        Assert.assertEquals(name, episode.getTitle());
    }

    @Test
    public void oneResult() throws UnsupportedEncodingException, ParseException
    {
        this.xml.addEpisode(1, 3, "2007-01-02", "Name");

        EpisodeList list = EpisodeList.create(xml.toStream());

        Assert.assertEquals(1, list.getEpisodeList().size());
        assertEpisode(list.getEpisodeList().get(0), 1, 3, "2007-01-02", "Name");
    }

    @Test
    public void multipleResult() throws UnsupportedEncodingException, ParseException
    {
        this.xml.addEpisode(1, 3, "2007-01-02", "Name");
        this.xml.addEpisode(1, 4, "2007-01-09", "Name2");

        EpisodeList list = EpisodeList.create(xml.toStream());

        Assert.assertEquals(2, list.getEpisodeList().size());

        assertEpisode(list.getEpisodeList().get(0), 1, 3, "2007-01-02", "Name");
        assertEpisode(list.getEpisodeList().get(1), 1, 4, "2007-01-09", "Name2");

    }
}
