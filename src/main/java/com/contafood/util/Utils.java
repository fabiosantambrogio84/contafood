package com.contafood.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;

@Slf4j
public class Utils {

    public static boolean stringContainsOnlyCertainCharacters(String input){
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if(!Constants.BARCODE_ALLOWED_CHARS.contains(ch)){
                return false;
            }
        }
        return true;
    }

    public static void removeFileOrDirectory(Path path){
        try{
            File file = path.toFile();
            if(file.isDirectory()){
                FileUtils.deleteDirectory(file);
            } else{
                file.delete();
            }

        } catch(Exception e){
            e.printStackTrace();
            log.error("Error deleting file '{}'", path.toAbsolutePath());
        }
    }

    public static BigDecimal roundPrice(BigDecimal price){
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundQuantity(BigDecimal quantity){
        return quantity.setScale(2, RoundingMode.HALF_UP);
    }
}
