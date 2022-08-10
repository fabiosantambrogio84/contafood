package com.contafood.util;

import com.contafood.service.jpa.NativeQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {

    public static Map<String, Object> createZipFile(NativeQueryService nativeQueryService, String baseDirectory, Integer idExport, String fileNamePrefix) throws Exception{

        Map<String, Object> result = new HashMap<>();
        String resultFilePath = "";
        String resultFileName = "";
        byte[] zipFile;

        String destinationPath = baseDirectory + AdeConstants.FILE_SEPARATOR + idExport;
        Path path = Paths.get(destinationPath);

        long maxSize = AdeConstants.MAX_ZIP_FILE_SIZE;
        long currentSize = 0;
        int index = 1;

        Map<Integer, List<Path>> filesGrouped = new LinkedHashMap<>();
        filesGrouped.put(index, new ArrayList<>());

        // create a map containing xml files to insert into zip file
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){
            for (Path file: directoryStream) {
                long fileSize = file.toFile().length();

                if((currentSize + fileSize) < maxSize){
                    currentSize = currentSize + fileSize;

                    List<Path> filesByIndex = filesGrouped.getOrDefault(index, new ArrayList<>());
                    filesByIndex.add(file);
                    filesGrouped.put(index, filesByIndex);

                } else{
                    currentSize = fileSize;
                    index = index + 1;
                    List<Path> filesByIndex = new ArrayList<>();
                    filesByIndex.add(file);
                    filesGrouped.put(index, filesByIndex);
                }
            }

        } catch(Exception e){
            e.printStackTrace();
            log.error("Error creating xml files map");
            throw e;
        }

        if(!filesGrouped.isEmpty()){

            // create a file zip for every key of the map
            for(Map.Entry<Integer, List<Path>> entry : filesGrouped.entrySet()){

                // create the progressivo for the zip file
                Integer progressivoZip = nativeQueryService.getAdeNextId("zip_file");

                // create zip file name
                String fileName = AdeConstants.PAESE + AdeConstants.CODICE_FISCALE + "_" + StringUtils.leftPad(String.valueOf(progressivoZip), 5, '0')+".zip";

                // retrieve the list of xml file to insert into zip file
                List<Path> filesToZip = entry.getValue();

                try(FileOutputStream fos = new FileOutputStream(path.toAbsolutePath() + AdeConstants.FILE_SEPARATOR + fileName);
                    ZipOutputStream zipOut = new ZipOutputStream(fos)){

                    for(Path fileToZip : filesToZip){
                        try(FileInputStream fis = new FileInputStream(fileToZip.toFile())){
                            ZipEntry zipEntry = new ZipEntry(fileToZip.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);

                            byte[] bytes = new byte[1024];
                            int length;
                            while((length = fis.read(bytes)) >= 0) {
                                zipOut.write(bytes, 0, length);
                            }
                        }
                    }

                    // delete xml files inserted into zip file
                    //removeFiles(filesToZip, new ArrayList<>());

                } catch(Exception e){
                    e.printStackTrace();
                    log.error("Error creating zip files");
                    throw e;
                }
            }

            // create the zip file containing all the zip files
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyyMMdd_HHmmss");

            resultFileName = fileNamePrefix+"_"+sdf.format(new Date(System.currentTimeMillis()))+".zip";
            resultFilePath = path.toAbsolutePath() + AdeConstants.FILE_SEPARATOR + resultFileName;

            try(FileOutputStream fos = new FileOutputStream(resultFilePath);
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){

                for (Path file: directoryStream) {
                    if(file.getFileName().toString().endsWith("zip") && !file.getFileName().toString().startsWith(fileNamePrefix)){
                        try(FileInputStream fis = new FileInputStream(file.toFile())){
                            ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);

                            byte[] bytes = new byte[1024];
                            int length;
                            while((length = fis.read(bytes)) >= 0) {
                                zipOut.write(bytes, 0, length);
                            }
                        }
                    }
                }

            } catch(Exception e){
                e.printStackTrace();
                log.error("Error creating final zip file");
                throw e;
            }
        }

        zipFile = Files.readAllBytes(Paths.get(resultFilePath));

        // remove all zip files
        Utils.removeFileOrDirectory(path);

        result.put("fileName", resultFileName);
        result.put("content", zipFile);

        return result;
    }

    /*
    public static byte[] createZipFile(Map<String, byte[]> entries) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);
        for(String entry : entries.keySet()){
            ByteArrayInputStream bais = new ByteArrayInputStream(entries.get(entry));
            ZipEntry zipEntry = new ZipEntry(entry);
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = bais.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            bais.close();
        }
        zipOut.close();

        return baos.toByteArray();
    }
    */

    /*
    private static void saveFile(byte[] content){
        File outputFile = new File("C:\\temp\\tmp.zip");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(content);
        }
    }
     */
}
