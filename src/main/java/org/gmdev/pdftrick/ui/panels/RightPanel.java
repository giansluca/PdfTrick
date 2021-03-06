package org.gmdev.pdftrick.ui.panels;

import javax.swing.*;

import org.gmdev.pdftrick.ui.actions.*;

import net.miginfocom.swing.MigLayout;
import org.gmdev.pdftrick.utils.FileLoader;

import static org.gmdev.pdftrick.utils.Constants.*;

public class RightPanel {

	private final JPanel rightPanel;
    private final JPanel rightTopPanel;
    private final JPanel rightBottomPanel;
    private final JButton getImagesButton;
    private final JButton cancelButton;
    private final JButton cleanButton;
    private final JTextField currentPageField;
    private final JTextField selectedImagesField;
	
    public RightPanel() {
    	rightTopPanel = new JPanel(
    			new MigLayout("insets 3 3 3 0, right, ttb", "", ""));
		rightBottomPanel = new JPanel(
				new MigLayout("insets 3 3 3 0, right, btt", "", ""));

		ImageIcon getImagesIcon = new ImageIcon(FileLoader.loadFileAsUrl(GET_IMG_ICO));
	    getImagesButton = new JButton("Get Img");
	    getImagesButton.setFocusable(false);
	    getImagesButton.setAction(new ImagesExtractionAction());
	    getImagesButton.setText("GET IMG");
		getImagesButton.setIcon(getImagesIcon);
	   
		ImageIcon cancelIcon = new ImageIcon(FileLoader.loadFileAsUrl(CANCEL_ICO));
	    cancelButton = new JButton("Cancel");
	    cancelButton.setFocusable(false);
	    cancelButton.setAction(new CancelAction());
	    cancelButton.setText("CANCEL");
		cancelButton.setIcon(cancelIcon);
	    
		ImageIcon cleanSelectionIcon = new ImageIcon(FileLoader.loadFileAsUrl(CLEAN_SEL_ICO));
	    cleanButton = new JButton("Clean");
	    cleanButton.setFocusable(false);
	    cleanButton.setAction(new CleanSelectionAction());
	    cleanButton.setText("CLEAN");
		cleanButton.setIcon(cleanSelectionIcon);

	    rightTopPanel.add(getImagesButton, "h 25:30:35, w 80:80%:150, gaptop 10, wrap");
	    rightTopPanel.add(cancelButton, "h 25:30:35, w 80:80%:150, gaptop 5, wrap");
	    rightTopPanel.add(cleanButton, "h 25:30:35, w 80:80%:150, gaptop 5, wrap");

	    currentPageField = new JTextField();
	    currentPageField.setEditable(false);
	    currentPageField.setFocusable(false);

		selectedImagesField = new JTextField();
		selectedImagesField.setEditable(false);
		selectedImagesField.setFocusable(false);

	    rightBottomPanel.add(currentPageField, "h 15:25:28, w 60:80%, gaptop 10, wrap");
	    rightBottomPanel.add(selectedImagesField, "h 15:25:28, w 60:80%, wrap");
	   
	    rightPanel = new JPanel(new MigLayout("insets 0 0 0 0, flowy, fill"));
	    rightPanel.add(rightTopPanel, "grow");
	    rightPanel.add(rightBottomPanel, "grow");
	}
    
    public JPanel getRightPanel() {
		return rightPanel;
	}

	public JPanel getRightTopPanel() {
		return rightTopPanel;
	}
	
	public JPanel getRightBottomPanel() {
		return rightBottomPanel;
	}

	public JButton getGetImagesButton() {
		return getImagesButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getCleanButton() {
		return cleanButton;
	}

	public JTextField getCurrentPageField() {
		return currentPageField;
	}

	public JTextField getSelectedImagesField() {
		return selectedImagesField;
	}
}
