package nu.ted.domain;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.Series;
import nu.ted.generated.Ted;

public class TedBackendWrapper {

	private Ted ted;

	public TedBackendWrapper(Ted ted) {
		this.ted = ted;
	}

	public boolean hasMissingEpisodes() {
		for (Series s : ted.getSeries()) {
			if (new SeriesBackendWrapper(s).hasMissingEpisodes() == true)
				return true;
		}
		return false;
	}

	public List<Series> getSeriesWithMissingEpisodes() {
		List<Series> series = new LinkedList<Series>();
		for (Series s : ted.getSeries()) {
			if (new SeriesBackendWrapper(s).hasMissingEpisodes()) {
				series.add(s);
			}
		}
		return series;
	}

	public boolean updateSeries(Calendar calendar) {
		// TODO: locking
		boolean foundNew = false;

		for (Series s : ted.getSeries()) {
			if (new SeriesBackendWrapper(s).update(calendar) == true) {
				foundNew = true;
			}
		}
		return foundNew;
	}
}
