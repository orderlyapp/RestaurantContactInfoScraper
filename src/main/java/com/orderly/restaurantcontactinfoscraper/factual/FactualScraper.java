package com.orderly.restaurantcontactinfoscraper.factual;

import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.orderly.restaurantcontactinfoscraper.controller.SearchController;
import com.orderly.restaurantcontactinfoscraper.model.Search;
import com.orderly.restaurantcontactinfoscraper.utils.FileUtils;
import com.orderly.restaurantcontactinfoscraper.utils.MapsUtils;
import com.orderly.restaurantcontactinfoscraper.utils.console.ConsoleUtils;
import com.orderly.restaurantcontactinfoscraper.utils.console.KeyIn;
import com.orderly.restaurantcontactinfoscraper.utils.console.Options;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.orderly.restaurantcontactinfoscraper.controller.SearchController.exit;

/**
 * Created on 5/2/16.
 * <p>
 * http://www.darrinward.com/lat-long/?id=1935749
 * <p>
 * KEY    : WynD7vY8DuZXFSsQng6ePIsaT3pPUgDx5TwZV41f
 * SECRET : tqP4PIK5oKIpRF5mRxRJJI1IDrUp8gXI4fKIPUjl
 */
public class FactualScraper {
	public static void main (String[] args) throws Exception {
		ConsoleUtils.clearConsole();
		while (true) {
			int numberOfExistingSearches = FileUtils.getExistingSearchesAsFiles().length;

			Options<String> toDoOptions = new Options<>("What would you like to do?");
			Options.Item<String> createNewSearchItem = toDoOptions.add("Create a new search");
			Options.Item<String> runExistingSearchItem = toDoOptions.add("Run an existing search");
			Options.Item<String> deleteASearchItem = toDoOptions.add("Delete a search");
			Options.Item<String> displayAllSearchesItem = toDoOptions.add(String.format("Display all %d searches", numberOfExistingSearches));
			Options.Item<String> viewResultsItem = toDoOptions.add("View results from a search");
			Options.Item<String> checkApiUsageItem = toDoOptions.add("Check API Usage");
			Options.Item<String> exitItem = toDoOptions.add("Exit");
			Options.Item<String> toDo = toDoOptions.getUserChoice().orElse(exitItem);

			if (toDo.equals(createNewSearchItem)) {
				ConsoleUtils.clearConsole();
				createNewSearch();
			}
			else if (toDo.equals(runExistingSearchItem)) {
				ConsoleUtils.clearConsole();
				runASearch();
			}
			else if (toDo.equals(deleteASearchItem)) {
				ConsoleUtils.clearConsole();
				deleteASearch();
			}
			else if (toDo.equals(displayAllSearchesItem)) {
				ConsoleUtils.clearConsole();
				displayExistingSearches();
			}
			else if (toDo.equals(exitItem)) {
				System.out.println();
				System.out.println("Have a nice day!");
				exit();
			}
			else if (toDo.equals(viewResultsItem)) {
				ConsoleUtils.clearConsole();
				viewResultsForASearch();
			}
			else if (toDo.equals(checkApiUsageItem)) {
				ConsoleUtils.clearConsole();
				checkApiUsage();
			}
			else {
				exit();
			}
		}
	}

	private static void checkApiUsage () {
		ConsoleUtils.printBox("Paste your Factual Key below", 3);
		String factualKey = KeyIn.inString("Key: ");
		ConsoleUtils.newLine(3);

		ConsoleUtils.printBox("Paste your Factual Secret below\n[Not your key]", 3);
		String factualSecret = KeyIn.inString("Secret: ");
		while (factualSecret.equals(factualKey)) {
			System.out.println("Your Factual Key and Secret should different. Please paste your Factual Secret below.");
			factualSecret = KeyIn.inString("Secret: ");
		}
		ConsoleUtils.newLine(3);

		Factual factual = new Factual(factualKey, factualSecret);
		Query query = new Query().limit(1);
		ReadResponse readResponse = factual.fetch("restaurants-us", query);
		String throttleInfo = readResponse.getRawResponse().getHeaders().getFirstHeaderStringValue("X-Factual-Throttle-Allocation");
		SearchController.printThrottleInfo(throttleInfo);
		ConsoleUtils.newLine(3);
	}

	private static void viewResultsForASearch () {
		Options<Search> existingSearchesList = new Options<>("View Search Results", "Enter the number to view:");
		FileUtils.getAllExistingSearches().forEach(existingSearchesList::add);
		existingSearchesList.setCancelListener(() -> {
			ConsoleUtils.clearConsole();
			System.out.println("No search was selected.\n");
		});
		existingSearchesList.getUserChoice().map(Options.Item::getPayload).ifPresent(SearchController::viewResults);
	}


	private static void runASearch () {
		Options<Search> existingSearchesList = new Options<>("Run a Search", "Enter the number to run:");
		FileUtils.getAllExistingSearches().forEach(existingSearchesList::add);
		existingSearchesList.setCancelListener(() -> {
			ConsoleUtils.clearConsole();
			System.out.println("No search was run.\n");
		});
		existingSearchesList.getUserChoice().map(Options.Item::getPayload).ifPresent(SearchController::run);
	}

	public static void createNewSearch () throws IOException, URISyntaxException {
		ConsoleUtils.printBox("What would you like to name this search?", 3);
		String searchName = KeyIn.inString("Name: ");
		ConsoleUtils.newLine(3);

		Options<String> options = new Options<>("USA or specific city?");
		Options.Item<String> isUsaItem = options.add("USA");
		Options.Item<String> singleCityItem = options.add("Single City");
		options.setCancelListener(() -> System.out.println("Cancelled new search."));
		options.getUserChoice().ifPresent(usaOrCity -> {
			ConsoleUtils.newLine(3);

			String country = null;
			String state = null;
			String city = null;
			int totalBlockSizeMiles = 0;
			if (usaOrCity.equals(singleCityItem)) {
				ConsoleUtils.printBox("What country do you want to search in?", 3);
				country = KeyIn.inString("Country: ");
				ConsoleUtils.newLine(3);

				ConsoleUtils.printBox("What state/province do you want to search in?", 3);
				state = KeyIn.inString("State: ");
				ConsoleUtils.newLine(3);

				ConsoleUtils.printBox("What city do you want to search in?", 3);
				city = KeyIn.inString("City: ");
				ConsoleUtils.newLine(3);

				Optional<Pair<Double, Double>> coordinatesOptional = MapsUtils.getCoordinatesByCityInfo(city, state, country);

				if (!coordinatesOptional.isPresent()) {
					ConsoleUtils.clearConsole();
					System.out.println("City/State/Country combination not found. Canceling new search.");
					System.out.printf("The inputted City, State, Country: %s, %s, %s%n", city, state, country);
					ConsoleUtils.newLine(2);
					return;
				}
				else {
					System.out.println("City of " + city + " was verified with Google. ✔️");
					ConsoleUtils.newLine(2);
				}

				ConsoleUtils.printBox("How many miles wide should be searched?", 3);
				totalBlockSizeMiles = KeyIn.inInt("Miles: ");
				ConsoleUtils.newLine(3);
			}

			ConsoleUtils.printBox("What's your search term?", 3);
			String searchTerm = KeyIn.inString("Search Term: ");
			ConsoleUtils.newLine(3);

			ConsoleUtils.printBox("Paste your Factual Key below\n[Don't have a key? Just hit enter to get one.]", 3);
			String factualKey = KeyIn.inString("Key: ");
			if (factualKey.length() == 0) {
				if (Desktop.isDesktopSupported()) {
					try { Desktop.getDesktop().browse(new URI("https://www.factual.com/api-keys/request")); }
					catch (Exception e) { e.printStackTrace(); }
				}
				exit();
			}
			ConsoleUtils.newLine(3);

			ConsoleUtils.printBox("Paste your Factual Secret below\n[Not your key]", 3);
			String factualSecret = KeyIn.inString("Secret: ");
			while (factualSecret.equals(factualKey)) {
				System.out.println("Your Factual Key and Secret should different. Please paste your Factual Secret below.");
				factualSecret = KeyIn.inString("Secret: ");
			}
			ConsoleUtils.newLine(3);

			Search search;
			if (usaOrCity.equals(isUsaItem)) { search = new Search(searchName, searchTerm, factualKey, factualSecret); }
			else {
				search = new Search(searchName, city, state, country, totalBlockSizeMiles, searchTerm, factualKey, factualSecret);
			}
			FileUtils.saveToDirectory(search, FileUtils.getExistingSearchesDir());
		});
	}

	private static void deleteASearch () {
		Options<Search> existingSearchesList = new Options<>("Delete a Search", "Enter the number to delete:");
		FileUtils.getAllExistingSearches().forEach(existingSearchesList::add);
		existingSearchesList.setCancelListener(() -> {
			ConsoleUtils.clearConsole();
			System.out.println("Nothing was deleted.\n");
		});
		existingSearchesList.getUserChoice().ifPresent(searchItem -> {
			Search searchToDelete = searchItem.getPayload();
			String confirmationFromUser = KeyIn.inString("Are you sure you wish to permanently delete \"" + searchToDelete.getName() + "\" and all of it's results? Enter y/n: ");
			if (confirmationFromUser.equalsIgnoreCase("y")) {
				SearchController.delete(searchToDelete);
				ConsoleUtils.clearConsole();
				System.out.printf("\"%s\" was successfully deleted%n%n%n", searchToDelete.getName());
			}
			else {
				ConsoleUtils.clearConsole();
				System.out.println("Nothing was deleted.\n");
				deleteASearch();
			}
		});
	}

	private static void displayExistingSearches () {
		Options<Search> existingSearchesList = new Options<>("Existing Searches", null);
		FileUtils.getAllExistingSearches().forEach(existingSearchesList::add);
		existingSearchesList.displayToUser();
		KeyIn.inString("Press enter to go back");
		ConsoleUtils.clearConsole();
	}
}