package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Service;
import com.felysoft.felysoftApp.entities.TypeService;
import com.felysoft.felysoftApp.services.imp.ServiceImp;
import com.felysoft.felysoftApp.services.imp.TypeserviceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/service/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("*")
public class ServiceController {

    @Autowired
    private ServiceImp serviceImp;

    @Autowired
    private TypeserviceImp typeServiceImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Service> serviceList = this.serviceImp.findAll();
            response.put("status", "success");
            response.put("data", serviceList);
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
            Service service = this.serviceImp.findById(id);
            response.put("status", "success");
            response.put("data", service);
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
            Service service = new Service();
            service.setState(request.get("state").toString().toUpperCase());
            service.setPriceAdditional(new BigDecimal(request.get("priceAdditional").toString()));
            service.setTotal(new BigDecimal(request.get("total").toString()));

            service.setDateCreation(new Timestamp(System.currentTimeMillis()));
            service.setDateModification(new Timestamp(System.currentTimeMillis()));

            TypeService typeService = typeServiceImp.findById(Long.parseLong(request.get("fkIdTypeService").toString()));
            service.setTypeService(typeService);

            this.serviceImp.create(service);

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
            Service service = this.serviceImp.findById(id);

            service.setState(request.get("state").toString().toUpperCase());
            service.setPriceAdditional(new BigDecimal(request.get("priceAdditional").toString()));
            service.setTotal(new BigDecimal(request.get("total").toString()));
            service.setDateModification(new Timestamp(System.currentTimeMillis()));

            TypeService typeService = typeServiceImp.findById(Long.parseLong(request.get("fkIdTypeService").toString()));
            service.setTypeService(typeService);

            this.serviceImp.update(service);

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
            Service service = this.serviceImp.findById(id);
            serviceImp.delete(service);

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
