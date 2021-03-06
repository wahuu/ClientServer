package pl.client;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ImgPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public List<String> imgList = new ArrayList<String>();
	public List<ScheduleResponse> schedulesList = new ArrayList<ScheduleResponse>();
	int size = 0;
	JLabel lblNewLabel;
	JLabel titlesLabel;
	Timer tm;
	int interval = 10;
	int showImgCounter = 0;
	String titles = "";

	/**
	 * Create the panel.
	 */
	public ImgPanel() {
		lblNewLabel = new JLabel();
		lblNewLabel.setBounds(10, 11, 780, 336);
		add(lblNewLabel);
		readFiles();
		setImgListBySchedule();
		if (imgList.size() > 0)
			showImg(size - 1);

		setLayout(null);
		setSize(800, 400);
		
		titlesLabel = new JLabel(titles);
		titlesLabel.setBounds(10, 375, 173, 14);
		add(titlesLabel);
		setVisible(true);

		tm = new Timer(interval * 100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// change image
				showImg(showImgCounter);
				showImgCounter++;
				if (showImgCounter >= size)
					showImgCounter = 0;
			}
		});
		tm.start();
	}

	/**
	 * Ustawienie listy obrazkow ktora maja byc wyswietlane wg harmonogramow
	 */
	private void setImgListBySchedule() {
		for (ScheduleResponse schedule : schedulesList) {
			try {
				Date dateFrom = df.parse(schedule.getDateFrom());
				Date dateTo = df.parse(schedule.getDateTo());
				Date time = Calendar.getInstance().getTime();
				if (time.after(dateFrom) && time.before(dateTo)) {
					for (String mediaName : schedule.getMediaNames()) {
						imgList.add(mediaName);
						size++;
					}
					titles = titles + " " + schedule.getTitle();
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Ustawienie obrazka po numerze w liscie imgList
	 * 
	 * @param i
	 */
	private void showImg(int i) {
		try {
			ImageIcon imgIcon = new ImageIcon("images\\" + imgList.get(i));
			Image img = imgIcon.getImage();
			Image scaledInstance = img.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(),
					Image.SCALE_SMOOTH);
			ImageIcon imageIcon = new ImageIcon(scaledInstance);
			lblNewLabel.setIcon(imgIcon);
		} catch (Exception e) {
			lblNewLabel.setText("Nothing to show.");
		}
	}

	/**
	 * Wczytanie pliku harmonogramu z folderu image oraz parsowanie go na
	 * obiekty harmonogramow
	 */
	public void readFiles() {
		File dir = new File("images");
		File[] listFiles = dir.listFiles();
		if (listFiles != null && listFiles.length > 0)
			for (File file : listFiles) {
				if (file.isFile()) {
					/*
					 * if (file.getName().contains(".jpg")) {
					 * imgList.add("images\\" + file.getName()); size++; }
					 */
					if (file.getName().contains(".txt")) {
						List<String> schedules = new ArrayList<String>();
						try (Stream<String> stream = Files.lines(Paths.get("images\\" + file.getName()))) {
							schedules = stream.collect(Collectors.toList());
						} catch (IOException e) {
							e.printStackTrace();
						}
						// Parsowanie wczytaniego pliku na obiekty
						// ScheduleResponse i zapisanie do listy schedulesList
						for (String string : schedules) {
							String[] split = string.split(" ");
							List<String> mediaNames = new ArrayList<String>();
							for (int i = 3; i < split.length; i++) {
								mediaNames.add(split[i]);
							}
							schedulesList.add(new ScheduleResponse(split[0], split[1], split[2], mediaNames));
						}
						System.out.println();
					}
				}
			}
	}
}
