package com.contafood.service;

import com.contafood.exception.FileStorageException;
import com.contafood.properties.FileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileStorageService {

    private static Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    private String baseDirectory;

    private String uploadDirectory;

    private final Path fileStorageLocation;

    private static final String ARTICOLI_DIRECTORY = "articoli";

    @Autowired
    public FileStorageService(final FileStorageProperties fileStorageProperties){
        this.baseDirectory = fileStorageProperties.getBaseDirectory();
        this.uploadDirectory = fileStorageProperties.getUploadDirectory();
        this.fileStorageLocation = Paths.get(this.baseDirectory,this.uploadDirectory).toAbsolutePath().normalize();
        try{
            LOGGER.info("File storage location '{}'", this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation);
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<String,String> storeArticoloImmagine(Long articoloId, MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        LOGGER.info("Storing file '{}'", fileName);

        String filePath = Paths.get(this.uploadDirectory, ARTICOLI_DIRECTORY, String.valueOf(articoloId), fileName).toString();

        Path finalFileStorageLocation = Paths.get(this.fileStorageLocation.toString(),ARTICOLI_DIRECTORY, String.valueOf(articoloId));

        try{
            Files.createDirectories(finalFileStorageLocation);

            if(fileName.contains("..")){
                throw new FileStorageException("Filename '"+fileName+"' contains invalid path");
            }
            Path targetLocation = finalFileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info("File successfully stored");

            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("fileName", fileName);
            returnMap.put("filePath", filePath);
            returnMap.put("fileCompletePath", targetLocation.toAbsolutePath().toString());

            return returnMap;
        } catch(Exception e){
            throw new FileStorageException("Error storing file '"+fileName+"'", e);
        }
    }

    public void deleteFile(String path){
        try{
            Path filePath = Paths.get(path);
            Files.delete(filePath);

            Path parentPath = filePath.getParent();
            int countFiles = countFilesInDirectory(parentPath);
            if(countFiles == 0){
                Files.delete(parentPath);
            }

        } catch(Exception e ){
            LOGGER.error("Error deleting file '{}'", path);
        }
    }

    private int countFilesInDirectory(Path directoryPath) throws IOException {
        List<String> fileNames = new ArrayList<>();
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath);
        for (Path path : directoryStream) {
            fileNames.add(path.toString());
        }
        return fileNames.size();
    }

}
