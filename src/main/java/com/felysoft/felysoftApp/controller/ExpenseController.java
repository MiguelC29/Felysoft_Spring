package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Expense;
import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.entity.Purchase;
import com.felysoft.felysoftApp.service.imp.ExpenseImp;
import com.felysoft.felysoftApp.service.imp.PaymentImp;
import com.felysoft.felysoftApp.service.imp.PurchaseImp;
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
@RequestMapping(path = "/api/expense/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class ExpenseController {
    @Autowired
    private ExpenseImp expenseImp;

    @Autowired
    private PaymentImp paymentImp;

    @Autowired
    private PurchaseImp purchaseImp;

    @PreAuthorize("hasAuthority('READ_ALL_EXPENSES')")
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

    @PreAuthorize("hasAuthority('READ_ONE_EXPENSE')")
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

    @PreAuthorize("hasAuthority('CREATE_ONE_EXPENSE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            // Obtener y asignar las llaves foráneas antes de construir el objeto Expense
            Purchase purchase = purchaseImp.findById(Long.parseLong(request.get("fkIdPurchase").toString()));
            Payment payment = paymentImp.findById(Long.parseLong(request.get("fkIdPayment").toString()));

            Expense expense = Expense.builder()
                    .type(Expense.Type.valueOf(request.get("type").toString().toUpperCase()))
                    .date(new Timestamp(System.currentTimeMillis()))
                    .total(new BigDecimal(request.get("total").toString()))
                    .description(request.get("description").toString().toUpperCase())
                    .purchase(purchase)
                    .payment(payment)
                    .build();

            //FECHA
            //expense.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //expense.setDate(LocalDateTime.now());
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

    @PreAuthorize("hasAuthority('UPDATE_ONE_EXPENSE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Expense expense = this.expenseImp.findById(id);

            expense.setType(Expense.Type.valueOf(request.get("type").toString().toUpperCase())); //TIPO

            //FECHA
            //expense.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //expense.setDate(LocalDateTime.now());
            expense.setDate(new Timestamp(System.currentTimeMillis()));

            //TOTAL
            expense.setTotal(new BigDecimal(request.get("total").toString()));

            //DESCRIPCION
            expense.setDescription(request.get("description").toString().toUpperCase());

            //FORÁNEAS
            Purchase purchase = purchaseImp.findById(Long.parseLong(request.get("fkIdPurchase").toString()));
            expense.setPurchase(purchase);

            Payment payment = paymentImp.findById(Long.parseLong(request.get("fkIdPayment").toString()));
            expense.setPayment(payment);

            this.expenseImp.update(expense);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_EXPENSE')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Expense expense = this.expenseImp.findById(id);
            expense.setEliminated(true);

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
