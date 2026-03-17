package com.prajwal.instaaudioplayer.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;

public class OCRService {

    public String readText(String imagePath) {

        try {

            ITesseract tesseract = new Tesseract();

            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");

            String text = tesseract.doOCR(new File(imagePath));

            return text;

        } catch (Exception e) {
            return "OCR Error: " + e.getMessage();
        }

    }

}