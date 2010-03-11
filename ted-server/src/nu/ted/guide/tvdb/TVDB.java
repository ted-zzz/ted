package nu.ted.guide.tvdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import nu.ted.gen.SeriesSearchResult;
import nu.ted.guide.tvdb.Mirrors;
import nu.ted.guide.tvdb.Mirrors.NoMirrorException;
import nu.ted.guide.tvdb.SearchResults.TVDBSeries;

import nu.ted.domain.Series;
import nu.ted.guide.GuideDB;

public class TVDB implements GuideDB
{
	// TODO: add logger
	private static final String apikey = "0D513FDFA9D09C21";

	private Mirrors mirrors;

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
			mirrorUrl = new URL("http://www.thetvdb.com/api/" + apikey + "/mirrors.xml");

			URLConnection mirrorsConnection = mirrorUrl.openConnection();
			// TODO: set connection timeout

			mirrors = Mirrors.createMirrors(mirrorsConnection.getInputStream());
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

	public Series getSeriesFromUID(String id)
	{
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO Auto-generated method stub
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
				SeriesSearchResult s = new SeriesSearchResult(result.getName(), result.getId());
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

}
