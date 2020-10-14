package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.thread.ImgExtraction;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.PdfTrickMessages;
import org.gmdev.pdftrick.utils.PdfTrickPreInitUtils;

public class GetImgAction extends AbstractAction  {
	
	private static final long serialVersionUID = 5066094189763059556L;
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	
	public GetImgAction() {
	}
	
	/**
	 * Called from the GET IMG button, extract images
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = factory.getMessages();
		final Container contentPanel = factory.getUserInterface().getContentPane();
		
		if (factory.gettContainer().getImgExtractionThread() != null && factory.gettContainer().getImgExtractionThread().isAlive()) {
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_02"));
			return;
		}
		if (factory.gettContainer().getOpenFileChooserThread() != null && factory.gettContainer().getOpenFileChooserThread().isAlive()) {
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (factory.gettContainer().getDragAnDropFileChooserThread() != null && factory.gettContainer().getDragAnDropFileChooserThread().isAlive()) {
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (factory.gettContainer().getShowThumbsThread() != null && factory.gettContainer().getShowThumbsThread().isAlive()) {
			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Consts.WARNING_ICO));
			PdfTrickMessages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
			return;
		}
		
		boolean extract = true;
		File resultFile = new File(factory.getResultFile());
		if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
			CustomFileChooser choosefolderToSave = new CustomFileChooser();
			choosefolderToSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			choosefolderToSave.setDialogTitle(Consts.JFCEXTRACTTITLE);
			
			String selectedFolderToSave = "";
			Set<String> keys = factory.getImageSelected().keySet();
			Set<String> kk = factory.getInlineImgSelected().keySet();
			
			if (keys.size() > 0 || kk.size() > 0) {
				if (choosefolderToSave.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) { 
					if (PdfTrickPreInitUtils.isWindows()) {
						selectedFolderToSave = choosefolderToSave.getSelectedFile().getAbsolutePath();
					} else if (PdfTrickPreInitUtils.isMac()) {
						selectedFolderToSave = choosefolderToSave.getCurrentDirectory().getAbsolutePath();
					}
					factory.setFolderToSave(selectedFolderToSave);
				} else {
					extract = false;
				}
			} else {
				PdfTrickMessages.append("INFO", messages.getProperty("tmsg_03"));
				extract = false;
			}
		} else {
			PdfTrickMessages.append("INFO", messages.getProperty("tmsg_04"));
			extract = false;
		}
		if (extract) {
			ImgExtraction imgExtraction = new ImgExtraction();
			factory.gettContainer().setImgExtraction(imgExtraction);
			
			Thread imgExtractionThread = new Thread(imgExtraction, "imgExtractionThread");
			factory.gettContainer().setImgExtractionThread(imgExtractionThread);
			
			imgExtractionThread.start();
		}
	}


}