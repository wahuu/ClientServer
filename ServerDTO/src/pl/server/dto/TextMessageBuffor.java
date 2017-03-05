package pl.server.dto;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: MessageBuffor
 *
 */
@Entity
public class TextMessageBuffor implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue
	@Id
	private int id;
	private String ip;
	private String status;
	private String data;

	public TextMessageBuffor() {
		super();
	}

	public TextMessageBuffor(String ip, String status, String data) {
		super();
		this.ip = ip;
		this.status = status;
		this.data = data;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
