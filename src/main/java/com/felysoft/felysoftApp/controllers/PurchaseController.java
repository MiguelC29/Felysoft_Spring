package com.felysoft.felysoftApp.controllers;


import com.felysoft.felysoftApp.entities.*;
import com.felysoft.felysoftApp.services.imp.ProviderImp;
import com.felysoft.felysoftApp.services.imp.PurchaseImp;
import com.felysoft.felysoftApp.services.imp.ExpenseImp;
import com.felysoft.felysoftApp.services.imp.PaymentImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/purchase/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class PurchaseController {
    @Autowired
    private PurchaseImp purchaseImp;

    @Autowired
    private ExpenseImp expenseImp;

    @Autowired
    private PaymentImp paymentImp;

    @Autowired
    private ProviderImp providerImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Purchase> purchaseList = this.purchaseImp.findAll();
            response.put("status", "success");
            response.put("data", purchaseList);

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
            Purchase purchase = this.purchaseImp.findById(id);
            response.put("status", "success");
            response.put("data", purchase);
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
            Purchase purchase = new Purchase();

            //FECHA
            //purchase.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //purchase.setDate(LocalDateTime.now());
            purchase.setDate(new Timestamp(System.currentTimeMillis()));

            //TOTAL
            purchase.setTotal(new BigDecimal(request.get("total").toString()));

            //FORÁNEAS
            Provider provider = providerImp.findById(Long.parseLong(request.get("fkIdProvider").toString()));
            purchase.setProvider(provider);

            this.purchaseImp.create(purchase);

            // INSTANCIA OBJETO PAGO
            Payment payment = new Payment();

            // CAMPOS PROPIOS ENTIDAD PAGO
            payment.setMethodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()));
            payment.setState(Payment.State.valueOf(request.get("state").toString().toUpperCase()));
            payment.setTotal(purchase.getTotal());
            payment.setDate(purchase.getDate());

            this.paymentImp.create(payment);

            // INSTANCIA OBJETO GASTO
            Expense expense = new Expense();
            // CAMPOS PROPIOS ENTIDAD GASTO
            expense.setType(Expense.Type.PROVEEDORES);
            expense.setTotal(purchase.getTotal());
            expense.setDate(purchase.getDate());
            expense.setDescription(request.get("description").toString().toUpperCase());
            expense.setPayment(payment);

            // CAMPOS LLAVES FORANEAS
            expense.setPurchase(purchase);

           this.expenseImp.create(expense);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");

        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("add-detail")
    public ResponseEntity<Map<String, Object>> addDetailToPurchase(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            Long purchaseId = Long.parseLong(request.get("purchaseId").toString());
            Long detailId = Long.parseLong(request.get("detailId").toString());

            this.purchaseImp.addDetailToPurchase(purchaseId, detailId);

            response.put("status", "success");
            response.put("data", "Asociación Exitosa");

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
            Purchase purchase = this.purchaseImp.findById(id);

            purchase.setDate(new Timestamp(System.currentTimeMillis()));
            purchase.setTotal(new BigDecimal(request.get("total").toString()));

            //FORÁNEAS
            Provider provider = providerImp.findById(Long.parseLong(request.get("fkIdProvider").toString()));
            purchase.setProvider(provider);

            this.purchaseImp.update(purchase);


    /// INSTANCIA OBJETO GASTO
            Expense expense = this.expenseImp.findByPurchase(purchase);

            expense.setDate(purchase.getDate());
            expense.setTotal(purchase.getTotal());
            expense.setDescription(request.get("description").toString().toUpperCase());

            this.expenseImp.update(expense);


    // INSTANCIA OBJETO PAGO
            Payment payment = this.paymentImp.findById(expense.getPayment().getIdPayment());

            payment.setMethodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()));
            payment.setState(Payment.State.valueOf(request.get("state").toString().toUpperCase()));
            payment.setDate(purchase.getDate());
            payment.setTotal(purchase.getTotal());

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
            Purchase purchase = this.purchaseImp.findById(id);

            purchase.setEliminated(true);

            purchaseImp.delete(purchase);

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