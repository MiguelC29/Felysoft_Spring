package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Payment;
import com.felysoft.felysoftApp.services.imp.PaymentImp;
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
@RequestMapping(path = "/api/payment/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("*")
public class PaymentController {
    @Autowired
    private PaymentImp paymentImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Payment> paymentList = this.paymentImp.findAll();
            response.put("status", "success");
            response.put("data", paymentList);

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
            Payment payment = new Payment();

            payment.setMethodPayment(request.get("methodPayment").toString());
            payment.setState(request.get("state").toString());

            //FECHA
            // Convertir la cadena de fecha a LocalDateTime con formato específico
            //OJO CHINOS, PONER LA FECHA DEL MOMENTO AL REGISTRO
            payment.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            //TOTAL
            Integer totalInteger = (Integer) request.get("total");
            BigDecimal total = new BigDecimal(totalInteger);
            payment.setTotal(total);

            this.paymentImp.create(payment);

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