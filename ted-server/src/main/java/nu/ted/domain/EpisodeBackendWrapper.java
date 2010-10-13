package nu.ted.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.Episode;

public class EpisodeBackendWrapper
{
	Episode episode;

	public EpisodeBackendWrapper(Episode episode) {
		this.episode = episode;
	}

	/* For now this just returns S01E01 format. Later we may want
	 * to support other formats in an 'OR' structure. Currently
	 * all search terms are 'AND'd.
	 */
	public List<String> getSearchTerms() {
		List<String> terms = new LinkedList<String>();

		NumberFormat formatter = new DecimalFormat("00");
		terms.add("S" + formatter.format(episode.getSeason()) +
				"E" + formatter.format(episode.getNumber()));

		return terms;

	}
}
