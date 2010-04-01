#!/usr/bin/env thrift --gen java:beans --gen py:new_style

namespace java nu.ted.generated

struct Date {
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
	1: i16			season,
	2: i16			number,
	3: Date			aired,

	4: EpisodeStatus	status
}

struct Series
{
	1: i16			uid,
	2: string		name,
	3: Date			lastCheck,

	4: string		guideName,
	5: string		guideId,

	6: list<Episode>	episodes
}

struct TedConfig
{
	1: i16			port	# Listening Port
}

struct Ted
{
	1: TedConfig		config,
	2: list<Series>		series
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

service TedService
{
	# Find a show
	list<SeriesSearchResult> search(1: string name);
		# TODO: throws?

	# subscribe to the show
	# Returns the Ted-UID of the show.
	i16 startWatching(1: string searchUID);
		# TODO: throws? should be void and just throw?

	# unsubscribe to a show
	void stopWatching(1: i16 uID);
		# TODO: throws? should be void and just throw?

	# get a list of what you're currently subscribed to.
	list<Series> getWatching();
		# TODO: throws?

	ImageFile getImageByGuideId(1: string searchUID, 2: ImageType type);
		# TODO: throws?

	ImageFile getImageBySeriesId(1: i16 uID, 2: ImageType type);
		# TODO: throws?

	string getOverview(1: string searchUID);
		# TODO: throws?

}
