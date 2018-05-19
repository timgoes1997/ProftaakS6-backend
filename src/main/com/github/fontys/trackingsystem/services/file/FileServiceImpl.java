package com.github.fontys.trackingsystem.services.file;

import com.github.fontys.trackingsystem.services.interfaces.FileService;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.inject.Inject;
import javax.ws.rs.NotAcceptableException;
import java.io.*;
import java.util.UUID;
import java.util.logging.Logger;

public class FileServiceImpl implements FileService {

    @Inject
    private Logger logger;

    @Override
    public String writeToFile(InputStream inputStream, FormDataContentDisposition fileDetails) {
        File file = getNewFile(getUploadFilePath(), checkForExtenstion(fileDetails));
        writeToFile(inputStream, fileDetails);
        return file.getAbsolutePath();
    }

    @Override
    public String getUploadFilePath() {
        return System.getProperty("user.dir") + "//files//";
    }

    @Override
    public String checkForExtenstion(FormDataContentDisposition fileDetails) {
        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if (extension.equals("")) {
            throw new NotAcceptableException("No file given");
        }

        if (extension.equals("pdf")
                || extension.equals("word")
                || extension.equals("png")
                || extension.equals("jpg")) {
            return extension;
        }
        throw new NotAcceptableException("File not allowed");
    }

    @Override
    public File getNewFile(String uploadFileLocation, String fileType) {
        File f = new File(uploadFileLocation + getRandomFileName(fileType));
        while (f.exists()) {
            f = new File(uploadFileLocation + getRandomFileName(fileType));
        }
        return f;
    }

    @Override
    public String getRandomFileName(String fileType) {
        return UUID.randomUUID().toString() + "." + fileType;
    }

    @Override
    public void writeToFile(InputStream uploadedInputStream, File f) {
        try {
            OutputStream out;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(f);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
