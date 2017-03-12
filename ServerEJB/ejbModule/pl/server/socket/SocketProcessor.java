package pl.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
/**
 *Nasluchuje  i tworzy nowe sockety 
 */
public class SocketProcessor extends Thread {
	private static SocketProcessor instance = null;
	private ServerSocket listener = null;
	private List<SocketInstance> socketsList = new ArrayList<SocketInstance>(); // lista aktualnych polaczen 

	private SocketProcessor() {
	}

	public static SocketProcessor getInstance() {
		if (instance == null) {
			instance = new SocketProcessor();
		}
		return instance;
	}

	public void run() {
		int clientsCounter = 0;
		try {
			if (listener == null)
				listener = new ServerSocket(9898);
			while (true) {
				SocketInstance socketInstance = new SocketInstance(listener.accept(), clientsCounter); //nasluchuje i tworzy nowego socketu 
				socketInstance.start();
				socketsList.add(socketInstance);
			}
		} catch (IOException e) {
			try {
				listener.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public List<SocketInstance> getSocketsList() {
		return socketsList;
	}

	public void setSocketsList(List<SocketInstance> socketsList) {
		this.socketsList = socketsList;
	}

}
