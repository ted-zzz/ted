package nu.ted.domain;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import nu.ted.generated.CurrentEpisode;
import nu.ted.generated.WatchedSeries;
import nu.ted.guide.TestGuide;

public class SeriesTest {

	@Test
	public void currentEpisodeShouldBeSetOnCreation() {
		Series s = new Series((short) 1, new TestGuide(), "E");
		assertNotNull("Unable to create Series", s);

		WatchedSeries ws = s.getWatchedSeries();
		assertNotNull("Unable to get WatchedSeries from Series", ws);

		CurrentEpisode episode = ws.getCurrentEpisode();
		assertNotNull("Unable to get current episode", episode);
		assertEquals(TestGuide.LAST_EPISODE_SEASON, episode.getSeason());
		assertEquals(TestGuide.LAST_EPISODE_NUMBER, episode.getNumber());
	}

	@Test
	public void airedEpisodesShouldBeEmptyOnCreation() {
		Series s = new Series((short) 1, new TestGuide(), "E");
		WatchedSeries ws = s.getWatchedSeries();

		assertEquals("Should have no aired episodes to start",
				0, ws.getAiredEpisodesSize());
	}

	// shouldGetGuideFromFactorIfNULL

}
