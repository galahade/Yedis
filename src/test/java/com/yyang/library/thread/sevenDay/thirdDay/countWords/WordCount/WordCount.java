package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCount;

import java.util.HashMap;

public class WordCount {

	private static final HashMap<String, Integer> counts = new HashMap<>();
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Iterable<Page> pages = new Pages(1, "/Users/young_mac/Documents/workbench/enwiki.xml");
		for(Page page : pages) {
			Iterable<String> words = new Words(page.getText());
			for(String word :words) {
				countWord(word);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Elapsed time: " +(end - start) + "ms");
	}
	
	
	private static void countWord(String word) {
		Integer currentCount = counts.get(word);
		if(currentCount == null) {
			counts.put(word, 1);
			System.out.println("a new word \"" +word+"\" add to map.");
		} else {
			counts.put(word, ++currentCount);
			System.out.println("The word " +word+" count to " + currentCount);
		}
	}
}
