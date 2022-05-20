package com.contafood.xTemp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class Temp {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        System.out.println("START");
        File path = new File("C:\\Users\\Overdata\\Desktop\\Archivio\\output\\targetRevised");

        StringBuilder outputStringBuilder = new StringBuilder();
        String outputFilePath = "C:\\Users\\Overdata\\Desktop\\ts_insert_statements.sql";

        int count = 1;
        File [] files = path.listFiles();
        if(files != null) {
            //System.out.println(files.length);
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String[] tokens = fileName.replace(".json","").split("_");
                    String storeCode = tokens[0];
                    Integer year = Integer.valueOf(tokens[1]);
                    Integer month = Integer.valueOf(tokens[2]);
                    Integer version = Integer.valueOf(tokens[4]);
                    //System.out.println(storeCode+"-"+year+"-"+month+"-"+version);

                    StringBuilder contentBuilder = new StringBuilder();

                    try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
                        //Read the content with Stream
                        stream.forEach(contentBuilder::append);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String content = contentBuilder.toString().replace(" ","");

                    TargetSettingsBean targetSettingsBean = new TargetSettingsBean();
                    Map<String, Object> contentAsMap = objectMapper.readValue(content, Map.class);
                    targetSettingsBean.setType((String)contentAsMap.get("type"));
                    targetSettingsBean.setVersion((Integer)contentAsMap.get("version"));


                    String insertStatement = "INSERT INTO import.target_settings_mock(id,store_code,year,month,version,payload) VALUES(";
                    insertStatement += count + ",";
                    insertStatement += "'" + storeCode + "',";
                    insertStatement += year + ",";
                    insertStatement += month + ",";
                    insertStatement += "'" + version + "',";
                    insertStatement += "'" + content + "'";
                    insertStatement += ");";

                    outputStringBuilder.append(insertStatement).append("\n");

                    count += 1;
                }
            }
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(outputStringBuilder.toString());
        }

        System.out.println("END");
    }
}
