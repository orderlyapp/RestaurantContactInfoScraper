package com.orderly.restaurantcontactinfoscraper.utils;

import javafx.util.Pair;
import org.apache.activemq.artemis.utils.json.JSONObject;

import java.util.Optional;

/**
 * Created by Joshua King on 12/19/16.
 */
public class MapsUtils {
	public static Optional<Pair<Double, Double>> getCoordinatesByCityInfo (String city, String state, String country) {
		try {
			//	https://maps.googleapis.com/maps/api/geocode/json?address=cumming,ga,usa

//			String key = "AIzaSyB0ObIHD41ZiTIQv6P8dwWx4L1h8jA1Jug";
//			String.format("https://maps.googleapis.com/maps/api/geocode/json?key=%s&address=<%s,%s,%s>", key, city, state, country);
			String url = RestUtils.urlEscapeSpace(String.format("https://maps.googleapis.com/maps/api/geocode/json?address=<%s,%s,%s>", city, state, country));
			String response = RestUtils.getResponseBody(url);

			JSONObject obj = new JSONObject(response);
			if (!obj.getString("status").equalsIgnoreCase("OK")) {
				throw new Exception("Return Status was NOT \"OK\"!\n\n" + response);
			}
			JSONObject res = obj.getJSONArray("results").getJSONObject(0);
			JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
			double lat = loc.getDouble("lat");
			double lon = loc.getDouble("lng");

			return Optional.of(new Pair<>(lat, lon));
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}
}
