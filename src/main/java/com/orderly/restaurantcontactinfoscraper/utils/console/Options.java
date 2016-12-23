package com.orderly.restaurantcontactinfoscraper.utils.console;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/**
 * Created by Joshua King on 12/19/16.
 */
public class Options<T> {
	private           ArrayList<Item<T>> options;
	private           String             prompt;
	@Nullable private String             subtitle;
	private           Runnable           cancelListener;

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

	public Optional<Item<T>> getUserChoice () {
		displayToUser();

		int chosenIndex;
		while (true) {
			if (options.size() > 0) {
				chosenIndex = KeyIn.inInt("Select option: ");
				if (cancelListener != null && chosenIndex == options.size() + 1) {
					cancelListener.run();
					return Optional.empty();
				}
				if (chosenIndex <= options.size() && chosenIndex > 0) { break; } else {
					KeyIn.printPrompt("Please choose an option between 1 and " + options.size() + " (inclusive).\n");
				}
			} else {
				KeyIn.inString("Press enter to continue: ");
				return Optional.empty();
			}
		}

		return Optional.ofNullable(options.get(chosenIndex - 1));
	}

	public void displayToUser () {
		for (int i = 0; i < options.size(); i++) { options.get(i).setNumber(i + 1); }

		int lengthOfSubtitle = subtitle == null ? 0 : subtitle.length();
		String cancel = String.format("%d. Cancel", options.size() + 1);

		int longestPromptOrOptionLength = options.stream().map(Item::toString).map(String::length).max(Comparator.naturalOrder()).orElse(0);
		longestPromptOrOptionLength = Integer.max(longestPromptOrOptionLength, prompt.length());
		longestPromptOrOptionLength = Integer.max(longestPromptOrOptionLength, lengthOfSubtitle);
		longestPromptOrOptionLength = Integer.max(longestPromptOrOptionLength, cancel.length());
		int width = longestPromptOrOptionLength + 8;

		ConsoleUtils.printBox(prompt, 3, width);

		if (options.size() > 0) {
			Optional.ofNullable(subtitle).ifPresent(subtitle -> ConsoleUtils.printStringWithPadding(subtitle, 1, width));
			options.stream().map(Item::toString).forEach(s -> ConsoleUtils.printStringWithPadding(s, 3, width));
			if (cancelListener != null) { ConsoleUtils.printStringWithPadding(cancel, 3, width); }
		}

		ConsoleUtils.printDivider(width, "=");
	}

	public Item<T> add (T t) {
		return add(new Item<>(t));
	}

	public Item<T> add (Item<T> item) {
		options.add(item);
		return item;
	}

	public void setCancelListener (Runnable cancelListener) {
		this.cancelListener = cancelListener;
	}

	public static class Item<T> {
		private final T   t;
		private       int number;

		public Item (T t) {
			this.t = t;
		}

		@Override public String toString () {
			return String.format("%d. %s", number, t.toString());
		}

		public String getName () {
			return t.toString();
		}

		public T getPayload () {
			return t;
		}

		public int getNumber () {
			return number;
		}

		public void setNumber (int number) {
			this.number = number;
		}
	}
}
