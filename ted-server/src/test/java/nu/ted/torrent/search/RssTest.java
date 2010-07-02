package nu.ted.torrent.search;

import static junit.framework.Assert.*;

import java.util.LinkedList;
import java.util.List;

import nu.ted.torrent.Torrent;
import nu.ted.torrent.search.Rss.RssSource;

import org.junit.Test;

public class RssTest {
	
	private static class TestRssSource implements RssSource {

		@Override
		public List<Torrent> getTorrents() {
			
			List<Torrent> torrents = new LinkedList<Torrent>();
			torrents.add(new Torrent("A-name1", "A-link1"));
			torrents.add(new Torrent("A-name2", "A-link2"));
			torrents.add(new Torrent("B-name", "B-link"));
			torrents.add(new Torrent("C-name", "C-link"));
			
			return torrents;
		}
		
	}

	@Test
	public void ensureSimpleSearchReturnsResult() {
		Rss rss = new Rss(new TestRssSource());
		List<Torrent> torrents = rss.search("A-name");
		assertNotNull(torrents);
		assertEquals(2, torrents.size());
		
		assertEquals("A-name1" , torrents.get(0).getTitle());
		assertEquals("A-name2" , torrents.get(1).getTitle());
	}

}
