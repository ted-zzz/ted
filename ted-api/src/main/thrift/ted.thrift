#!/usr/bin/env thrift --gen java:beans --gen py:new_style

namespace java nu.ted.generated

typedef string date

struct SeriesSearchResult
{
	1: string searchUID,
	2: string name
}

struct CurrentEpisode
{
	1: i16 season,
	2: i16 number,
	3: date aired
}

enum EpisodeStatus
{
	SEARCHING = 1,
	FOUND = 2
}

# This is an Episode that has come out and is on a Series.
struct AiredEpisode
{
	1: i16 season,
	2: i16 number,
	3: date aired,

	4: EpisodeStatus status

}

# TODO: this will be expanded to show the everything a UI would be intersted in a
#       subscribed Series
struct WatchedSeries
{
	# UID does not match searchUID. SearchUID is known only by
	# the current search source, whereas UID is Ted's internal
	# ID number for this series.
	1: i16 uID,
	2: string name,

	3: CurrentEpisode currentEpisode,
	4: list<AiredEpisode> airedEpisodes
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
	list<WatchedSeries> getWatching();
		# TODO: throws?

	ImageFile getBanner(1: string searchUID);
		# TODO: throws?

	string getOverview(1: string searchUID);
		# TODO: throws?

}
