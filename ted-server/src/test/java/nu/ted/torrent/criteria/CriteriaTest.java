package nu.ted.torrent.criteria;

import static org.junit.Assert.*;

import nu.ted.torrent.TorrentRef;

import org.junit.Test;

public class CriteriaTest {

	public static class FailingCriteria implements Criteria {

		@Override
		public boolean isAcceptable(TorrentRef torrent) {
			return false;
		}

	}

	@Test
	public void ensureSimpleFailingCriteriaFails() {
		TorrentRef torrent = new TorrentRef("Show", "http://location.invalid", 100);

		Criteria criteria = new FailingCriteria();

		assertFalse("Failing Criteria should always fail", criteria.isAcceptable(torrent));

	}

	@Test
	public void ensureMinSizeWorks() {
		Criteria criteria;
		TorrentRef torrent = new TorrentRef("Show", "http://location.invalid", 100);

		criteria = new MinSizeCriteria(99);
		assertTrue("Show should be >= 99", criteria.isAcceptable(torrent));

		criteria = new MinSizeCriteria(100);
		assertTrue("Show should be >= 100", criteria.isAcceptable(torrent));

		criteria = new MinSizeCriteria(101);
		assertFalse("Show should not be >= 101", criteria.isAcceptable(torrent));
	}

	@Test
	public void ensureMaxSizeWorks() {
		Criteria criteria;
		TorrentRef torrent = new TorrentRef("Show", "http://location.invalid", 100);

		criteria = new MaxSizeCriteria(101);
		assertTrue("Show should be <= 101", criteria.isAcceptable(torrent));

		criteria = new MaxSizeCriteria(100);
		assertTrue("Show should be <= 100", criteria.isAcceptable(torrent));

		criteria = new MaxSizeCriteria(99);
		assertFalse("Show should not be <= 99", criteria.isAcceptable(torrent));
	}

}
