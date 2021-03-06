package pl.server.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Encja przechowywujaca informacje o harmonogramie
 *
 */
@Entity
public class Schedule implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8997414322543783401L;
	@GeneratedValue
	@Id
	private int id;
	private String title;
	private Date dateFrom;
	private Date dateTo;
	@OneToOne
	@JoinColumn(name = "connection_id")
	private Connections connection;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<MediaLibrary> media;

	public Schedule() {
		super();
	}

	public Schedule(String title, Date dateFrom, Date dateTo, Connections connection, List<MediaLibrary> media) {
		super();
		this.title = title;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.connection = connection;
		this.media = media;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Connections getConnection() {
		return connection;
	}

	public void setConnection(Connections connection) {
		this.connection = connection;
	}

	public List<MediaLibrary> getMedia() {
		return media;
	}

	public void setMedia(List<MediaLibrary> media) {
		this.media = media;
	}

}
