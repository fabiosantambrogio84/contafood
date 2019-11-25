package com.contafood.service;

import com.contafood.exception.FileStorageException;
import com.contafood.properties.FileStorageProperties;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private static Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(final FileStorageProperties fileStorageProperties){
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDirectory()).toAbsolutePath().normalize();
        try{
            LOGGER.info("File storage location '{}'", this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation);
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ImmutablePair<String,String> storeFile(Long articoloId, MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        LOGGER.info("Storing file '{}'", fileName);

        Path finalFileStorageLocation = Paths.get(this.fileStorageLocation.toString(),"articolo",String.valueOf(articoloId));

        try{
            Files.createDirectories(finalFileStorageLocation);

            if(fileName.contains("..")){
                throw new FileStorageException("Filename '"+fileName+"' contains invalid path");
            }
            Path targetLocation = finalFileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info("File successfully stored");

            return new ImmutablePair<>(targetLocation.toAbsolutePath().toString(),fileName);
        } catch(Exception e){
            throw new FileStorageException("Error storing file '"+fileName+"'", e);
        }
    }

}
