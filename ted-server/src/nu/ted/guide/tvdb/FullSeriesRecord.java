package nu.ted.guide.tvdb;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "Data")
public class FullSeriesRecord {

	public static class TVDBSeries {

		@XmlElement(name = "banner")
		private String banner;
	
	}

	@XmlElement(name = "Series")
	private TVDBSeries series;

	public String getBanner() {
		return series.banner;
	}

	public static FullSeriesRecord create(InputStream is)
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(FullSeriesRecord.class);
			Unmarshaller um = context.createUnmarshaller();
			return (FullSeriesRecord) um.unmarshal(is);
		}
		catch (JAXBException e)
		{
			// If we can't parse the results, return none.
			return new FullSeriesRecord();
		}
	}

}
