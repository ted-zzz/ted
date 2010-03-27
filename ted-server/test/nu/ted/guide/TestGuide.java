package nu.ted.guide;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.domain.Episode;
import nu.ted.generated.ImageFile;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.GuideDB;

public class TestGuide implements GuideDB
{
	public static final short LAST_EPISODE_SEASON = 4;
	public static final short LAST_EPISODE_NUMBER = 2;
	
	@Override
	public List<SeriesSearchResult> search(String name) {
		List<SeriesSearchResult> results = new LinkedList<SeriesSearchResult>();
		if (name.equalsIgnoreCase("Exactly")) {
			results.add(new SeriesSearchResult("E", "Exactly"));
		} else if (name.equalsIgnoreCase("General")) {
			results.add(new SeriesSearchResult("1", "General1"));
			results.add(new SeriesSearchResult("2", "General2"));
			results.add(new SeriesSearchResult("3", "General3"));
		}
		return results;
	}

	@Override
	public ImageFile getBanner(String id) {
		if (id.equals("E")) {
			return new ImageFile("image/cool", "ABCD".getBytes());
		}
		return new ImageFile();
	}

	@Override
	public String getName() {
		return "TEST";
	}

	@Override
	public String getName(String guideId) {
		if (guideId.equals("E")) {
			return "Exactly";
		}
		return null;
	}

	@Override
	public Episode getLastEpisode(String guideId, Calendar date) {
		Calendar oneDayAgo = (Calendar) date.clone();
		oneDayAgo.add(Calendar.DAY_OF_MONTH, -1);
		if (guideId.equals("E")) {
			return new Episode(LAST_EPISODE_SEASON, LAST_EPISODE_NUMBER, oneDayAgo);
		}
		return null;
	}

	@Override
	public String getOverview(String guideId) {
		return "An Overview";
	}
}