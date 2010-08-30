package nu.ted.torrent.search;

import java.util.HashMap;
import java.util.Map;

public class TorrentSourceTypeIndex {
	static private Map<String, TorrentSourceType> knownTypes = new HashMap<String, TorrentSourceType>();

	public static void addType(TorrentSourceType type) {
		knownTypes.put(type.getName(), type);
	}

	public static TorrentSourceType getTorrentSourceType(String name) {
		return knownTypes.get(name);
	}
}
