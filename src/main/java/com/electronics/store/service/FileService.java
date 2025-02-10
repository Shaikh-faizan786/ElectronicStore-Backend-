package com.electronics.store.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    // String path = jaha image store karna hai

     String uploadFile(MultipartFile file, String path) throws IOException;

     InputStream getResource(String path,String name) throws IOException;


}