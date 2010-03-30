#!/usr/bin/env thrift --gen java:beans --gen py:new_style

namespace java nu.ted.generated

typedef i64 date

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
	3: date			aired

	4: EpisodeStatus	status
}

struct Series
{
	1: i16			uid,
	2: string		name,

	3: string		guideName,
	4: string		guideId,

	5: list<Episode>	episodes
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

	ImageFile getBanner(1: string searchUID);
		# TODO: throws?

	string getOverview(1: string searchUID);
		# TODO: throws?

}
