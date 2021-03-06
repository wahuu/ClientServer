package pl.client;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

import java.awt.BorderLayout;

public class MainWindow {

	private static JFrame frame;
	private static Socket socket;
	private static BufferedReader in;
	private static final DateFormat df = new SimpleDateFormat("MM/dd/yyy");
	private static JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		if (RequestProcessor.connect()) {
			connectToSocket();
		}
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Polaczenie z socketem i sprawdzeanie czy harmonogram sie nie zmienil
	 */
	private static void connectToSocket() {
		try {
			InetAddress address = InetAddress.getByName(RequestProcessor.serverIp);
			socket = new Socket(address, 9898);
			socket.setReuseAddress(true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				String msg = in.readLine();
				System.out.println(msg + "\n");
				if ("Harmonogram".equals(msg)) {
					// get harmonogram from server
					System.out.println("Zmienil se harmonogram");
					ScheduleResponse[] schedule = RequestProcessor.getSchedule();
					getMedia(schedule);
					saveSchedule(schedule);
					frame.remove(panel);
					panel = new ImgPanel();
					frame.getContentPane().add(panel);
					panel.setVisible(true);
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Pobranie obrazkow wg harmonogramu z serwera
	 * 
	 * @param schedule
	 */
	private static void getMedia(ScheduleResponse[] schedule) {
		// remove all old files from directory
		deleteAll();
		for (ScheduleResponse scheduleResponse : schedule) {
			// get media and save
			for (String mediaName : scheduleResponse.getMediaNames()) {
				byte[] image = RequestProcessor.getImage(mediaName);
				System.out.println();
			}
		}
	}

	private static void deleteAll() {
		File dir = new File("images");
		try {
			FileUtils.cleanDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zapisanie tablicy harmonogramu do pliku txt
	 * 
	 * @param schedule
	 */
	private static void saveSchedule(ScheduleResponse[] schedule) {
		// save schedule txt
		try (PrintWriter out = new PrintWriter("images\\harmonogram.txt")) {
			for (ScheduleResponse scheduleResponse : schedule) {
				out.println(scheduleResponse.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 450);
		frame.setLocationRelativeTo(null);

		panel = new ImgPanel();
		frame.getContentPane().add(panel);
		panel.setVisible(true);

		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				RequestProcessor.disconnect();
				System.exit(0);
			}
		};
		frame.addWindowListener(exitListener);
	}

}
