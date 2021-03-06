package pl.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
/**
 * 
 *Klasa wysylajaca zapytania do restow 
 */
public class RequestProcessor {
	public static final String serverIp = "localhost";

	public static boolean connect() {
		URL url;
		try {
			url = new URL("http://" + serverIp + ":8080/ServerWeb/rest/messages/connect");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void disconnect() {
		URL url;
		try {
			url = new URL("http://" + serverIp + ":8080/ServerWeb/rest/messages/disconnect");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ScheduleResponse[] getSchedule() {
		URL url;
		StringBuilder sb = new StringBuilder();
		try {
			url = new URL("http://" + serverIp + ":8080/ServerWeb/rest/messages/schedule");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			System.out.println("Schedule from Server .... \n");
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ScheduleResponse[] scheduleList = new Gson().fromJson(sb.toString(), ScheduleResponse[].class);
		return scheduleList;
	}

	public static byte[] getImage(String name) {
		URL url;
		StringBuilder sb = new StringBuilder();
		try {
			url = new URL("http://" + serverIp + ":8080/ServerWeb/rest/messages/media/" + name);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/octet-stream");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader((conn.getInputStream())));
			System.out.println("Schedule from Server .... \n");
			String line;
			/*
			 * while ((line = br.readLine()) != null) { sb.append(line + "\n");
			 * } br.close();
			 */
			byte[] buffer = new byte[32768];
			int read = 0;
			InputStream inputStream = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(new File("images\\" + name));
			while ((read = inputStream.read(buffer)) > 0) {
				fos.write(buffer, 0, read);
			}
			fos.close();
			inputStream.close();
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return String.valueOf(sb).getBytes();
	}

}
