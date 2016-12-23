package com.orderly.restaurantcontactinfoscraper.utils;

import com.orderly.restaurantcontactinfoscraper.model.Search;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Joshua King on 12/19/16.
 */
public class FileUtils {
	private static final String EXISTING_SEARCHES_DIRECTORY_NAME = "_EXISTING_SEARCHES_";

	public static void createAndWriteToFile (File file, String data, boolean openWhenDone, OpenOption... options) throws IOException {
		file.createNewFile();
		if (file.exists() && file.canWrite()) {
			Files.write(file.toPath(), data.getBytes(), options);
			if (openWhenDone) {
				Desktop.getDesktop().open(file);
			}
		} else {
			System.out.println(data);
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("An error occurred while creating the file so the data was output into the console.");
		}
	}

	public static File[] getExistingSearchesAsFiles () {
		File[] files = FileUtils.getExistingSearchesDir().listFiles();
		if (files == null) { files = new File[0]; }
		return files;
	}

	public static File getExistingSearchesDir () {
		return new File("./" + EXISTING_SEARCHES_DIRECTORY_NAME);
	}

	public static void saveToDirectory (Search search, File dir) {
		if (!dir.exists() && !dir.mkdirs()) {
			System.err.println("AN ERROR OCCURRED WHILE SAVING - Could not create directory at: " + dir.getAbsolutePath());
			return;
		}

		try {
			String s = dir.getPath() + "/" + search.getOutputDirectory();
			new File(s).mkdirs();
			FileOutputStream fos = new FileOutputStream(s + "/object.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(search);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Set<Search> getAllExistingSearches () {
		Map<Search, File> allFromDirectory = getAllFromDirectory(getExistingSearchesDir());
		return allFromDirectory.keySet();
	}

	public static Map<Search, File> getAllFromDirectory (File dir) {
		Map<Search, File> searchFileMap = new HashMap<>();

		if (dir.exists()) {
			File[] files = dir.listFiles();
			files = files == null ? new File[0] : files;

			Arrays.stream(files).forEach(file -> {
				try {
					FileInputStream fis = new FileInputStream(file.getPath() + "/object.ser");
					ObjectInputStream ois = new ObjectInputStream(fis);
					searchFileMap.put((Search) ois.readObject(), file);
					ois.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		return searchFileMap;
	}

	public static Map<Search, File> getAllExistingSearchesAsMapFromDir () {
		return getAllFromDirectory(getExistingSearchesDir());
	}
}
