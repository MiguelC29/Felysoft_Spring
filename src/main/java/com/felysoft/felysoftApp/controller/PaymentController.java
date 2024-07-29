package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.service.imp.PaymentImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/payment/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class PaymentController {
    @Autowired
    private PaymentImp paymentImp;

    @PreAuthorize("hasAuthority('READ_ALL_PAYMENTS')")
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

    @PreAuthorize("hasAuthority('READ_ONE_PAYMENT')")
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

    @PreAuthorize("hasAuthority('CREATE_ONE_PAYMENT')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            Payment payment = Payment.builder()
                    .methodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()))
                    .state(Payment.State.valueOf(request.get("state").toString().toUpperCase()))
                    .date(new Timestamp(System.currentTimeMillis()))
                    .total(new BigDecimal(request.get("total").toString()))
                    .build();

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_PAYMENT')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Payment payment = this.paymentImp.findById(id);

            payment.setMethodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase())); //MÉTODO

            payment.setState(Payment.State.valueOf(request.get("state").toString().toUpperCase())); //ESTADO

            //FECHA
            //payment.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //payment.setDate(LocalDateTime.now());
            payment.setDate(new Timestamp(System.currentTimeMillis()));

            payment.setTotal(new BigDecimal(request.get("total").toString())); //TOTAL

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

    @PreAuthorize("hasAuthority('DISABLE_ONE_PAYMENT')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Payment payment = this.paymentImp.findById(id);
            payment.setEliminated(true);

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
