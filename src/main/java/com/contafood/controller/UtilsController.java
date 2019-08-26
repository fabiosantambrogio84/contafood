package com.contafood.controller;

import com.contafood.util.Provincia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/utils")
public class UtilsController {

    private static Logger LOGGER = LoggerFactory.getLogger(UtilsController.class);

    @RequestMapping(method = GET, path = "/province")
    @CrossOrigin
    public List<String> getAll() {
        LOGGER.info("Performing GET request for retrieving list of 'province'");
        return Provincia.labels();
    }

}
