package nu.ted.domain;

import java.util.Calendar;

import nu.ted.generated.Series;
import nu.ted.generated.Ted;

public class TedBackendWrapper {

	private Ted ted;

	public TedBackendWrapper(Ted ted) {
		this.ted = ted;
	}
	
	public void updateSeries(Calendar calendar) {
		// TODO: locking
		
		for (Series s : ted.getSeries()) {
			new SeriesBackendWrapper(s).update(calendar);
		}
	}
}
