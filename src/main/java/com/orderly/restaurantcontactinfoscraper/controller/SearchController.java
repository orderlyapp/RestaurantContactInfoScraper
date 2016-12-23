package com.orderly.restaurantcontactinfoscraper.controller;

import com.orderly.restaurantcontactinfoscraper.model.Search;
import com.orderly.restaurantcontactinfoscraper.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Joshua King on 12/22/16.
 */
public class SearchController {
	public static boolean delete (Search searchToDelete) {
		Map<Search, File> allExistingSearchesAsMapFromDir = FileUtils.getAllExistingSearchesAsMapFromDir();
		if (allExistingSearchesAsMapFromDir.keySet().contains(searchToDelete)) {
			File dir = allExistingSearchesAsMapFromDir.get(searchToDelete);
			try { org.apache.commons.io.FileUtils.deleteDirectory(dir); } catch (IOException e) {
				new RuntimeException("An error occurred while deleting a search directory", e).printStackTrace();
				return false;
			}
			return true;
		} else { return false; }
	}
}
