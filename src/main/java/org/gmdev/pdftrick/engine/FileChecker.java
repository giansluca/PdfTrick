package org.gmdev.pdftrick.engine;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.*;

import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.*;

import static org.gmdev.pdftrick.engine.PasswordChecker.Result.OK;

public class FileChecker {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    private static final int MAX_SIZE_MB = 256;
    private static final String PDF_MAGIC_NUMBER = "%PDF";

    private boolean userProtection = false;
    private boolean ownerProtection = false;
    private final File uploadedFile;
    private final Properties messages;

    public FileChecker(File uploadedFile) {
        this.uploadedFile = uploadedFile;
        this.messages = bag.getMessagesProps();
    }

    public boolean isValid() {
        if (!isValidFileType()) return false;
        if (!isValidFileSize()) return false;
        if (!canAccess()) return false;
        return hasImages();
    }

    private boolean isValidFileType() {
        String fileName = uploadedFile.getName();
        String content = this.readFile();
        if (content.isEmpty() || !content.substring(0, 4).equalsIgnoreCase(PDF_MAGIC_NUMBER)) {
            Messages.append("WARNING", MessageFormat.format(messages.getProperty("d_msg_04"), fileName));
            return false;
        }

        return true;
    }

    private String readFile() {
        try (FileReader in = new FileReader(uploadedFile);
            BufferedReader reader = new BufferedReader(in)) {
            return reader.readLine();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isValidFileSize() {
        long fileSize = uploadedFile.length();
        long fileSizeKB = fileSize / 1024;
        long fileSizeMB = fileSizeKB / 1024;
        if (fileSizeMB > MAX_SIZE_MB) {
            Messages.append("WARNING", messages.getProperty("t_msg_20"));
            return false;
        }

        return true;
    }

    private boolean canAccess() {
        verifyProtection();
        if (!userProtection && !ownerProtection) return true;

        PasswordChecker passwordChecker = new PasswordChecker(uploadedFile);
        return passwordChecker.check() == OK;
    }

    private void verifyProtection() {
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(uploadedFile.getPath());
            if (pdfReader.isEncrypted()) ownerProtection = true;
        } catch (BadPasswordException e) {
            userProtection = true;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (pdfReader != null) pdfReader.close();
        }
    }

    private boolean hasImages() {
        PdfReader pdfReader = null;
        try {
            pdfReader = bag.getPdfPassword() != null
                    ? new PdfReader(uploadedFile.getPath(), bag.getPdfPassword().getBytes())
                    : new PdfReader(uploadedFile.getPath());

            for (int i = 0; i < pdfReader.getXrefSize(); i++) {
                PdfObject pdfObject = pdfReader.getPdfObject(i);
                if (pdfObject == null || !pdfObject.isStream()) continue;

                PdfStream pdfStream = (PdfStream) pdfObject;
                PdfObject pdfSubtype = pdfStream.get(PdfName.SUBTYPE);

                if (pdfSubtype != null &&
                        pdfSubtype.toString().equals(PdfName.IMAGE.toString())) return true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (pdfReader != null) pdfReader.close();
        }

        Messages.append("WARNING", messages.getProperty("t_msg_21"));
        return false;
    }


}
