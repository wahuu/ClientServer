package pl.server.rest;

import java.util.Date;
import java.util.List;

public class ScheduleResponse {
	private String title;
	private String dateFrom;
	private String dateTo;
	private List<String> mediaNames;

	public ScheduleResponse(String title, String dateFrom, String dateTo, List<String> mediaNames) {
		super();
		this.title = title;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.mediaNames = mediaNames;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public List<String> getMediaNames() {
		return mediaNames;
	}

	public void setMediaNames(List<String> mediaNames) {
		this.mediaNames = mediaNames;
	}

}
