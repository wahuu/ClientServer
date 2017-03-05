package pl.server.CDIManagedBeans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;

import pl.server.constants.Status;
import pl.server.dto.Connections;
import pl.server.dto.MediaLibrary;
import pl.server.dto.MessageBuffor;
import pl.server.dto.TextMessageBuffor;
import pl.server.ejb.ConnectionBeanImpl;
import pl.server.ejb.MediaLibraryBeanImpl;
import pl.server.ejb.MessageBeanImpl;
import pl.server.ejb.TextMessageBeanImpl;

@Named
// @SessionScoped
@RequestScoped
public class MainForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	ConnectionBeanImpl connectionBean;
	@Inject
	TextMessageBeanImpl textMessageBeanImpl;
	@Inject
	MediaLibraryBeanImpl mediaLibraryBeanImpl;
	@Inject
	MessageBeanImpl messageBeanImpl;

	List<Connections> connections;
	Connections selectedConnection;
	String textMessage = "";
	MediaLibrary selectedMedia;

	@PostConstruct
	public void init() {
		connections = connectionBean.getAll();
	}

	public void synchronize(ActionEvent event) throws Exception {
		System.out.println();
		String url = "http://localhost:8080/ServerWeb/rest/messages/sendtosockets";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		System.out.println();
	}

/*	public void send(ActionEvent event) {
		FacesMessage message = null;
		if (textMessage != null && !textMessage.equals("") && selectedConnection != null) {
			textMessageBeanImpl
					.addMessage(new TextMessageBuffor(selectedConnection.getIp(), Status.NEW.name(), textMessage));
			message = new FacesMessage("Sent '" + textMessage + "' to ip: " + selectedConnection.getIp());
		} else {
			message = new FacesMessage("Error");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}*/

	/**
	 * Juz nie uzywane
	 * @param med
	 */
/*	public void sendImage(MediaLibrary med) {
		FacesMessage message = null;
		if (med != null && selectedConnection != null) {
			messageBeanImpl.addMessage(new MessageBuffor(selectedConnection.getIp(), Status.NEW.name(), med));
			message = new FacesMessage("Sent '" + med.getName() + "' to ip: " + selectedConnection.getIp());
		} else {
			message = new FacesMessage("Error");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}*/

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		try {
			byte[] bytes = IOUtils.toByteArray(event.getFile().getInputstream());
			mediaLibraryBeanImpl
					.add(new MediaLibrary(event.getFile().getFileName(), event.getFile().getContentType(), bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StreamedContent getImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			String imageId = context.getExternalContext().getRequestParameterMap().get("imageId");
			return new DefaultStreamedContent(
					new ByteArrayInputStream(mediaLibraryBeanImpl.getbyId(Integer.valueOf(imageId)).getData()));
		}
	}

	public List<MediaLibrary> getMedia() {
		return mediaLibraryBeanImpl.getAll();
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public List<Connections> getConnections() {
		return connections;
	}

	public void setConnections(List<Connections> connections) {
		this.connections = connections;
	}

	public Connections getSelectedConnection() {
		return selectedConnection;
	}

	public void setSelectedConnection(Connections selectedConnection) {
		this.selectedConnection = selectedConnection;
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Connection Selected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public MediaLibrary getSelectedMedia() {
		return selectedMedia;
	}

	public void setSelectedMedia(MediaLibrary selectedMedia) {
		this.selectedMedia = selectedMedia;
	}

}
