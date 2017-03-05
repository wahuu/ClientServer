package pl.server.ejb;

import java.util.List;

import pl.server.dto.Connections;

public interface ConnectionBean {
	public void addConnection(Connections connection);

	public void delete(Connections connection);

	public List<Connections> getAll();

	public Connections getByIp(String ip);

	public void update(Connections connection);

}
