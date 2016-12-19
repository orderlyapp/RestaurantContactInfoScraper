package com.orderly.restaurantcontactinfoscraper.utils.console;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/**
 * Created by Joshua King on 12/19/16.
 */
public class Options {
	private           ArrayList<Item> options;
	private           String          prompt;
	@Nullable private String          subtitle;

	public Options (@Nonnull String prompt) {
		this.prompt = prompt;
		subtitle = "Options:";
		options = new ArrayList<>();
	}

	public Options (@Nonnull String prompt, @Nullable String subtitle) {
		this.prompt = prompt;
		this.subtitle = subtitle;
		options = new ArrayList<>();
	}

	public Item getUserChoice () {
		displayToUser();

		int chosenIndex;
		while (true) {
			chosenIndex = KeyIn.inInt("Select option: ");
			if (chosenIndex <= options.size() && chosenIndex > 0) { break; } else {
				KeyIn.printPrompt("Please choose an option between 1 and " + options.size() + " (inclusive).\n");
			}
		}

		return options.get(chosenIndex - 1);
	}

	public void displayToUser () {
		for (int i = 0; i < options.size(); i++) { options.get(i).setIndex(i + 1); }

		int longestPromptOrOptionLength = Integer.max(options.stream().map(Item::toString).map(String::length).max(Comparator.naturalOrder()).orElse(0), prompt.length());
		int width = longestPromptOrOptionLength + 8;

		ConsoleUtils.printBox(prompt, 3, width);
		Optional.ofNullable(subtitle).ifPresent(subtitle -> ConsoleUtils.printStringWithPadding(subtitle, 1, width));

		options.stream().map(Item::toString).forEach(s -> ConsoleUtils.printStringWithPadding(s, 3, width));

		ConsoleUtils.printDivider(width, "=");
	}

	public void add (String name) {
		add(new Item(name));
	}

	public void add (Item item) {
		options.add(item);
	}

	public static class Item {
		private final String name;
		private       int    index;

		public Item (String name) {
			this.name = name;
		}

		@Override public String toString () {
			return String.format("%d. %s", index, name);
		}

		public String getName () {
			return name;
		}

		public int getIndex () {
			return index;
		}

		public void setIndex (int index) {
			this.index = index;
		}
	}
}
