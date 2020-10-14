package org.gmdev.pdftrick.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.gmdev.pdftrick.factory.PdfTrickFactory;

public class ExecPool implements Runnable {
	
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	
	private final String imgPath;
	private final int numPages;
	private final int division;
	volatile boolean finished = false;
	
	public ExecPool(int totPages, int division, String imgPath) {
		this.imgPath = imgPath;
		this.numPages = totPages;
		this.division = division;
	}
	
	public void stop() {
		finished = true;
	}
	
	@Override
	public void run() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		factory.gettContainer().setExecutor(executor);
		
		int i = division + 1;
		
		while (i <= numPages && !finished) {
			Runnable worker = new PageThumb(imgPath, i);
			executor.execute(worker);
			i++;
		}
		
		executor.shutdown();
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}

}