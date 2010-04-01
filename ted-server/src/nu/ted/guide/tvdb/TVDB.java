package nu.ted.guide.tvdb;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.tvdb.Mirrors;
import nu.ted.guide.tvdb.Mirrors.NoMirrorException;
import nu.ted.guide.tvdb.SearchResults.TVDBSeries;

import nu.ted.guide.GuideDB;

public class TVDB implements GuideDB
{
	public static final String NAME = "TVDB";
	private static final String APIKEY = "0D513FDFA9D09C21";

	private Mirrors mirrors;
	private ImageFileFactory imageFactory;

	private static class TVDBHolder
	{
		private static final TVDB INSTANCE = new TVDB();
	}

	public static TVDB getInstance()
	{
		return TVDBHolder.INSTANCE;
	}

	public TVDB()
	{
		URL mirrorUrl;
		try
		{
			mirrorUrl = new URL("http://www.thetvdb.com/api/" + APIKEY + "/mirrors.xml");

			URLConnection mirrorsConnection = mirrorUrl.openConnection();
			// TODO: set connection timeout

			this.mirrors = Mirrors.createMirrors(mirrorsConnection.getInputStream());
			this.imageFactory = new ImageFileFactory(this.mirrors);
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoMirrorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getName() {
		return TVDB.NAME;
	}


	// TODO: this is slow for the moment, as it does a lookup each time. Will be improved.
	private FullSeriesRecord getFullSeriesRecord(String id) throws NoMirrorException, IOException
	{
		String location = mirrors.getXMLMirror() + "/api/" + APIKEY + "/series/" + id + "/all/";
		URL seriesURL = new URL(location);
		URLConnection seriesConnection = seriesURL.openConnection();

		return FullSeriesRecord.create(seriesConnection.getInputStream());

	}

	public String getName(String id)
	{
		FullSeriesRecord record = null;
		try {
			record = getFullSeriesRecord(id);
		} catch (NoMirrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (record != null) {
			return record.getName();
		}
		return null; // TODO: fix
	}

	@Override
	public ImageFile getImage(String guideId, ImageType type)
	{
		try {
			FullSeriesRecord record = getFullSeriesRecord(guideId);
			return imageFactory.createImage(record, type);
		} catch (NoMirrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageFileFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Throw new exception.
		return new ImageFile();
	}

	public List<SeriesSearchResult> search(String name)
	{
		URL searchUrl;
		String nameEncoded;
		String location;
		List<SeriesSearchResult> returner = new LinkedList<SeriesSearchResult>();
		try
		{
			nameEncoded = URLEncoder.encode(name, "UTF-8");
			//searchUrl = new URL("http://www.thetvdb.com/api/GetSeries.php?seriesname=" + nameEncoded);
			location = mirrors.getXMLMirror() + "/api/GetSeries.php?seriesname=" + nameEncoded;
			searchUrl = new URL(location);
			URLConnection searchConnection = searchUrl.openConnection();

			SearchResults searchResults =  SearchResults.create(searchConnection.getInputStream());
			for (TVDBSeries result : searchResults.getSeriesList()) {
				// TODO: TVDBSeries <--> Series conversion should be moved somewhere.
				SeriesSearchResult s = new SeriesSearchResult(result.getId(), result.getName());
				returner.add(s);
			}
		}
		catch (NoMirrorException e)
		{
			// TODO: log error
			// TODO: something else?
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO: log error
			throw new RuntimeException(e);
		}
		catch (MalformedURLException e)
		{
			// TODO: log error
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			// TODO: log error
			// If we can't connect, return no search results.
		}
		return returner;
	}

	public Episode getNextEpisode(String guideID, Calendar date) {
		try {
			FullSeriesRecord record = getFullSeriesRecord(guideID);
			return record.getNextEpisode(date);
		} catch (NoMirrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO: no new episode exception?
		return null;
	}

	@Override
	public String getOverview(String guideId) {
		try {
			FullSeriesRecord series = getFullSeriesRecord(guideId);
			return series.getOverview();
		} catch (NoMirrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: Should throw an exception instead of returning null.
		return "";
	}

	@Override
	public List<Episode> getNewAiredEpisodes(String guideId, Calendar date,
			Episode lastEpisode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Series getSeries(String guideId, short id, Calendar date) {
		try {
			FullSeriesRecord record = getFullSeriesRecord(guideId);

			Series s = new Series(id, record.getName(), new Date(date.getTimeInMillis()),
					getName(), guideId, new LinkedList<Episode>());
			return s;
		} catch (NoMirrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

}
