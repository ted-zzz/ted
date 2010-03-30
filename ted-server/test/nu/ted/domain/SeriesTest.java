package nu.ted.domain;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import nu.ted.guide.TestGuide;

public class SeriesTest {

//	@Test
//	public void currentEpisodeShouldBeSetOnCreation() {
//		Series s = new Series(Calendar.getInstance(), (short) 1, new TestGuide(), "E");
//		assertNotNull("Unable to create Series", s);
//
//		WatchedSeries ws = s.getWatchedSeries();
//		assertNotNull("Unable to get WatchedSeries from Series", ws);
//
//		CurrentEpisode episode = ws.getCurrentEpisode();
//		assertNotNull("Unable to get current episode", episode);
//		assertEquals(TestGuide.LAST_EPISODE_SEASON, episode.getSeason());
//		assertEquals(TestGuide.LAST_EPISODE_NUMBER, episode.getNumber());
//	}
//
//	@Test
//	public void airedEpisodesShouldBeEmptyOnCreation() {
//		Series s = new Series(Calendar.getInstance(), (short) 1, new TestGuide(), "E");
//		WatchedSeries ws = s.getWatchedSeries();
//
//		assertEquals("Should have no aired episodes to start",
//				0, ws.getAiredEpisodesSize());
//	}
//	
//	@Test
//	public void updateAfterNewEpisodeShouldHaveOneSearching() {
//		Calendar calendar = Calendar.getInstance();
//		Series s = new Series(calendar, (short) 1, new TestGuide(), "E");
//		s.update(calendar);
//		WatchedSeries ws = s.getWatchedSeries();
//		
//		assertEquals("Should have the one episodes that aired yesterday",
//				1, ws.getAiredEpisodesSize());
//		
//	}

	// shouldGetGuideFromFactorIfNULL

}
