#!/usr/bin/env thrift --gen java:beans --gen py:new_style

namespace java nu.ted.generated

#  1) A client would not be able communicate with a service
#  2) Persisted data will need to be upgraded.
const i32	PROTOCOL_VERSION = 1

const i16	DEFAULT_PORT = 9030

# Standard API errors. These should have clear messages so UI
# designers can fix.
#
# These must be fixable in the UI, and are not to be used for
# exceptions that the UI is unable to predict.
#
# Example exceptions:
#   * Passing a NULL as a parameter where it wasn't expected
#   * Calling stopWatching on a non-watched show
exception InvalidOperation {
	1: string	message
}

struct TDate {
	1: i64			value
}

struct SeriesSearchResult
{
	1: string searchUID,
	2: string name
}

enum EpisodeStatus
{
	UNKNOWN = 1,
	SEARCHING = 2,
	FOUND = 3,
	OLD = 4
}

struct Episode
{
	1: i16				season,
	2: i16				number,
	3: TDate				aired,

	4: optional EpisodeStatus	status
}

struct TorrentSource
{
	1: i16			uid		# Unique id for source.
	2: string		type,	# Unique identifer for type
	3: string		name,   # Unique name for source
	4: string		location
}

# Not currently used, left as idea for how to include custom sources later
#struct TorrentSourceUnion
#{
#	# One or the other should be set, never both:
#	1: string		name,
#	2: list<TorrentSource>	sources
#}

struct Series
{
	1: i16			uid,
	2: string		name,
	3: TDate		lastCheck,

	4: string		guideName,
	5: string		guideId,

	6: list<Episode>	episodes
}

struct TedConfig
{
	1: i16			port = DEFAULT_PORT,	# Listening Port

	# Password for Secure Remote Passwords
	2: string		verifier,
	3: string		salt,

	# Where to look for torrents
	4: list<TorrentSource>	torrentSources,

	# Where to save them when found
	5: string		savePath,

	# Default keywords. Values include:
	# %S = season  %E = episode (zero extended)
	# %s = season  %e = episode (non-extended)
	# (Default: S%SE%E, %sx%e, %sx%E (ex: S01E02, 1x2, 1x02))
	6: list<string>		episodeKeywords,
}


struct UidCache
{
	1: i16	nextUid = 1;
}

struct Ted
{
	1: i32				version = PROTOCOL_VERSION,

	2: TedConfig		config,
	3: list<Series>		series,
	4: UidCache	seriesUidCache,
	5: UidCache sourceUidCache
}

enum ImageType {
	BANNER = 1,
	BANNER_THUMBNAIL = 2
}

struct ImageFile
{
	1: string mimetype,
	2: binary data
}

enum EventType
{
	WATCHED_SERIES_ADDED = 1,
	WATCHED_SERIES_REMOVED = 2,
	EPISODE_ADDED = 3
}

struct Event
{
	1: EventType type,
	2: Series series,
	3: Episode episode,
	4: TDate registeredOn
}

service TedService
{
	# Get the server protocol version
	i32 getVersion();

	# Authentication Functions

	void logout();  # Called automatically on disconnect

	# Find a show
	list<SeriesSearchResult> search(1: string name);
		# TODO: throws?

	# subscribe to the show
	# Returns the Ted-UID of the show.
	i16 startWatching(1: string searchUID)
		throws (1:InvalidOperation invalidOperation);

	# unsubscribe to a show
	void stopWatching(1: i16 uID);
		# TODO: throws? should be void and just throw?

	# get a list of what you're currently subscribed to.
	list<Series> getWatching();
		# TODO: throws?

	# get a single serie by uID.
	Series getSeries(1: i16 uID);
		# TODO: throws?

	ImageFile getImageByGuideId(1: string searchUID, 2: ImageType type);
		# TODO: throws?

	ImageFile getImageBySeriesId(1: i16 uID, 2: ImageType type);
		# TODO: throws?

	string getOverview(1: string searchUID);
		# TODO: throws?

	list<Event> getEvents(1: TDate lastChecked);
		# TODO: throws?

	# --- Commands related to Torrent Sources ---
	list<TorrentSource> 	getTorrentSources(); # TODO: throw
	void			updateTorrentSources(1: list<TorrentSource> sources); #throw

	TorrentSource getTorrentSource(1: i16 id)
		throws (1:InvalidOperation invalidOperation);

	void addTorrentSource(1: TorrentSource source)
		throws (1:InvalidOperation invalidOperation);

	void removeTorrentSource(1: i16 id)
		throws (1:InvalidOperation invalidOperation);

	void updateTorrentSource(1: TorrentSource updatedSource)
		throws (1:InvalidOperation invalidOperation);

	bool torrentSourceExists(1: string name);
}
