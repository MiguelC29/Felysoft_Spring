package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Detail;
import com.felysoft.felysoftApp.entities.Expense;
import com.felysoft.felysoftApp.entities.Payment;
import com.felysoft.felysoftApp.entities.Purchase;
import com.felysoft.felysoftApp.services.imp.ExpenseImp;
import com.felysoft.felysoftApp.services.imp.PaymentImp;
import com.felysoft.felysoftApp.services.imp.PurchaseImp;
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
@RequestMapping(path = "/api/expense/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("*")
public class ExpenseController {
    @Autowired
    private ExpenseImp expenseImp;

    @Autowired
    private PaymentImp paymentImp;

    @Autowired
    private PurchaseImp purchaseImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Expense> expenseList = this.expenseImp.findAll();
            response.put("status", "success");
            response.put("data", expenseList);

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
            Expense expense = this.expenseImp.findById(id);
            response.put("status", "success");
            response.put("data", expense);
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
            Expense expense = new Expense();

            //TIPO
            expense.setType(request.get("type").toString());

            //FECHA
            expense.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            //TOTAL
            Integer totalInteger = (Integer) request.get("total");
            BigDecimal total = new BigDecimal(totalInteger);
            expense.setTotal(total);

            //DESCRPCION
            expense.setDescription(request.get("description").toString());

            //FORÁNEAS
            Purchase purchase = purchaseImp.findById(Long.parseLong(request.get("fkIdPurchase").toString()));
            expense.setPurchase(purchase);

            Payment payment = paymentImp.findById(Long.parseLong(request.get("fkIdPayment").toString()));
            expense.setPayment(payment);

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

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Expense expense = this.expenseImp.findById(id);
            expenseImp.delete(expense);

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