package nu.ted.gwt.client.page.watched;

import java.util.List;

import nu.ted.gwt.client.Css;
import nu.ted.gwt.client.image.Images;
import nu.ted.gwt.domain.GwtEpisode;
import nu.ted.gwt.domain.GwtEpisodeStatus;
import nu.ted.gwt.domain.GwtWatchedSeries;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WatchedSeriesWidget extends Composite {

	public WatchedSeriesWidget(GwtWatchedSeries watched, ClickHandler stopWatchingClickHandler) {
		DisclosurePanel content = new DisclosurePanel();
		initWidget(content);

		content.setAnimationEnabled(true);
		content.setHeader(createHeader(watched, stopWatchingClickHandler));
		content.setContent(createContent(watched.getEpisodes()));
	}

	private FlowPanel createHeader(final GwtWatchedSeries watched,
			ClickHandler stopWatchingClickHandler) {
		String thumbImgKey = watched.getName() + watched.getuID();
		String thumbUrl = GWT.getModuleBaseURL() + "images?iid=" + thumbImgKey;
		FlowPanel imagePanel = new FlowPanel();
		imagePanel.setStyleName(Css.WatchedSeriesPage.WATCHED_SERIES_IMAGE);
		imagePanel.add(new Image(thumbUrl));

		Label info = new Label(watched.getName());
		info.setStyleName(Css.WatchedSeriesPage.WATCHED_SERIES_INFO);

		Image stopWatchingImage = new Image(Images.INSTANCE.stopWatchingIcon());
		stopWatchingImage.setStyleName(Css.WatchedSeriesPage.WATCHED_SERIES_ACTION_ITEM);
		stopWatchingImage.setTitle("Stop Watching");
		stopWatchingImage.addClickHandler(stopWatchingClickHandler);

		FlowPanel actionPanel = new FlowPanel();
		actionPanel.setStyleName(Css.WatchedSeriesPage.WATCHED_SERIES_ACTIONS);
		actionPanel.add(stopWatchingImage);

		FlowPanel watchingPanel = new FlowPanel();
//		watchingPanel.setStyleName(Css.WatchedSeriesPage.WATCHED_SERIES);
//		watchingPanel.add(actionPanel);
//		watchingPanel.add(info);
//		watchingPanel.add(imagePanel);

		FlowPanel lhs = new FlowPanel();
		lhs.setStyleName("ted-watched-series-LHS");

		FlowPanel rhs = new FlowPanel();
		rhs.setStyleName("ted-watched-series-RHS");
		rhs.add(stopWatchingImage);

		FlowPanel stuff = new FlowPanel();
		stuff.setStyleName("ted-watched-series-CONTENT");

		watchingPanel.add(rhs);
		watchingPanel.add(stuff);
		watchingPanel.add(imagePanel);
		watchingPanel.add(lhs);
		return watchingPanel;
	}

	private Widget createContent(List<GwtEpisode> episodes) {
		FlowPanel episodeContainer = new FlowPanel();
		episodeContainer.setStyleName(Css.WatchedSeriesPage.EPISODE_CONTAINER);

		if (episodes == null || episodes.isEmpty()) {
			episodeContainer.add(new Label("No Episodes"));
			return episodeContainer;
		}

		for (GwtEpisode episode : episodes) {
			episodeContainer.add(new Label(createSeasonString(episode)));
		}
		return episodeContainer;
	}

	private String createSeasonString(GwtEpisode episode) {
		StringBuffer buf = new StringBuffer();
		buf.append("Season " + episode.getSeason());
		buf.append(" - ");
		buf.append("Episode " + episode.getNumber());
		buf.append(" [" + episode.getAired() + " ]");
		return buf.toString();
	}
}
