package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Expense;
import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.entity.Provider;
import com.felysoft.felysoftApp.entity.Purchase;
import com.felysoft.felysoftApp.service.imp.ExpenseImp;
import com.felysoft.felysoftApp.service.imp.PaymentImp;
import com.felysoft.felysoftApp.service.imp.ProviderImp;
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

    @PreAuthorize("hasAuthority('READ_ALL_PURCHASES')")
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

    @PreAuthorize("hasAuthority('READ_ONE_PURCHASE')")
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

    @PreAuthorize("hasAuthority('READ_EXPENSE_BY_PURCHASE')")
    @GetMapping("expensePurchase/{id}")
    public ResponseEntity<Map<String, Object>> findByExpensePurchase(@PathVariable Purchase id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Expense expense = this.expenseImp.findByPurchase(id);

            response.put("status", "success");
            response.put("data", expense);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_PURCHASE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            // Construir el objeto Purchase usando el patrón Builder
            Purchase purchase = Purchase.builder()
                    .date(new Timestamp(System.currentTimeMillis()))
                    .total(new BigDecimal(request.get("total").toString()))
                    .provider(providerImp.findById(Long.parseLong(request.get("fkIdProvider").toString())))
                    .build();

            this.purchaseImp.create(purchase);

            // INSTANCIA OBJETO PAGO
            Payment payment = Payment.builder()
                    .methodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()))
                    .state(Payment.State.valueOf(request.get("state").toString().toUpperCase()))
                    .total(purchase.getTotal())
                    .date(purchase.getDate())
                    .build();

            this.paymentImp.create(payment);

            // INSTANCIA OBJETO GASTO
            Expense expense = Expense.builder()
                    .type(Expense.Type.PROVEEDORES)
                    .total(purchase.getTotal())
                    .date(purchase.getDate())
                    .description(request.get("description").toString().toUpperCase())
                    .payment(payment)
                    .purchase(purchase)
                    .build();

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_PURCHASE')")
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

    @PreAuthorize("hasAuthority('DISABLE_ONE_PURCHASE')")
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
