package com.electronics.store.service.impl;


import com.electronics.store.exceptions.BadApiRequestException;
import com.electronics.store.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        // Original filename fetch karenge
        String originalFilename = file.getOriginalFilename();

        logger.info("Filename: {}", originalFilename);

        // Random unique filename generate karenge
        String filename = UUID.randomUUID().toString();

        // Extension extract karenge
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // File ka naam aur extension jodenge
        String filenameWithExtension = filename + extension;

        // Complete path create karenge
        String fullPathWithFileName = path   + filenameWithExtension;

        logger.info("full image path with name : {} ",fullPathWithFileName);
        // Extension validation
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            // Folder existence check karenge
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs(); // Folder create karenge agar exist nahi karta
            }

            logger.info("file extension is: {}",extension);
            // File copy karenge specified path pe
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return filenameWithExtension;

        } else {
            throw new BadApiRequestException("File with extension " + extension + " is not allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws IOException {
        String fullPath = path + File.separator + name;
        FileInputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
