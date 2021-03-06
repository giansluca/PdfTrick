package org.gmdev.pdftrick.ui;

import java.awt.*;
import java.awt.Taskbar;

import javax.swing.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.WaitPanel.WaitPanelMode;
import org.gmdev.pdftrick.ui.listeners.MainWindowListener;
import org.gmdev.pdftrick.ui.custom.GlassPane;
import org.gmdev.pdftrick.ui.panels.*;
import org.gmdev.pdftrick.ui.panels.Menu;
import org.gmdev.pdftrick.utils.*;
import net.miginfocom.swing.MigLayout;

import static org.gmdev.pdftrick.swingmanager.WaitPanel.WaitPanelMode.PAGE_LOADING_THUMBNAILS;
import static org.gmdev.pdftrick.utils.Constants.*;
import static org.gmdev.pdftrick.utils.SetupUtils.WIN_OS;

public class UserInterface extends JFrame {

	private final static PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	private final LeftPanel left;
    private final CenterPanel center;
    private final RightPanel right;
    private final BottomPanel bottom;
    private final GlassPane glassPanel;
    private final Menu menu;
    
	public UserInterface() {
		super();
		setLookAndFeel();
		setAppIcon();
        setTitle(APP_NAME);

        addWindowListener(new MainWindowListener());

		left = new LeftPanel();
		center = new CenterPanel();
		right = new RightPanel();
        bottom = new BottomPanel();
        glassPanel = new GlassPane();
        menu = new Menu();

        setJMenuBar(menu.getMenuBar());
        getRootPane().setGlassPane(glassPanel);
        setContentPane(contentPanelSetUp());
        pack();
	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void setAppIcon() {
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(PDFTRICK_ICO));
		if (imageIcon.getImageLoadStatus() != MediaTracker.COMPLETE)
			throw new IllegalStateException("Image icon non loaded");

		if (bag.getOs().equals(WIN_OS))
			super.setIconImage(imageIcon.getImage());
		else
			Taskbar.getTaskbar().setIconImage(imageIcon.getImage());
	}
	
	private JPanel contentPanelSetUp() {
		JPanel contentPane = new JPanel();
        contentPane.setLayout(new MigLayout());
        contentPane.setPreferredSize(new Dimension(1250, 800));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(left.getLeftScrollPanel(), "h 100%, w 20%, span 1 2");
        contentPane.add(center.getCenterScrollPanel(), "h 77%, w 68%");
        contentPane.add(right.getRightPanel(), "h 77%, w 12%, wrap");
        contentPane.add(bottom.getBottomPanel(), "h 23%, w 82%, span 2 1");
        return contentPane;
	}

	public void lockScreen(WaitPanelMode mode) {
		if (mode.equals(PAGE_LOADING_THUMBNAILS))
			right.getRightBottomPanel().add(
					glassPanel.getThumbSpinner(), "gaptop 10, center, wrap");
		else
			right.getRightBottomPanel().add(
					glassPanel.getExtractSpinner(), "gaptop 10, center, wrap");
		
		right.getRightBottomPanel().revalidate();
		right.getRightBottomPanel().repaint();
		glassPanel.setVisible(true);
	}

	public void unlockScreen() {
		// the spinner is the last component added in that panel, it is dynamically added whit the glass pane
		int spinnerIndex = right.getRightBottomPanel().getComponents().length - 1;
		right.getRightBottomPanel().remove(spinnerIndex);
		right.getRightBottomPanel().revalidate();
		right.getRightBottomPanel().repaint();
		glassPanel.setVisible(false);
	}
	
	public LeftPanel getLeft() {
		return left;
	}
	
	public CenterPanel getCenter() {
		return center;
	}

	public RightPanel getRight() {
		return right;
	}

	public BottomPanel getBottom() {
		return bottom;
	}
	
	public Menu getMenu() {
		return menu;
	}

}
