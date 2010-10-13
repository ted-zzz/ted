package nu.ted.torrent.search;

import java.util.HashMap;
import java.util.Map;

import nu.ted.generated.TorrentSource;

/**
 *  This reason for this index is to have objects to store the torrent sources in.
 *  Then we don't have to keep hitting the (online) feed for every episode.
 *
 *  It also includes a an set of factories for actually creating the sources.
 */
public class TorrentSourceIndex {

	// Map of type, location --> torrent source
	static private Map<String, Map<String, TorrentSourceType>> torrentSources =
		new HashMap<String, Map<String, TorrentSourceType>>();

	static private Map<String, TorrentSourceTypeFactory> torrentSourceFactories =
		new HashMap<String, TorrentSourceTypeFactory>();

	public static void registerFactory(String type, TorrentSourceTypeFactory factory) {
		if (torrentSourceFactories.containsKey(type)) {
			throw new RuntimeException("Factory already registered.");
		}
		torrentSourceFactories.put(type, factory);
	}

	public static void clear() {
		torrentSourceFactories.clear();
	}

	public static TorrentSourceType getTorrentSourceType(TorrentSource source) {
		String type = source.getType();
		String location = source.getLocation();

		if (torrentSources.containsKey(type) && torrentSources.get(type).containsKey(location)) {
			return torrentSources.get(type).get(location);
		}

		if (!torrentSources.containsKey(type)) {
			torrentSources.put(type, new HashMap<String, TorrentSourceType>());
		}

		if (!torrentSourceFactories.containsKey(type)) {
			throw new RuntimeException("No known factores for type " + type);
		}
		TorrentSourceTypeFactory factory = torrentSourceFactories.get(type);
		TorrentSourceType tst = factory.createTorrentSourceType(source);

		torrentSources.get(type).put(location, tst);
		return tst;
	}
}
