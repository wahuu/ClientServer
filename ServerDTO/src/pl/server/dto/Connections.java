package pl.server.dto;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Encja przechowywujaca infomacje o polaczeniach
 * 
 */
@Entity
public class Connections implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue
	@Id
	private int id;
	private String ip;
	private String status;

	public Connections() {
		super();
	}

	public Connections(String ip, String status) {
		super();
		this.ip = ip;
		this.status = status;
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

	@Override
	public String toString() {
		return this.ip;
	}

}
