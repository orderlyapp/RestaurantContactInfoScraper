package com.orderly.restaurantcontactinfoscraper.utils.console;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Joshua King on 12/19/16.
 */
public class Options {
	private ArrayList<Item> options;
	private String          prompt;

	public Options (@Nonnull String prompt) {
		this.prompt = prompt;
		options = new ArrayList<>();
	}

	public Item getUserChoice () {
		if (options.size() < 2) { throw new IllegalStateException("Please add at least two options to this."); }

		for (int i = 0; i < options.size(); i++) { options.get(i).setIndex(i); }

		int longestPromptOrOptionLength = Integer.max(options.stream().map(Item::toString).map(String::length).max(Comparator.naturalOrder()).orElse(0), prompt.length());
		int width = longestPromptOrOptionLength + 8;

		ConsoleUtils.printBox(prompt, 3);
		ConsoleUtils.printStringWithPadding("Options:", 1, width);

		options.stream().map(Item::toString).forEach(s -> ConsoleUtils.printStringWithPadding(s, 3, width));

		ConsoleUtils.printDivider(width, "=");

		int chosenIndex;
		while (true) {
			chosenIndex = KeyIn.inInt("Select option: ");
			if (chosenIndex < options.size()) { break; } else {
				KeyIn.printPrompt("Please choose an option between 0 and " + options.size() + " (inclusive).\n");
			}
		}

		return options.get(chosenIndex);
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
