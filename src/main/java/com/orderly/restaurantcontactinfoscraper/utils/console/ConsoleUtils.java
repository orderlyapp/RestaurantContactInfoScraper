package com.orderly.restaurantcontactinfoscraper.utils.console;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Joshua King on 12/19/16.
 */
public class ConsoleUtils {
	public static void printBox (@Nonnull String prompt, int padding) {
		printBox(prompt, padding, 0);
	}

	public static void printBox (@Nonnull String prompt, int padding, int minWidth) {
		String[] lines = prompt.split("\n");
		int width = Integer.max(minWidth, Arrays.stream(lines).map(String::length).max(Comparator.naturalOrder()).orElse(0) + 2 + padding + padding);
		ConsoleUtils.printDivider(width, "=");
		Arrays.stream(lines).forEach(line -> ConsoleUtils.printStringWithPadding(line, padding, width));
		ConsoleUtils.printDivider(width, "=");
	}

	public static void printDivider (int width, String chr) {
		for (int i = 0; i < width; i++) { System.out.print(chr); }
		System.out.println();
	}

	public static void printStringWithPadding (String prompt, int padding, int width) {
		System.out.print("|");
		for (int i = 0; i < padding; i++) { System.out.print(" "); }
		KeyIn.printPrompt(prompt);
		for (int i = 0; i < width - prompt.length() - padding - 2; i++) { System.out.print(" "); }
		System.out.print("|\n");
	}

	public static void clearConsole () {
		newLine(100);
	}

	public static void newLine (int lines) {
		for (int i = 0; i < lines; i++) { System.out.println(); }
	}

}
