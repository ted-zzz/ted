package nu.ted.guide.tvdb;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import nu.ted.DataRetrievalException;
import nu.ted.generated.Episode;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.TDate;
import nu.ted.guide.tvdb.datasource.DataSource;


public class TVDBTest {

	private static class TestRecord extends FullSeriesRecord {

		@Override
		public Episode getNextEpisode(TDate date) {
			return new Episode((short) 1, (short) 1, new TDate(1000));
		}

		@Override
		public Episode getNextEpisode(Episode last) {
			Episode next = new Episode(last);
			next.setAired(new TDate(last.getAired().getValue() + 1000));
			return next;
		}


	}
	private static class TestDataSource implements DataSource {

		@Override
		public FullSeriesRecord getFullSeriesRecord(String id)
				throws DataRetrievalException {

			return new TestRecord();
		}

		@Override
		public ImageFile getImage(String guideId, ImageType type)
				throws DataRetrievalException {
			throw new UnsupportedOperationException("Not Yet Implemented");
		}

		@Override
		public List<SeriesSearchResult> search(String name)
				throws DataRetrievalException {
			throw new UnsupportedOperationException("Not Yet Implemented");
		}

	}

	@Test
	public void ensureFindsMultipleEpisodes() throws DataRetrievalException {
		TVDB tvdb = new TVDB(new TestDataSource());

		// Search between 900-3100, should find 1000, 2000, 3000.
		List<Episode> eps = tvdb.getAiredEpisodesBetween("Doesn't matter", new TDate(900),
				new TDate(3100));

		Assert.assertEquals("Should have found 3 episodes", eps.size(), 3);
		Assert.assertEquals("Should be episode at 1000", eps.get(0).getAired().getValue(), 1000);
		Assert.assertEquals("Should be episode at 2000", eps.get(1).getAired().getValue(), 2000);
		Assert.assertEquals("Should be episode at 3000", eps.get(2).getAired().getValue(), 3000);
	}


}
