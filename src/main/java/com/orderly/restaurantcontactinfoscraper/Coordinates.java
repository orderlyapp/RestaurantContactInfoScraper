package com.orderly.restaurantcontactinfoscraper;

import javafx.util.Pair;

/**
 * Created by joshuaking on 5/4/16.
 */
public class Coordinates extends Pair<Double, Double> {
	public double lat;
	public double lon;

	public Coordinates (double lat, double lon) {
		super(lat, lon);

		this.lat = lat;
		this.lon = lon;
	}

	public static Coordinates moveBy (Coordinates coordinates, Coordinates moveByCoordinates) {
		Coordinates movedCoordinates = new Coordinates(coordinates.lat, coordinates.lon);
		movedCoordinates.lat += moveByCoordinates.lat;
		movedCoordinates.lon += moveByCoordinates.lon;

		return movedCoordinates;
	}

	public static Coordinates parse (String s) {
		String[] split = s.split(",");
		double lat = Double.parseDouble(split[0].trim());
		double lon = Double.parseDouble(split[1].trim());

		return new Coordinates(lat, lon);
	}

	@Override
	public String toString () {
		return String.format("%s,%s", lat, lon);
	}
}