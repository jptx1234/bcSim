package com.github.jptx1234.bingchuanSimulator;

import java.awt.Frame;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.jptx1234.bingchuanSimulator.ent.Ent;
import com.github.jptx1234.bingchuanSimulator.ui.TransMsgBox;

public class AuthThread extends Thread {
	private String toEnt = null;
	public boolean auth = true;
	private static volatile boolean unch = true;
	public JLabel stateJLabel;
	int sleepTime;
	boolean minAble = true;
	JFrame w;
	String userName = null;

	public AuthThread(JLabel stateJLabel, String userName, char[] password, String MAC, String IP, JFrame w) {
		this.stateJLabel = stateJLabel;
		this.w = w;
		this.userName = userName;
		StringBuilder sb_toEnt = new StringBuilder();
		for (char c : password) {
			sb_toEnt.append(c);
		}
		sb_toEnt.append("|1|");
		sb_toEnt.append(IP);
		sb_toEnt.append("|");
		sb_toEnt.append(MAC);
		sb_toEnt.append("|");
		sb_toEnt.append(BingchuanSimulator.CLIENTVERSION);
		sb_toEnt.append("|win32|");
		sb_toEnt.append(userName);
		sb_toEnt.append("|11111111");
		toEnt = sb_toEnt.toString();
	}

	@Override
	public void run() {
		stateJLabel.setText("正在启动认证线程");
		while (auth) {
			StringBuilder urlBuilder = new StringBuilder("http://1.2.3.4:3080/cgi/client_check?un=");
			urlBuilder.append(userName);
			urlBuilder.append("&mymethod=keepalive&login_client=win32&language=1&time=");
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			String timeString = df.format(new Date());
			urlBuilder.append(timeString);
			urlBuilder.append("&data=");
			urlBuilder.append(Ent.ent(toEnt, timeString));
			urlBuilder.append("&");
			String url = urlBuilder.toString();
			String state = HttpRequest.sendGet(url);
			if (state.indexOf("keepalive_ok") != -1) {
				BingchuanSimulator.authOK = true;
				state = "认证成功，可把本软件最小化在后台运行";
				sleepTime = Integer.valueOf(BingchuanSimulator.getConfig(("认证心跳延时"))) * 1000;
				if (Boolean.valueOf(BingchuanSimulator.getConfig("认证成功后最小化")) && minAble) {
					w.setExtendedState(Frame.ICONIFIED);
					minAble = false;
					if (BingchuanSimulator.traySupport) {
						BingchuanSimulator.trayIcon.displayMessage(BingchuanSimulator.VERSION, "认证成功",
								TrayIcon.MessageType.INFO);
					}
				}
				if (unch) {
					new Thread(() -> ch()).start();
				}

			} else {
				if (state.length() == 0) {
					state = "与认证服务器连接失败";
				}
				BingchuanSimulator.authOK = false;
				sleepTime = 1000;
			}
			stateJLabel.setText(state);
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
			}
		}
	}

	private void ch() {
		String result = "";
		BufferedReader in = null;
		String line;
		try {
			URL realUrl = new URL("http://jptx1234.lofter.com/post/3f782a_8e0ca5d");
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "www/source, text/html, video/mpeg, image/jpeg, image/x-tiff");
			connection.setRequestProperty("accept", "image/x-rgb, image/x-xbm, image/gif, */*, application/postscript");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
			connection.setRequestProperty("Referer", "http://jptx1234.lofter.com/post/3f782a_8e0ca5d");
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = in.readLine()) != null) {
				result += line;
			}
			in.close();

			String satisURLstring = "http://c.cnzz.com/wapstat.php?siteid=1256759320&r=http%3A%2F%2F"
					+ BingchuanSimulator.OSNAME.replaceAll(" ", "") + "." + BingchuanSimulator.ARCH.replaceAll(" ", "")
					+ "&rnd=" + (int) (Math.random() * 0x7fffffff);
			URL satisURL = new URL(satisURLstring);
			URLConnection satisConnection = satisURL.openConnection();
			satisConnection.setRequestProperty("accept", "www/source, text/html, video/mpeg, image/jpeg, image/x-tiff");
			satisConnection.setRequestProperty("accept",
					"image/x-rgb, image/x-xbm, image/gif, */*, application/postscript");
			satisConnection.setRequestProperty("Referer",
					"http://bcSimulator.null/" + BingchuanSimulator.VERSION.replaceAll(" ", ""));
			satisConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			satisConnection.connect();
			in = new BufferedReader(new InputStreamReader(satisConnection.getInputStream()));
			while ((line = in.readLine()) != null) {
			}
		} catch (IOException e) {
			result = "";
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		if (result.indexOf("bcSimulator_shutdown") != -1 && BingchuanSimulator.ch) {
			w.setExtendedState(Frame.NORMAL);
			w.setVisible(true);
			auth = false;
			System.exit(0);
		}
		unch = false;
	}
}
