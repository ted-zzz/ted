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
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.ImageFile;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.tvdb.Mirrors;
import nu.ted.guide.tvdb.Mirrors.NoMirrorException;
import nu.ted.guide.tvdb.SearchResults.TVDBSeries;

import nu.ted.domain.Series;
import nu.ted.guide.GuideDB;

public class TVDB implements GuideDB
{
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

	public ImageFile getBanner(String id)
	{
		URL seriesURL;
		String location;
		String mimetype;
		ImageFile file = new ImageFile();
		
		try {
			location = mirrors.getXMLMirror() + "/api/" + apikey + "/series/" + id + "/all/";
			seriesURL = new URL(location);
			URLConnection seriesConnection = seriesURL.openConnection();
			
			FullSeriesRecord record = FullSeriesRecord.create(seriesConnection.getInputStream());
			
			String banner = record.getBanner();
			if (banner != null) {
				location = mirrors.getBannerMirror() + "/banners/" + banner;
				URL bannerURL = new URL(location);
				URLConnection bannerURLConnection = bannerURL.openConnection();
				InputStream iStream = new BufferedInputStream(bannerURLConnection.getInputStream());
				mimetype = bannerURLConnection.getContentType();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while ((n = iStream.read(buf)) != -1) {
					out.write(buf, 0, n);
				}
				out.close();
				iStream.close();
				file.setData(out.toByteArray());
				file.setMimetype(mimetype);
				return file;
			}
		} catch (NoMirrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: return null? throw exception?
		return file;
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

}
