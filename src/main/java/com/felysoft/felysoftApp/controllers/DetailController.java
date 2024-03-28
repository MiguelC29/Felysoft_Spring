package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.*;
import com.felysoft.felysoftApp.services.imp.BookImp;
import com.felysoft.felysoftApp.services.imp.DetailImp;
import com.felysoft.felysoftApp.services.imp.ProductImp;
import com.felysoft.felysoftApp.services.imp.ServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/detail/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class DetailController {
    @Autowired
    private DetailImp detailImp;
    @Autowired
    private ProductImp productImp;
    @Autowired
    private BookImp bookImp;
    @Autowired
    private ServiceImp serviceImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Detail> detailList = this.detailImp.findAll();
            response.put("status", "success");
            response.put("data", detailList);

        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Detail detail = this.detailImp.findById(id);
            response.put("status", "success");
            response.put("data", detail);
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
            Detail detail = new Detail();

            //CANTIDAD
            detail.setQuantity((Integer.parseInt(request.get("quantity").toString())));

            //PRECIO UNICO
            detail.setUnitPrice(new BigDecimal(request.get("unitPrice").toString()));


            //FORANEAS
            Product product = productImp.findById(Long.parseLong(request.get("fkIdProduct").toString()));
            detail.setProduct(product);

            Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
            detail.setBook(book);

            Service service = serviceImp.findById(Long.parseLong(request.get("fkIdService").toString()));
            detail.setService(service);

            this.detailImp.create(detail);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");

        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Detail detail = this.detailImp.findById(id);

            //CANTIDAD
            detail.setQuantity((Integer.parseInt(request.get("quantity").toString())));

            //PRECIO UNICO
            detail.setUnitPrice(new BigDecimal(request.get("unitPrice").toString()));


            //FORANEAS
            Product product = productImp.findById(Long.parseLong(request.get("fkIdProduct").toString()));
            detail.setProduct(product);

            Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
            detail.setBook(book);

            Service service = serviceImp.findById(Long.parseLong(request.get("fkIdService").toString()));
            detail.setService(service);

            this.detailImp.update(detail);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Detail detail = this.detailImp.findById(id);

            detail.setEliminated(true);

            detailImp.delete(detail);

            response.put("status", "success");
            response.put("data", "Eliminado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}