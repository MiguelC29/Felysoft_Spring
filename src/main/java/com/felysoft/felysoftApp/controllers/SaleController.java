package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Payment;
import com.felysoft.felysoftApp.entities.Sale;
import com.felysoft.felysoftApp.services.imp.PaymentImp;
import com.felysoft.felysoftApp.services.imp.SaleImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/sale/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("*")
public class SaleController {
    @Autowired
    private SaleImp saleImp;
    @Autowired
    private PaymentImp paymentImp;


    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Sale> saleList = this.saleImp.findAll();
            response.put("status", "success");
            response.put("data", saleList);

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
            Sale sale = this.saleImp.findById(id);
            response.put("status", "success");
            response.put("data", sale);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = new Sale();

            //FECHA
            // Convertir la cadena de fecha a LocalDateTime con formato específico
            //OJO CHINOS, PONER LA FECHA DEL MOMENTO AL REGISTRO
            sale.setDateSale(LocalDateTime.now());

            //TOTAL
            Integer totalSaleInteger = (Integer) request.get("totalSale");
            BigDecimal totalSale = new BigDecimal(totalSaleInteger);
            sale.setTotalSale(totalSale);

            //FORÁNEAS
            Payment payment = paymentImp.findById(Long.parseLong(request.get("fkIdPayment").toString()));
            sale.setPayment(payment);

            this.saleImp.create(sale);

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


