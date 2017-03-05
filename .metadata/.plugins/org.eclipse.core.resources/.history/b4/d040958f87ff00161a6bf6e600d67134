package pl.server.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

@Entity
public class MediaLibrary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	@GeneratedValue
	@Id
	private int id;
	private String name;
	private String extension;
	@Lob
	@Column(length = 100000)
	private byte[] data;
	@ManyToMany(mappedBy = "media", fetch = FetchType.EAGER)
	private List<Schedule> employees;

	public MediaLibrary() {
	}

	public MediaLibrary(String name, String extension, byte[] data) {
		super();
		this.name = name;
		this.extension = extension;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
