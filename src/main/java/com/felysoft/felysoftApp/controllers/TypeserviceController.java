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
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            TypeService typeService = this.typeserviceImp.findById(id);
            response.put("status", "success");
            response.put("data", typeService);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
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
            typeService.setName(request.get("name").toString().toUpperCase());

            //Description
            typeService.setDescription(request.get("description").toString().toUpperCase());

            //Price
            BigDecimal price = new BigDecimal(request.get("price").toString().toUpperCase());
            typeService.setPrice(price);

            this.typeserviceImp.create(typeService);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");

        } catch (Exception e) {

            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            TypeService typeService = this.typeserviceImp.findById(id);

            if (typeService == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "El tipo de servicio con el ID proporcionado no fue encontrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            typeService.setName(request.get("name").toString().toUpperCase());
            typeService.setDescription(request.get("description").toString().toUpperCase());
            BigDecimal price = new BigDecimal(request.get("price").toString().toUpperCase());
            typeService.setPrice(price);

            this.typeserviceImp.update(typeService);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            TypeService typeService = this.typeserviceImp.findById(id);
            typeserviceImp.delete(typeService);

            response.put("status", "success");
            response.put("data", "Eliminado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
