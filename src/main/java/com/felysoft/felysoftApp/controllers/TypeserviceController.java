package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.TypeService;
import com.felysoft.felysoftApp.services.imp.TypeserviceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/typeservice/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("*")
public class TypeserviceController {

    @Autowired
    private TypeserviceImp typeserviceImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){

        Map<String, Object> response = new HashMap<>();

        try {
            List<TypeService> typeServiceList = this.typeserviceImp.findAll();
            response.put("status", "success");
            response.put("data", typeServiceList);

        } catch (Exception e) {

            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();

        try {

            System.out.println("@@@@" + request);

            TypeService typeService = new TypeService();

            //Id
            //typeService.setIdTypeService(Long.parseLong(request.get("idTypeService").toString()));

            //Name
            typeService.setName(request.get("name").toString());

            //Description
            typeService.setDescription(request.get("description").toString());

            //Price
            BigDecimal price = new BigDecimal(request.get("price").toString());
            typeService.setPrice(price);

            this.typeserviceImp.create(typeService);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");

        } catch (Exception e) {

            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
