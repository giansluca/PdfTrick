package org.gmdev.pdftrick.tasks;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;

public class PageThumb implements Runnable {
	
	private static final Logger logger = Logger.getLogger(PageThumb.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final String imgPath;
	private final int numPage;
	
	public PageThumb(String imgPath, int numPage) {
		this.imgPath = imgPath;
		this.numPage = numPage;
	}
	
	@Override
	public void run() {
		execute();
	}
	
	/**
	 * Render pdf cover calling native lib
	 */
	public void execute() {
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		
		try {
			nativeManager.renderPdfPageThumbnail(
					BAG.getPdfFilePath().toString(), imgPath, numPage, Constants.ZOOM_THUMBNAIL);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Exception", e);
		}
	}
	
	
}