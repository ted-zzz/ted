package nu.ted.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.Episode;
import nu.ted.torrent.TorrentTitleMatcher;

public class EpisodeBackendWrapper implements TorrentTitleMatcher
{
	Episode episode;
	List<String> terms;

	public EpisodeBackendWrapper(Episode episode, List<String> defaultTerms) {
		NumberFormat formatter = new DecimalFormat("00");

		this.episode = episode;
		terms = new LinkedList<String>();

		if (defaultTerms != null) {
			for (String term : defaultTerms) {
				term = term.replaceAll("%s", String.valueOf(episode.getSeason()));
				term = term.replaceAll("%e", String.valueOf(episode.getNumber()));

				term = term.replaceAll("%s", formatter.format(episode.getSeason()));
				term = term.replaceAll("%e", formatter.format(episode.getNumber()));
				terms.add(term);
			}
		}
	}

	/**
	 * Match any episode number format in the title
	 */
	public boolean matchTitle(String title) {
		for (String searchTerm : terms) {
			if (title.contains(searchTerm))
				return true;
		}
		return false;
	}
}
