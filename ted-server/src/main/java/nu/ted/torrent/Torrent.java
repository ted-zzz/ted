package nu.ted.torrent;

public class Torrent {
	
	private String title;
	private String link;
	
	public Torrent(String title, String link) {
		this.title = title;
		this.link = link;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getLink() {
		return link;
	}
}
