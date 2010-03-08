#!/usr/bin/env thrift --gen java:beans --gen py:new_style

namespace java nu.ted.gen

struct SeriesSearchResult
{
	1: string uid,
	2: string name
}

# TODO: this will be expanded to show the everything a UI would be intersted in a
#       subscribed Series
struct WatchedSeries
{
	# Unique ID _may not_ match the UID from the Search Result
	# UID from search result is the ID used to re-find the
	# Series and add it. The UID here _may_ be a unique mapping
	# to Ted, and translated between the DB serach.
	1: string uid,
	2: string name,
	3: i16 season,
	4: i16 episode
}

service TedService
{
	# Find a show
	list<SeriesSearchResult> search(1: string name);
		# TODO: throws?

	# subscribe to the show
	bool startWatching(1: string uid);
		# TODO: throws? should be void and just throw?
	
	# unsubscribe to a show
	bool stopWatching(1: string uid);
		# TODO: throws? should be void and just throw?

	# get a list of what you're currently subscribed to.
	list<WatchedSeries> getWatching();
		# TODO: throws?

}
