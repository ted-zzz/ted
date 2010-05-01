package nu.ted.guide.tvrage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;
import nu.ted.guide.tvrage.SearchResults.TVRageSeries;

public class TVRage implements GuideDB {
	public final static String NAME = "TVRage";

	@Override
	public String getName() {
		return TVRage.NAME;
	}

	@Override
	public Series getSeries(String guideId, short id, Calendar date) throws DataSourceException {
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO Auto-generated method stub
	}


	@Override
	public ImageFile getImage(String guideId, ImageType type) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getName(String searchId) throws DataSourceException {
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO Auto-generated method stub
	}

	@Override
	public List<SeriesSearchResult> search(String name) {

		List<SeriesSearchResult> returner = new LinkedList<SeriesSearchResult>();
		String encodedName;
		try {
			encodedName = URLEncoder.encode(name, "UTF-8");
			String location = "http://services.tvrage.com/feeds/search.php?show=" + encodedName;

			URL searchUrl = new URL(location);
			URLConnection searchConnection = searchUrl.openConnection();

			SearchResults searchResults =  SearchResults.create(searchConnection.getInputStream());
			for (TVRageSeries result : searchResults.getSeriesList()) {
				// TODO: TVRageSeries <--> Series conversion should be moved somewhere.
				SeriesSearchResult s = new SeriesSearchResult(result.getId(), result.getName());
				returner.add(s);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returner;
	}

	@Override
	public String getOverview(String guideId) throws DataSourceException {
		// TODO Implement me.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public List<Episode> getAiredEpisodesBetween(String guideId,
			Date after, Date before) throws DataSourceException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
