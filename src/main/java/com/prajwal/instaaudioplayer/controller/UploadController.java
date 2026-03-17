package com.prajwal.instaaudioplayer.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.prajwal.instaaudioplayer.service.OCRService;

import java.io.File;

@RestController
public class UploadController {

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) {

        try {

            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File folder = new File(uploadDir);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filename = file.getOriginalFilename();

            File dest = new File(uploadDir + filename);

            file.transferTo(dest);

            OCRService ocrService = new OCRService();

            String extractedText = ocrService.readText(dest.getAbsolutePath());

            return "Detected Text:\n\n" + extractedText;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }
}