package com.contafood.controller;

import com.contafood.util.Provincia;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/util")
public class UtilController {

    @RequestMapping(method = GET, path = "/province")
    @CrossOrigin
    public List<String> getAll() {
        return Provincia.labels();
    }

}
