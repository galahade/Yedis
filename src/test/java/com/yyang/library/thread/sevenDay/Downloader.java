package com.yyang.library.thread.sevenDay;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Downloader extends Thread {

	private InputStream in;
	private OutputStream out;
	private List<ProgressListener> listeners;
	
	public Downloader(URL url, String outputFilename) throws IOException {
		in = url.openConnection().getInputStream();
		out = new FileOutputStream(outputFilename);
		listeners = new ArrayList<>();
	}
	
	public synchronized void addListener(ProgressListener listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeListener(ProgressListener listener) {
		listeners.remove(listener);
	}
	
	private synchronized void updateProgress(int n) {
		for (ProgressListener listener : listeners) {
			listener.onProgress(n);
		}
	}
	
	public void run() {
		int n = 0, total = 0;
		byte[] buffer = new byte[1024];
		try {
			while((n = in.read(buffer)) != -1) {
				out.write(buffer, 1, n);
				total += n;
				updateProgress(total);
			}
			out.flush();
		} catch (IOException e) {}
	}
	
	static class ProgressListener {
		
		public void onProgress(int n) {
			System.out.println("Process " + n);
		}
		
	}
}
