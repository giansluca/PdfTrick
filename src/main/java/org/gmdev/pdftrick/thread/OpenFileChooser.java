package org.gmdev.pdftrick.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.gmdev.pdftrick.engine.CheckFiles;
import org.gmdev.pdftrick.engine.MergeFiles;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;

public class OpenFileChooser implements Runnable {
	
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	private final File[] files;
	volatile boolean finished = false;
	
	public OpenFileChooser(File[] files) {
		this.files = files;
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		execute();
	}
	
	/**
	 * Called after choosing pdf file upload  
	 */
	public void execute() {
		final Properties messages = factory.getMessages();
		final JTextField currentPageField = factory.getUserInterface().getRight().getCurrentPageField();
		final JTextField numImgSelectedField = factory.getUserInterface().getRight().getNumImgSelectedField();
		final ArrayList<File> filesVett = factory.getPdfFilesArray();
		
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Utils.cleanLeftPanel();
				Utils.cleanCenterPanel();
				Messages.cleanTextArea();
				currentPageField.setText("");
				numImgSelectedField.setText("");
				Utils.startWaitIconLoadPdf();
			}
		});
        		
        // clean areatext fileVett resultFile imageSelected imageselected in case of reopen jfilechooser 
        factory.setSelected("");
        factory.setFolderToSave("");
        Utils.cleanFilevett();
        Utils.cleanImageSelectedHashMap();
        Utils.cleanInlineImgSelectedHashMap();
        Utils.cleanRotationFromPagesHashMap();
        		
        Utils.deleteImgFolderAnDFile();
        Utils.deleteResultFile();
     	System.gc();
     	
		for (int i = 0; i < files.length; i++) {
			File item = files[i];
			if (!item.isDirectory()) {
				filesVett.add(item);
			}
		}
        
        // call check class control files after selection
        boolean fileCheck = false;
        CheckFiles checkFiles = new CheckFiles();
        if (filesVett.size() > 0) {
        	fileCheck = checkFiles.check();
        	if (!fileCheck) {
        		// in case of check failed i clean panel left and center, other stuff 
        		// (vector, hasmap, resultpdf was cleaned on approve open filechooser)
        		Messages.append("WARNING", messages.getProperty("tmsg_11"));
        		
        		SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						Utils.cleanLeftPanel();
						Utils.cleanCenterPanel();
					}
				});
        	} else {
        		// merge pdf selection after check
        		MergeFiles engine = new MergeFiles();
        		File resultFile = engine.mergePdf(filesVett, factory.getPdfFilePath());
        		
        		if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
        			Messages.append("INFO", messages.getProperty("tmsg_12"));
        			PdfRenderLeft render = new PdfRenderLeft();
        			render.pdfRender();
        		} else {
        			Messages.append("WARNING", messages.getProperty("tmsg_13"));
        		}
        	}
        } 
        
        finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}
	

}
