package com.github.fontys.trackingsystem.services.interfaces;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.InputStream;

public interface FileService {
    String writeToFile(InputStream inputStream, FormDataContentDisposition fileDetails);
    String getUploadFilePath();
    String checkForExtenstion(FormDataContentDisposition fileDetails);
    File getNewFile(String uploadFileLocation, String fileType);
    String getRandomFileName(String fileType);
    void writeToFile(InputStream uploadedInputStream,
                     File f);
}
