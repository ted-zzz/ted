package nu.ted.torrent;

public class TorrentRef {

	private String title;
	private String link;
	private long size;

	public TorrentRef(String title, String link, long size) {
		this.title = title;
		this.link = link;
		this.size = size;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public long getSize() {
		return size;
	}
}
