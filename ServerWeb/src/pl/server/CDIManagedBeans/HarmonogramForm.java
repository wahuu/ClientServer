package pl.server.CDIManagedBeans;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;

import com.sun.javafx.scene.control.FormatterAccessor;

import pl.server.dto.Connections;
import pl.server.dto.MediaLibrary;
import pl.server.dto.Schedule;
import pl.server.ejb.ConnectionBeanImpl;
import pl.server.ejb.MediaLibraryBeanImpl;
import pl.server.ejb.ScheduleBeanImpl;
import pl.server.pojo.ExtraSchedule;
/**
 * Widok stronki serwera (podstrona harmonogramu)
 *
 */
@Named
@SessionScoped
public class HarmonogramForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	ConnectionBeanImpl connectionBean;
	@Inject
	MediaLibraryBeanImpl mediaLibraryBeanImpl;
	@Inject
	ScheduleBeanImpl scheduleBeanImpl;

	List<String> connectionsList = new ArrayList<String>();
	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent("", new Date(), new Date(),
			new ExtraSchedule(new ArrayList<MediaLibrary>(), ""));;

	@PostConstruct
	public void init() {
		eventModel = new DefaultScheduleModel();
		List<Schedule> allSchedules = scheduleBeanImpl.getAll();
		for (Schedule schedule : allSchedules) {
			Connections connection = schedule.getConnection();
			List<MediaLibrary> media = schedule.getMedia();
			eventModel.addEvent(new DefaultScheduleEvent(schedule.getTitle(), schedule.getDateFrom(),
					schedule.getDateTo(), new ExtraSchedule(media, connection.getIp())));
		}
		List<Connections> all = connectionBean.getAll();
		for (Connections connection : all) {
			connectionsList.add(connection.getIp());
		}
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject(),
				new ExtraSchedule(new ArrayList<MediaLibrary>(), ""));
	}

	public void addEvent(ActionEvent actionEvent) {
		List<Connections> byIp = connectionBean.getByIp(((ExtraSchedule) event.getData()).getIp());
		List<MediaLibrary> media = ((ExtraSchedule) event.getData()).getMedia();
		if (event.getId() == null) {
			eventModel.addEvent(event);
			scheduleBeanImpl.addSchedule(
					new Schedule(event.getTitle(), event.getStartDate(), event.getEndDate(), byIp.get(0), media));
		} else {
			List<Schedule> byTitle = scheduleBeanImpl.getByTitle(event.getTitle());
			if (byTitle != null && byTitle.size()>0) {
				Schedule schedule = byTitle.get(0);
				schedule.setDateFrom(event.getStartDate());
				schedule.setDateTo(event.getEndDate());
				schedule.setMedia(media);
				schedule.setConnection(byIp.get(0));
				scheduleBeanImpl.update(schedule);
				eventModel.updateEvent(event);
			}
			// update
		}

		event = new DefaultScheduleEvent();
	}

	public List<String> getConnectionsList() {
		return connectionsList;
	}

	public void setConnectionsList(List<String> connectionsList) {
		this.connectionsList = connectionsList;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void addMedia(MediaLibrary med) {
		FacesMessage message = null;
		if (med != null) {
			((ExtraSchedule) event.getData()).getMedia().add(med);
		}
	}

	public StreamedContent getImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			String imageId = context.getExternalContext().getRequestParameterMap().get("imageId");
			return new DefaultStreamedContent(new ByteArrayInputStream(
					scale(mediaLibraryBeanImpl.getbyId(Integer.valueOf(imageId)).getData(), 100, 100)));
		}
	}

	public List<MediaLibrary> getMedia() {
		return mediaLibraryBeanImpl.getAll();
	}

	public byte[] scale(byte[] fileData, int width, int height) {
		ByteArrayInputStream in = new ByteArrayInputStream(fileData);
		try {
			BufferedImage img = ImageIO.read(in);
			if (height == 0) {
				height = (width * img.getHeight()) / img.getWidth();
			}
			if (width == 0) {
				width = (height * img.getWidth()) / img.getHeight();
			}
			Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			ImageIO.write(imageBuff, "jpg", buffer);

			return buffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileData;
	}

}
