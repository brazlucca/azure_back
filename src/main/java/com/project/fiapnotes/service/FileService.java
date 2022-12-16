package com.project.fiapnotes.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.project.fiapnotes.config.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private final Path fileStorageLocation;
    private final BlobServiceClient blobServiceClient;

    @Autowired
    public FileService(@NonNull FileStorageProperties fileStorageProperties, BlobServiceClient blobServiceClient) {
        // get the path of the upload directory
        fileStorageLocation = Path.of(fileStorageProperties.getUploadDir());
        this.blobServiceClient = blobServiceClient;
        try {
            // creates directory/directories, if directory already exists, it will not throw exception
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            //log.error("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public Path getFileStorageLocation() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            //log.error("Could not create the directory where the uploaded file will be stored.", e);
        }
        return fileStorageLocation;
    }

    public Boolean uploadAndDownloadFile(@NonNull MultipartFile file, String containerName, String filename) {
        boolean isSuccess = true;
        BlobContainerClient blobContainerClient = getBlobContainerClient(containerName);
        //String filename = file.getOriginalFilename();
        BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(filename).getBlockBlobClient();
        try {
            // delete file if already exists in that container
            if (blockBlobClient.exists()) {
                blockBlobClient.delete();
            }
            // upload file to azure blob storage
            blockBlobClient.upload(new BufferedInputStream(file.getInputStream()), file.getSize(), true);
            String tempFilePath = fileStorageLocation + "/" + filename;
            Files.deleteIfExists(Paths.get(tempFilePath));
            // download file from azure blob storage to a file
            blockBlobClient.downloadToFile(new File(tempFilePath).getPath());
        } catch (IOException e) {
            isSuccess = false;
            //log.error("Error while processing file {}", e.getLocalizedMessage());
        }
        return isSuccess;
    }


    private @NonNull BlobContainerClient getBlobContainerClient(@NonNull String containerName) {
        // create container if not exists
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }
        return blobContainerClient;
    }

    public @NonNull ByteArrayResource getBlobContainerClient2(String containerName, String filename) {
        BlobContainerClient blobContainerClient = getBlobContainerClient(containerName);
        //String filename = file.getOriginalFilename();
        BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(filename).getBlockBlobClient();

        if (blockBlobClient.exists()) {
//            String tempFilePath = fileStorageLocation + "/" + filename;
//            BlobProperties blobProperties = blockBlobClient.downloadToFile(new File(tempFilePath).getPath());

            BlobClient blob = blobContainerClient.getBlobClient(filename);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);
            final byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ByteArrayResource resource = new ByteArrayResource(bytes);

            return resource;
        }

        return null;

    }
}
