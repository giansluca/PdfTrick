package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DefaultReaderPdf implements PdfImageReader {

    private final PdfImageXObject image;
    private final int ref;

    public DefaultReaderPdf(PdfImageXObject image, int ref) {
        this.image = image;
        this.ref = ref;
    }

    @Override
    public BufferedImage readImage() {
        try {
            return image.getBufferedImage();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}