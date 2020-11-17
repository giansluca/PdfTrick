package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.thread.ImgExtraction;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.SetupUtils;

public class GetImgAction extends AbstractAction  {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	/**
	 * Called from the GET IMG button, extract images
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		final Properties messages = BAG.getMessagesProps();
		final Container contentPanel = BAG.getUserInterface().getContentPane();
		
		if (BAG.getThreadContainer().getImgExtractionThread() != null &&
				BAG.getThreadContainer().getImgExtractionThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("tmsg_02"));
			return;
		}
		if (BAG.getThreadContainer().getOpenFileChooserThread() != null &&
				BAG.getThreadContainer().getOpenFileChooserThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (BAG.getThreadContainer().getDragAnDropFileChooserThread() != null &&
				BAG.getThreadContainer().getDragAnDropFileChooserThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (BAG.getThreadContainer().getShowThumbsThread() != null &&
				BAG.getThreadContainer().getShowThumbsThread().isAlive()) {

			ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
			return;
		}
		
		boolean extract = true;
		File resultFile = BAG.getPdfFilePath().toFile();
		if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
			CustomFileChooser choosefolderToSave = new CustomFileChooser();
			choosefolderToSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			choosefolderToSave.setDialogTitle(Constants.JFC_EXTRACT_TITLE);
			
			String extractionFolder = null;
			Set<String> keys = BAG.getSelectedImages().keySet();
			Set<String> kk = BAG.getInlineSelectedImages().keySet();
			
			if (keys.size() > 0 || kk.size() > 0) {
				if (choosefolderToSave.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) { 
					if (SetupUtils.isWindows()) {
						extractionFolder = choosefolderToSave.getSelectedFile().getAbsolutePath();
					} else if (SetupUtils.isMac()) {
						extractionFolder = choosefolderToSave.getCurrentDirectory().getAbsolutePath();
					}
					BAG.setExtractionFolderPath(Path.of(extractionFolder));
				} else {
					extract = false;
				}
			} else {
				Messages.append("INFO", messages.getProperty("tmsg_03"));
				extract = false;
			}
		} else {
			Messages.append("INFO", messages.getProperty("tmsg_04"));
			extract = false;
		}
		if (extract) {
			ImgExtraction imgExtraction = new ImgExtraction();
			BAG.getThreadContainer().setImgExtraction(imgExtraction);
			
			Thread imgExtractionThread = new Thread(imgExtraction, "imgExtractionThread");
			BAG.getThreadContainer().setImgExtractionThread(imgExtractionThread);
			
			imgExtractionThread.start();
		}
	}


}
