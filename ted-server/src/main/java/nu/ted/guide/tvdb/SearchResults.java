package nu.ted.guide.tvdb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Data")
public class SearchResults
{
	public static class TVDBSeries
	{
		@XmlElement
		private int seriesid;

		@XmlElement(name = "SeriesName")
		private String name;

		public String getId()
		{
			return Integer.toString(seriesid);
		}

		public String getName()
		{
			return name;
		}
	}

	@XmlElement(name = "Series")
	private List<TVDBSeries> seriesList = new ArrayList<TVDBSeries>();

	public List<TVDBSeries> getSeriesList()
	{
		return Collections.unmodifiableList(seriesList);
	}

	public int size()
	{
		return seriesList.size();
	}

	public static SearchResults create(InputStream is)
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(SearchResults.class);
			Unmarshaller um = context.createUnmarshaller();
			return (SearchResults) um.unmarshal(is);
		}
		catch (JAXBException e)
		{
			// If we can't parse the results, return none.
			return new SearchResults();
		}
	}

}
