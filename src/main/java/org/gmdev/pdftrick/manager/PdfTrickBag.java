package org.gmdev.pdftrick.manager;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.ui.UserInterface;

import static org.gmdev.pdftrick.utils.Constants.*;

public enum PdfTrickBag {
	INSTANCE;

	String os;
	Path homeFolderPath;
	Path nativeLibraryPath;
	Path pdfFilePath;
	Path thumbnailsFolderPath;
	int numberOfPages;
	ArrayList<File> pdfFilesArray;
	String selected;
	Path extractionFolder;
	HashMap<Integer, String> rotationFromPages;
	HashMap<String, String> namePwd;
	HashMap<String, RenderedImageAttributes> imageSelected;
	HashMap<String, RenderedImageAttributes> inlineImgSelected;
	ThreadContainer threadContainer;
	Properties messages;
	NativeObjectManager nativeObjectManager;
	UserInterface userInterface;

	@CanIgnoreReturnValue
	public static PdfTrickBag getInstance() {
		return INSTANCE;
	}

	protected void init(String os, Path homeFolderPath, Path nativeLibraryPath) {
		this.os = os;
		this.homeFolderPath = homeFolderPath;
		this.nativeLibraryPath = nativeLibraryPath;
		pdfFilePath = Path.of(homeFolderPath + File.separator + PDF_FILE_NAME);
		thumbnailsFolderPath = Path.of(homeFolderPath + File.separator + PAGES_THUMBNAIL_FOLDER);
		numberOfPages = 0;
		pdfFilesArray = new ArrayList<>();
		selected = "";
		extractionFolder = null;
		rotationFromPages = new HashMap<>();
		namePwd = new HashMap<>();
		imageSelected = new HashMap<>();
		inlineImgSelected = new HashMap<>();
		threadContainer = new ThreadContainer();
	}

	public String getOs() {
		return os;
	}

	public Path getHomeFolderPath() {
		return homeFolderPath;
	}

	public Path getNativeLibraryPath() {
		return nativeLibraryPath;
	}

	public Path getPdfFilePath() {
		return pdfFilePath;
	}

	public Path getThumbnailsFolderPath() {
		return thumbnailsFolderPath;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public ArrayList<File> getPdfFilesArray() {
		return pdfFilesArray;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public Path getExtractionFolder() {
		return extractionFolder;
	}

	public void setExtractionFolder(Path extractionFolder) {
		this.extractionFolder = extractionFolder;
	}

	public HashMap<Integer, String> getRotationFromPages() {
		return rotationFromPages;
	}

	public HashMap<String, String> getNamePwd() {
		return namePwd;
	}

	public HashMap<String, RenderedImageAttributes> getImageSelected() {
		return imageSelected;
	}

	public HashMap<String, RenderedImageAttributes> getInlineImgSelected() {
		return inlineImgSelected;
	}

	public ThreadContainer getThreadContainer() {
		return threadContainer;
	}

	public Properties getMessages() { return messages; }

	protected void setMessages(Properties messages) {
		this.messages = messages;
	}

	public NativeObjectManager getNativeObjectManager() {
		return nativeObjectManager;
	}

	protected void setNativeObjectManager(NativeObjectManager nativeObjectManager) {
		this.nativeObjectManager = nativeObjectManager;
	}

	public UserInterface getUserInterface() {
		return userInterface;
	}

	protected void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}


}
