package pl.server.pojo;

import java.util.List;

import pl.server.dto.Connections;
import pl.server.dto.MediaLibrary;

public class ExtraSchedule {

	private List<MediaLibrary> media;
	private String ip;

	public ExtraSchedule(List<MediaLibrary> media, String ip) {
		super();
		this.media = media;
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<MediaLibrary> getMedia() {
		return media;
	}

	public void setMedia(List<MediaLibrary> media) {
		this.media = media;
	}

}
