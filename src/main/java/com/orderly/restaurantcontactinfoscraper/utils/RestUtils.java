package com.orderly.restaurantcontactinfoscraper.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Joshua King on 12/19/16.
 */
public class RestUtils {
	public static String urlEscapeSpace (String url) {
		return url.replaceAll(" ", "%20");
	}

	public static String getResponseBody (String url) {
		try {
			URLConnection connection = new URL(url).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;

			String response = "";
			while ((inputLine = in.readLine()) != null) { response += inputLine; }
			in.close();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
