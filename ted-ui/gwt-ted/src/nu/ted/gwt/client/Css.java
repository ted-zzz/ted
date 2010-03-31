package nu.ted.gwt.client;

/**
 * Supplies CSS constants to the Ted GWT application.
 *
 */
public interface Css {

	public interface Application {
		public static final String TED_HEADER = "ted-header";
		public static final String TED_HEADER_LOGO = TED_HEADER + "-logo";
		public static final String TED_HEADER_TITLE = TED_HEADER + "-title";
		public static final String TED_CONTENT = "ted-content";
		public static final String TED_PAGE_HEADER = "ted-page-header";
		public static final String TED_PAGE_HEADER_TEXT = TED_PAGE_HEADER + "-text";
		public static final String TED_MENU = "ted-menu";
		public static final String TED_MENU_ITEM = TED_MENU + "-item";
		public static final String TED_MENU_SPACER = TED_MENU + "-spacer";

		/**
		 * Appended to TED_MENU_ITEM onMouseOver() - .ted-menu-item-over
		 */
		public static final String TED_MENU_ITEM_OVER = "over";
	}

	public interface Widgets {
		public static final String TED_SERIES_TABLE = "ted-series-table";
		public static final String TED_TABLE_ROW = "ted-table-row";
		public static final String TED_TABLE_ROW_ALTERNATE = TED_TABLE_ROW + "-alternate";
		public static final String TED_TABLE_ROW_SELECTED = TED_TABLE_ROW + "-selected";
	}

	public interface SearchPage {
		public static final String TED_SERIES_LIST = "ted-series-list";
		public static final String TED_SEARCH_SERIES_INFO = "ted-search-series-info";
		public static final String TED_SEARCH_SERIES_IMG = "ted-search-serie-img";
		public static final String TED_SEARCH_SERIES_OVERVIEW = "ted-search-overview";
	}

	public interface WatchedSeriesPage {
		public static final String WATCHED_SERIES = "ted-watched-series";
		public static final String WATCHED_SERIES_IMAGE = WATCHED_SERIES + "-image";
		public static final String WATCHED_SERIES_INFO = WATCHED_SERIES + "-info";
	}

}
