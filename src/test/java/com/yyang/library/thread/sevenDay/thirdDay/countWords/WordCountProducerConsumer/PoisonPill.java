package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountProducerConsumer;

 class PoisonPill implements Page {

	public String getTitle() {
		throw new UnsupportedOperationException();
	}

	public String getText() {
		throw new UnsupportedOperationException();
	}

	public boolean isPoisonPill() {
		return true;
	}

}
