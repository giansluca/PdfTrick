package org.gmdev.pdftrick.ui.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.render.ImageAction;
import org.gmdev.pdftrick.utils.PdfTrickMessages;
import org.gmdev.pdftrick.utils.PdfTrickUtils;

public class CleanSelectionAction extends AbstractAction {
	
	private static final long serialVersionUID = 1827086419763590961L;
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	
	public CleanSelectionAction() {
	}
	
	/**
	 * Called from the CLEAN SELECTION button, clean image selection
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		final Properties messages = factory.getMessages();
		final JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
		final JTextField numImgSelectedField = factory.getUserInterface().getRight().getNumImgSelectedField();
		final HashMap<String, RenderedImageAttributes> inlineImgSelected = factory.getInlineImgSelected();
		
		if (factory.gettContainer().getImgExtractionThread() != null && factory.gettContainer().getImgExtractionThread().isAlive()) {
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_02"));
			return;
		}
		
		if (factory.gettContainer().getImgThumbThread() !=null && factory.gettContainer().getImgThumbThread().isAlive()) {
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_23"));
			return;
		}
		
		if (factory.gettContainer().getOpenFileChooserThread() != null && factory.gettContainer().getOpenFileChooserThread().isAlive()) {
			return;
		}
		
		if (factory.gettContainer().getDragAnDropFileChooserThread() != null && factory.gettContainer().getDragAnDropFileChooserThread().isAlive()) {
			return;
		}
		
		if (factory.gettContainer().getShowThumbsThread() != null && factory.gettContainer().getShowThumbsThread().isAlive()) {
			return;
		}
		
		if (factory.getImageSelected().size() == 0 && inlineImgSelected.size() == 0) {
			PdfTrickMessages.append("INFO", messages.getProperty("tmsg_24"));
		} else {
			Border borderGray = BorderFactory.createLineBorder(Color.gray);
			Component[] comps =  centerPanel.getComponents();
			Component component = null;
		
			for (int z = 0; z < comps.length; z++) {
				component = comps[z];
				
				if (component instanceof JLabel) {
					JLabel picLabel = (JLabel) comps[z];
					String name = ""+picLabel.getName();
					
					if (!name.equalsIgnoreCase("NoPicsImg")) {
						picLabel.setBorder(borderGray);
						picLabel.setOpaque(true);
						picLabel.setBackground(Color.WHITE);
						MouseListener[] mls = (picLabel.getListeners(MouseListener.class));
						
						if (mls.length > 0) {
							ImageAction act = (ImageAction) mls[0];
							act.setSelected(false);
						}
					}	
				}
			}
			
			PdfTrickUtils.cleanImageSelectedHashMap();
			PdfTrickUtils.cleanInlineImgSelectedHashMap();
			numImgSelectedField.setText("");
		}
		
	}
	
}