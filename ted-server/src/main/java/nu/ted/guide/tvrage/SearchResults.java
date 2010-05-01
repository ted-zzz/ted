package nu.ted.guide.tvrage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Results")
public class SearchResults
{
	public static class TVRageSeries
	{
		@XmlElement(name = "showid")
		private int seriesid;

		@XmlElement(name = "name")
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

	@XmlElement(name = "show")
	private List<TVRageSeries> seriesList = new ArrayList<TVRageSeries>();

	public List<TVRageSeries> getSeriesList()
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
