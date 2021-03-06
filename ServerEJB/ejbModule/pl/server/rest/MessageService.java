package pl.server.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.server.constants.Status;
import pl.server.dto.Connections;
import pl.server.dto.MediaLibrary;
import pl.server.dto.Schedule;
import pl.server.ejb.ConnectionBeanImpl;
import pl.server.ejb.MediaLibraryBeanImpl;
import pl.server.ejb.ScheduleBeanImpl;
import pl.server.socket.SocketInstance;
import pl.server.socket.SocketProcessor;

@ApplicationScoped
@Path("/messages")
@Produces("application/json")
@Consumes("application/json")
public class MessageService {

	@Inject
	ConnectionBeanImpl connectionBean;
	@Inject
	ScheduleBeanImpl scheduleBeanImpl;  
	@Inject
	MediaLibraryBeanImpl mediaLibraryBeanImpl;

	private SocketProcessor sp;

	/**
	 * laczenie sie klienta z serwerem
	 * wpisanie do bazy danych nowego polaczenia uzytkownika lub update statusu
	 * polaczenia na connected oraz utworzenie socketa
	 * 
	 * @param req
	 * @return
	 */
	@GET
	@Produces("application/json")
	@Path("/connect")
	public Response connect(@Context HttpServletRequest req) {
		String ip = req.getRemoteAddr(); // pobieranie adresu ip 
		List<Connections> connections = connectionBean.getByIp(ip); //pobranie polaczen z baz danych po adresie ip 
		if (connections.size() < 1) {
			connectionBean.addConnection(new Connections(ip, Status.CONNECTED.name()));
			createOrConnectSocket();
		} else {
			Connections connection = connections.get(0);
			connection.setStatus(Status.CONNECTED.name());
			connectionBean.update(connection);
			createOrConnectSocket();
		}
		return Response.ok("ok").build();
	}

	/**
	 * Tworzenie nowego polaczenia z klientem
	 */
	private void createOrConnectSocket() {
		if (sp == null) {
			sp = SocketProcessor.getInstance();
			sp.start();
		}
	}

	/**
	 * Wyslanie informacji o zmianie harmonogramu - do wszystkich podlaczonych
	 * socketow
	 * 
	 * @param req
	 * @return
	 */
	@GET
	@Produces("application/json")
	@Path("/sendtosockets")
	public Response sendToSockets(@Context HttpServletRequest req) {
		for (SocketInstance socket : sp.getSocketsList()) {
			socket.sendMessage();
		}
		return Response.ok("ok").build();
	}

	/**
	 * Przeslanie harmonogramu dla danego uzytkownika(sprawdzanie go po ip)
	 * 
	 * @param req
	 * @return
	 */
	@GET
	@Produces("application/json")
	@Path("/schedule")
	public Response sendSchedule(@Context HttpServletRequest req) {
		String ip = req.getRemoteAddr();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		List<Schedule> byConnection = scheduleBeanImpl.getByConnection(ip);
		List<ScheduleResponse> scheduleResponses = new ArrayList<ScheduleResponse>();
		for (Schedule schedule : byConnection) {
			List<String> mediaNames = new ArrayList<String>();
			for (MediaLibrary media : schedule.getMedia()) {
				mediaNames.add(media.getName());
			}
			scheduleResponses.add(new ScheduleResponse(schedule.getTitle(),
					df.format(new Date(schedule.getDateFrom().getTime() + TimeUnit.DAYS.toMillis(1))),
					df.format(new Date(schedule.getDateTo().getTime() + TimeUnit.DAYS.toMillis(1))), mediaNames));
		}
		return Response.ok(scheduleResponses).build();
	}

	/**
	 * Wpisanie do bazy danych odlaczenie uzytkownika
	 * 
	 * @param req
	 * @return
	 */
	@GET
	@Produces("application/json")
	@Path("/disconnect")
	public Response disconnect(@Context HttpServletRequest req) {
		String ip = req.getRemoteAddr();
		List<Connections> connections = connectionBean.getByIp(ip);
		if (connections != null && connections.size() > 0) {
			Connections connection = connections.get(0);
			connection.setStatus(Status.DISCONNECTED.name());
			connectionBean.update(connection);
		}
		return Response.ok("ok").build();
	}

	/**
	 * Metoda zwracajaca obraz z biblioteki multimediow
	 * 
	 * @param req
	 * @param name
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/media/{name}")
	public Response getMedia(@Context HttpServletRequest req, @PathParam("name") String name) {
		String remoteAddr = req.getRemoteAddr();
		List<MediaLibrary> byName = mediaLibraryBeanImpl.getByName(name);
		if (byName != null && byName.size() > 0) {
			byte[] encode = java.util.Base64.getEncoder().encode(byName.get(0).getData());
			return Response.ok(byName.get(0).getData()).header("mime", byName.get(0).getExtension())
					.header("name", byName.get(0).getName()).build();
		}
		return Response.noContent().build();
	}

}
