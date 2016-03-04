package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountSynchronizedHashMap;

import java.text.BreakIterator;
import java.util.Iterator;

class Words implements Iterable<String> {

	private final String text;
	
	public Words(String text) {
		this.text = text;
	}
	
	private class WordIterator implements Iterator<String> {
		
		private BreakIterator wordBoundary;
		private int start;
		private int end;
		
		public WordIterator() {
			wordBoundary = BreakIterator.getWordInstance();
			wordBoundary.setText(text);
			start = wordBoundary.first();
			end = wordBoundary.next();
		}

		@Override
		public boolean hasNext() {
			return end != BreakIterator.DONE;
		}

		@Override
		public String next() {
			String s = text.substring(start, end);
			start = end;
			end = wordBoundary.next();
			return s;
		}
		
	    public void remove() { throw new UnsupportedOperationException(); }
		
	}
	
	@Override
	public Iterator<String> iterator() {
		return new WordIterator();
	}

}
