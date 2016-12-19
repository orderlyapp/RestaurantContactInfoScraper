package com.orderly.restaurantcontactinfoscraper.model;

import java.io.Serializable;

/**
 * Created by Joshua King on 12/19/16.
 */
public class Search implements Serializable {
	private final String  name;
	private final boolean isUsaSearch;
	private final String  searchTerm;
	private final String  factualKey;
	private final String  factualSecret;

	public Search (String name, boolean isUsaSearch, String searchTerm, String factualKey, String factualSecret) {
		this.name = name;
		this.isUsaSearch = isUsaSearch;
		this.searchTerm = searchTerm;
		this.factualKey = factualKey;
		this.factualSecret = factualSecret;
	}

	public String getName () {
		return name;
	}

	public boolean isUsaSearch () {
		return isUsaSearch;
	}

	public String getSearchTerm () {
		return searchTerm;
	}

	public String getFactualKey () {
		return factualKey;
	}

	public String getFactualSecret () {
		return factualSecret;
	}
}