package pl.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class SocketInstance extends Thread {
	private Socket socket;
	private int clientNumber;

	public SocketInstance(Socket socket, int clientIp) {
		this.socket = socket;
		try {
			this.socket.setReuseAddress(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.clientNumber = clientIp;
	}

	public void run() {
		try {
			// Decorate the streams so we can send characters
			// and not just bytes. Ensure output is flushed
			// after every newline.
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// Send a welcome message to the client.
			out.println("Hello, you are client #" + clientNumber + ".");
			out.println("Enter a line with only a period to quit\n");
			// Get messages from the client, line by line; return them
			// capitalized
			while (true) {
				String input = in.readLine();
				if (input == null || input.equals(".")) {
					break;
				}
				out.println(input.toUpperCase());
			}
		} catch (IOException e) {
			System.out.println("Error handling client# " + clientNumber + ": " + e);

		} finally {
			try {
				socket.close();
				removeAfterClose();
				// set disconneced for ip
			} catch (IOException e) {
				System.out.println("Couldn't close a socket, what's going on?");
			}
			System.out.println("Connection with client# " + clientNumber + " closed");
		}
	}

	public void sendMessage() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("Harmonogram" + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void removeAfterClose() {
		SocketProcessor instance = SocketProcessor.getInstance();
		List<SocketInstance> socketsList = instance.getSocketsList();
		int toRemove = 0;
		for (int i = 0; i < socketsList.size(); i++) {
			if (this.getClientNumber() == socketsList.get(i).getClientNumber()) {
				toRemove = i;
				break;
			}
		}
		socketsList.remove(toRemove);
	}

	public int getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}

}
