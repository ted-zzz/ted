#!/usr/bin/env thrift --gen java:beans --gen py:new_style

namespace java nu.ted.gen

struct SeriesSearchResult
{
	1: string uid,
	2: string name
}

service TedService
{
	list<SeriesSearchResult> search(1: string name);
		# TODO: throws?
}
