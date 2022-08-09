package com.contafood.xTemp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Temp {

    public static void main(String[] args) throws Exception {

        String tokenExpiration = "2022-06-17T23:00:00";

        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(tokenExpiration));
        ZonedDateTime tokenExpirationZdt = localDateTime.atZone(ZoneId.of("UTC"));
        //Timestamp ts = Timestamp.valueOf(localDateTime);
        System.out.println(tokenExpirationZdt);

        ZonedDateTime nowZdt = ZonedDateTime.now(ZoneId.of("UTC"));
        //Timestamp nowTs = Timestamp.from(nowZdt.toInstant());
        System.out.println(nowZdt);

        System.out.println(nowZdt.isAfter(null));
    }
}
