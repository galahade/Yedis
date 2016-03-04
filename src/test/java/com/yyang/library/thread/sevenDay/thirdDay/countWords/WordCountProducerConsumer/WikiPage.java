package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountProducerConsumer;

 class WikiPage implements Page {

	private String title;
	private String text;
	
	public WikiPage(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}
}
