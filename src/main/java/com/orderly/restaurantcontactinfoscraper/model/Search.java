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
	private final String  city;
	private final String  state;
	private final String  country;
	private final int     totalBlockSizeMiles;
	private final String  outputDirectory;

	public Search (String name, String searchTerm, String factualKey, String factualSecret) {
		this.name = name;
		this.city = null;
		this.state = null;
		this.country = null;
		this.totalBlockSizeMiles = -1;
		this.isUsaSearch = true;
		this.searchTerm = searchTerm;
		this.factualKey = factualKey;
		this.factualSecret = factualSecret;
		this.outputDirectory = String.valueOf(System.currentTimeMillis());
	}

	public Search (String name, String city, String state, String country, int totalBlockSizeMiles, String searchTerm, String factualKey, String factualSecret) {
		this.name = name;
		this.city = city;
		this.state = state;
		this.country = country;
		this.totalBlockSizeMiles = totalBlockSizeMiles;
		this.isUsaSearch = false;
		this.searchTerm = searchTerm;
		this.factualKey = factualKey;
		this.factualSecret = factualSecret;
		this.outputDirectory = String.valueOf(System.currentTimeMillis());
	}

	public String getOutputDirectory () {
		return outputDirectory;
	}

	@Override public String toString () {
		return String.format("name='%s', searchTerm='%s'", name, searchTerm);
	}

	@Override public boolean equals (Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		Search search = (Search) o;

		if (isUsaSearch() != search.isUsaSearch()) { return false; }
		if (getName() != null ? !getName().equals(search.getName()) : search.getName() != null) { return false; }
		if (getSearchTerm() != null ? !getSearchTerm().equals(search.getSearchTerm()) : search.getSearchTerm() != null) { return false; }
		if (getFactualKey() != null ? !getFactualKey().equals(search.getFactualKey()) : search.getFactualKey() != null) { return false; }
		return getFactualSecret() != null ? getFactualSecret().equals(search.getFactualSecret()) : search.getFactualSecret() == null;
	}

	public boolean isUsaSearch () {
		return isUsaSearch;
	}

	public String getName () {
		return name;
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

	public String getCity () {
		return city;
	}

	public String getState () {
		return state;
	}

	public String getCountry () {
		return country;
	}

	public int getTotalBlockSizeMiles () {
		return totalBlockSizeMiles;
	}

	@Override public int hashCode () {
		int result = getName() != null ? getName().hashCode() : 0;
		result = 31 * result + (isUsaSearch() ? 1 : 0);
		result = 31 * result + (getSearchTerm() != null ? getSearchTerm().hashCode() : 0);
		result = 31 * result + (getFactualKey() != null ? getFactualKey().hashCode() : 0);
		result = 31 * result + (getFactualSecret() != null ? getFactualSecret().hashCode() : 0);
		return result;
	}
}