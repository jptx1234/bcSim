package com.github.jptx1234.bingchuanSimulator.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

import com.github.jptx1234.bingchuanSimulator.BingchuanSimulator;

public class MACIPGetter {

	public static String[] getMACIP() {
		String osName = BingchuanSimulator.OSNAME.toLowerCase();
		if (osName.contains("Windows")) {
			return getMACIP_Win();
		} else if (osName.contains("linux")) {
			return getMACIP_Linux();
		} else if (osName.contains("mac os")) {
			return getMACIP_MacOS();
		} else {
			return new String[] { "", "" };
		}
	}

	private static String[] getMACIP_Win() {
		Runtime rt = Runtime.getRuntime();
		BufferedReader br = null;
		String[] macip = new String[] { "", "" };
		try {
			Process p = rt.exec("route print 0.0.0.0");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String readed = "";
			while (!readed.startsWith("0.0.0.0")) {
				readed = br.readLine();
				if (readed == null) {
					return macip;
				}
				readed = readed.trim();
			}
			String interFace = null;
			String[] metrics = new String[2];
			while (readed.startsWith("0.0.0.0")) {
				String[] toAnalysis = readed.split(" +");
				if (toAnalysis.length != 5) {
					return macip;
				}
				interFace = toAnalysis[3];
				String metric = toAnalysis[4];
				if (metrics[0] == null || Integer.valueOf(metrics[0]) > Integer.valueOf(metric)) {
					metrics[0] = metric;
					metrics[1] = interFace;
				}
				readed = br.readLine();
				if (readed == null) {
					break;
				}
				readed = readed.trim();
			}
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(interFace));
			StringBuilder macString = new StringBuilder();
			for (byte b : ni.getHardwareAddress()) {
				String bString = Integer.toHexString(0xFF & b);
				if (bString.length() == 1) {
					macString.append("0");
					macString.append(bString);
				} else {
					macString.append(bString);
				}
			}
			macip[0] = interFace;
			macip[1] = macString.toString().toUpperCase();
			return macip;
		} catch (Exception e) {
			return macip;
		}

	}

	private static String[] getMACIP_Linux() {
		Runtime rt = Runtime.getRuntime();
		BufferedReader br = null;
		String[] macip = new String[] { "", "" };
		try {
			Process p = rt.exec("route -e");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String readed = "";
			while (!readed.startsWith("default")) {
				readed = br.readLine();
				if (readed == null) {
					return macip;
				}
				readed = readed.trim();
			}
			String[] toRouteResolved = readed.split(" +");
			String itfc = toRouteResolved[toRouteResolved.length - 1];
			NetworkInterface ni = NetworkInterface.getByName(itfc);
			String IP = ni.getInetAddresses().nextElement().getHostAddress();
			StringBuilder macString = new StringBuilder();
			for (byte b : ni.getHardwareAddress()) {
				String bString = Integer.toHexString(0xFF & b);
				if (bString.length() == 1) {
					macString.append("0");
					macString.append(bString);
				} else {
					macString.append(bString);
				}
			}
			macip[0] = IP;
			macip[1] = macString.toString();

		} catch (Exception e) {
		}
		return macip;
	}

	private static String[] getMACIP_MacOS() {
		Runtime rt = Runtime.getRuntime();
		BufferedReader br = null;
		String[] macip = new String[] { "", "" };
		try {
			Process p = rt.exec("netstat -r");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String readed = "";
			while (!readed.startsWith("default")) {
				readed = br.readLine();
				if (readed == null) {
					return macip;
				}
				readed = readed.trim();
			}
			String[] toRouteResolved = readed.split(" +");
			String itfc = toRouteResolved[toRouteResolved.length - 1];
			NetworkInterface ni = NetworkInterface.getByName(itfc);
			String IP = ni.getInetAddresses().nextElement().getHostAddress();
			StringBuilder macString = new StringBuilder();
			for (byte b : ni.getHardwareAddress()) {
				String bString = Integer.toHexString(0xFF & b);
				if (bString.length() == 1) {
					macString.append("0");
					macString.append(bString);
				} else {
					macString.append(bString);
				}
			}
			macip[0] = IP;
			macip[1] = macString.toString();

		} catch (Exception e) {
		}
		return macip;

	}

}
