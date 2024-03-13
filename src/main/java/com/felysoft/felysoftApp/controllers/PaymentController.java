package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Charge;
import com.felysoft.felysoftApp.entities.Expense;
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

    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Payment payment = this.paymentImp.findById(id);
            response.put("status", "success");
            response.put("data", payment);
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

            //MÉTODO (ENUM)
            payment.setMethodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()));

            //ESTADO (ENUM)
            payment.setState(Payment.State.valueOf(request.get("state").toString().toUpperCase()));

            //FECHA
            //payment.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            payment.setDate(LocalDateTime.now());

            //TOTAL
            payment.setTotal(new BigDecimal(request.get("total").toString()));

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

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Payment payment = this.paymentImp.findById(id);

            //MÉTODO
            payment.setMethodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()));

            //ESTADO
            payment.setState(Payment.State.valueOf(request.get("state").toString().toUpperCase()));

            //FECHA
            //payment.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            payment.setDate(LocalDateTime.now());

            //TOTAL
            payment.setTotal(new BigDecimal(request.get("total").toString()));

            this.paymentImp.update(payment);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Payment payment = this.paymentImp.findById(id);
            paymentImp.delete(payment);

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