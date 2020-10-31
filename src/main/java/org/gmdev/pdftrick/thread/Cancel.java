package org.gmdev.pdftrick.thread;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;

public class Cancel implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Cancel.class);
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	volatile boolean finished = false;
	
	public Cancel() {
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		try {
			execute();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	
	/**
	 * Clean the interface and all objects
	 */
	public void execute () throws InterruptedException {
		JTextField currentPageField = factory.getUserInterface().getRight().getCurrentPageField();
		JTextField numImgSelectedField = factory.getUserInterface().getRight().getNumImgSelectedField();
		
		if (factory.gettContainer().getOpenFileChooser() != null) {
			while (!factory.gettContainer().getOpenFileChooser().isFinished()) {
				// wait thread stop
			}
			
			if (factory.gettContainer().getOpenFileChooserThread() !=null) {
				factory.gettContainer().getOpenFileChooserThread().join();
				
				while (factory.gettContainer().getOpenFileChooserThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.gettContainer().getDragAnDropFileChooser() != null) {
			while (!factory.gettContainer().getDragAnDropFileChooser().isFinished()) {
				// wait thread stop
			}
			
			if (factory.gettContainer().getDragAnDropFileChooserThread() != null) {
				factory.gettContainer().getDragAnDropFileChooserThread().join();
				while (factory.gettContainer().getDragAnDropFileChooserThread().isAlive()) {
				// wait thread stop
				}
			}
		}
		
		if (factory.gettContainer().getDivisionThumbs() != null && !factory.gettContainer().getDivisionThumbs().isFinished()) {
			factory.gettContainer().getDivisionThumbs().stop();
			while (!factory.gettContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			if (factory.gettContainer().getDivisionThumbsThread() != null) {
				factory.gettContainer().getDivisionThumbsThread().join();
				while (factory.gettContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.gettContainer().getShowThumbs() != null && !factory.gettContainer().getShowThumbs().isFinished()) {
			factory.gettContainer().getShowThumbs().stop();
			while (!factory.gettContainer().getShowThumbs().isFinished()) {
				// wait thread stop
			}
			if (factory.gettContainer().getShowThumbsThread() != null) {
				factory.gettContainer().getShowThumbsThread().join();
				while (factory.gettContainer().getShowThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.gettContainer().getExecPool() != null && !factory.gettContainer().getExecPool().isFinished()) {
			factory.gettContainer().getExecPool().stop();
			if (factory.gettContainer().getExecPoolThread() != null) {
				factory.gettContainer().getExecPoolThread().join();
				while (factory.gettContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (factory.gettContainer().getExecutor() != null) {
			factory.gettContainer().getExecutor().shutdownNow();
			while (!factory.gettContainer().getExecutor().isTerminated()) {
				//wait stop all threadPool task
			}
		}
		
		try {
			SwingUtilities.invokeAndWait(() -> {
				Utils.cleanLeftPanel();
				Utils.cleanCenterPanel();
				Messages.cleanTextArea();
				currentPageField.setText("");
				numImgSelectedField.setText("");
			});
		} catch (InterruptedException e) {
			logger.error("Exception", e);
		} catch (InvocationTargetException e) {
			logger.error("Exception", e);
		}

		Utils.cleanImageSelectedHashMap();
		Utils.cleanInlineImgSelectedHashMap();
		Utils.cleanRotationFromPagesHashMap();
		factory.setSelected("");
		factory.setFolderToSave("");
		Utils.cleanFilevett();
		
		Utils.deleteResultFile();
		Utils.deleteImgFolderAnDFile();
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}

}
