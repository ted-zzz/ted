package nu.ted.guide;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
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
	public ImageFile getImage(String id, ImageType type) {
		if (id.equals("E")) {
			return getImageFile(type);
		}
		return new ImageFile();
	}

	public ImageFile getImage(short uID, ImageType type) {
		if (uID == 2) {
			return getImageFile(type);
		}
		return new ImageFile();
	}

	@Override
	public String getName() {
		return "TEST";
	}

	@Override
	public String getName(String guideId) throws DataSourceException {
		if (guideId.equals("E")) {
			return "Exactly";
		}
		return null;
	}

	@Override
	public String getOverview(String guideId) throws DataSourceException {
		return "An Overview";
	}

	@Override
	public List<Episode> getAiredEpisodesBetween(String guideId,
			Date after, Date before) throws DataSourceException {

		List<Episode> episodes = new LinkedList<Episode>();
		episodes.add(new Episode(LAST_EPISODE_SEASON, (short) (LAST_EPISODE_NUMBER + 1),
				new Date(before.getValue() - 10))) ;
		return episodes;
	}

	@Override
	public Series getSeries(String guideId, short id, Calendar date) throws DataSourceException {
		if (guideId.equals("E")) {
			return new Series(id, "Exactly", new Date(date.getTimeInMillis()),
					getName(), guideId, new LinkedList<Episode>());
		}
		return null;
	}

	private ImageFile getImageFile(ImageType type) {
		if (type == ImageType.BANNER) {
			return new ImageFile("image/banner", "BANNER".getBytes());
		}
		else if (type == ImageType.BANNER_THUMBNAIL) {
			return new ImageFile("image/thumbnail", "THUMBNAIL".getBytes());
		}
		else {
			return new ImageFile();
		}
	}


}