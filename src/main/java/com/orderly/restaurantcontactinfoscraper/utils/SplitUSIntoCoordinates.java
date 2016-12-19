package com.orderly.restaurantcontactinfoscraper.utils;

import com.orderly.restaurantcontactinfoscraper.model.Coordinates;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by joshuaking on 5/2/16.
 */
@SuppressWarnings ("Duplicates")
public class SplitUSIntoCoordinates {

	private static final double     LAT       = 49.175741871935685;
	private static final double     LON       = -124.87946875;
	private static final double[][] US_BORDER = {{38.099983, -74.685059},
												 {37.221580, -75.476074},
												 {36.049099, -75.190430},
												 {35.065973, -75.234375},
												 {32.138409, -80.156250},
												 {30.751278, -81.079102},
												 {26.824071, -79.804688},
												 {25.244696, -80.068359},
												 {24.766785, -81.123047},
												 {27.761330, -83.364258},
												 {28.690588, -82.968750},
												 {29.611670, -83.979492},
												 {29.267233, -85.297852},
												 {30.031055, -86.616211},
												 {29.916852, -89.077148},
												 {28.960089, -88.637695},
												 {28.767659, -91.362305},
												 {29.496988, -93.295898},
												 {27.371767, -96.899414},
												 {25.562265, -96.899414},
												 {26.234302, -99.492188},
												 {29.075375, -100.942383},
												 {29.649869, -102.172852},
												 {28.844674, -103.007813},
												 {29.611670, -104.897461},
												 {31.541090, -106.611328},
												 {31.541090, -108.017578},
												 {31.203405, -108.061523},
												 {31.165810, -111.181641},
												 {32.546813, -115.136719},
												 {32.361403, -117.333984},
												 {34.415973, -121.025391},
												 {40.044438, -124.584961},
												 {43.357138, -124.760742},
												 {45.736860, -124.101563},
												 {48.632909, -124.980469},
												 {48.253941, -123.354492},
												 {48.951366, -123.310547},
												 {49.037868, -123.266602},
												 {49.066668, -95.273438},
												 {49.439557, -95.273438},
												 {49.439557, -94.746094},
												 {48.922499, -94.438477},
												 {48.136767, -89.560547},
												 {48.632909, -88.505859},
												 {47.010226, -84.946289},
												 {46.558860, -84.550781},
												 {45.336702, -82.573242},
												 {43.548548, -82.133789},
												 {41.967659, -82.924805},
												 {42.843751, -79.145508},
												 {43.421009, -79.365234},
												 {43.739352, -76.992188},
												 {45.089036, -75.190430},
												 {45.151053, -71.586914},
												 {47.428087, -69.345703},
												 {47.457809, -68.994141},
												 {47.309034, -68.906250},
												 {47.398349, -68.466797},
												 {47.368594, -68.115234},
												 {47.070122, -67.631836},
												 {45.798170, -67.675781},
												 {44.871443, -66.796875},
												 {44.590467, -66.621094},
												 {43.675818, -69.916992},
												 {41.672912, -69.741211},
												 {40.279526, -73.476563},
												 {38.616870, -74.393921}};

	public static void main (String[] args) throws Exception {
		//  49.175741871935685,-124.87946875
		//  24.937160872053784,-66.31409374999998

		String lines = getCoordinatesInUS().stream().map(Coordinates::toString).map(line -> line.concat("\n")).reduce(String::concat).orElse("");

		String outputDir = "/Users/joshuaking/Desktop/SplitUSIntoCoordinates/";
		new File(outputDir).mkdirs();
		File file = new File(String.format("%s/withinUSCoords.txt", outputDir));
		com.orderly.restaurantcontactinfoscraper.utils.FileUtils.createAndWriteToFile(file, lines, true);
	}

	public static List<Coordinates> getCoordinatesInUS () {
		double fifteenMilesInCoordinatesApprox = 15 / 69.1;
		double lat = LAT;
		double lon = LON;
		double goalLat = 24.937160872053784;
		double goalLon = -66.31409374999998;

		ArrayList<Coordinates> coords = new ArrayList<>();

		while (true) {
			lat -= fifteenMilesInCoordinatesApprox;

			if (lat < goalLat) {
				lat = LAT;
				lon += fifteenMilesInCoordinatesApprox;
			}
			coords.add(new Coordinates(lat, lon));
			if (lon > goalLon) {
				break;
			}
		}

		return coords.stream().filter(SplitUSIntoCoordinates::isInUS).collect(Collectors.toList());
	}

	private static boolean intersects (double[] pointA, double[] pointB, Coordinates point) {
		if (pointA[1] > pointB[1]) { return intersects(pointB, pointA, point); }
		if (point.lon == pointA[1] || point.lon == pointB[1]) { point.lon += 0.0001; }
		if (point.lon > pointB[1] || point.lon < pointA[1] || point.lat > max(pointA[0], pointB[0])) { return false; }
		if (point.lat < min(pointA[0], pointB[0])) { return true; }

		return (point.lon - pointA[1]) / (point.lat - pointA[0]) >= (pointB[1] - pointA[1]) / (pointB[0] - pointA[0]);
	}

	public static boolean isInUS (Coordinates coords) {
		boolean isInUs = false;
		for (int i = 0; i < US_BORDER.length; i++) {
			if (intersects(US_BORDER[i], US_BORDER[(i + 1) % US_BORDER.length], coords)) {
				isInUs = !isInUs;
			}
		}
		return isInUs;
	}
}