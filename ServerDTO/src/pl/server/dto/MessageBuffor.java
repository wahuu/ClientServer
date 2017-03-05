package pl.server.dto;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: MessageBuffor
 *
 */
@Entity
public class MessageBuffor implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue
	@Id
	private int id;
	private String ip;
	private String status;
	@OneToOne
	@JoinColumn(name = "media_id")
	private MediaLibrary media;

	public MessageBuffor() {
		super();
	}

	public MessageBuffor(String ip, String status, MediaLibrary media) {
		super();
		this.ip = ip;
		this.status = status;
		this.media = media;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MediaLibrary getMedia() {
		return media;
	}

	public void setMedia(MediaLibrary media) {
		this.media = media;
	}

}
