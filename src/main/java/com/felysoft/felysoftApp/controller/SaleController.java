package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.entity.Sale;
import com.felysoft.felysoftApp.service.imp.PaymentImp;
import com.felysoft.felysoftApp.service.imp.SaleImp;
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
@RequestMapping(path = "/api/sale/", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class SaleController {
    @Autowired
    private SaleImp saleImp;

    @Autowired
    private PaymentImp paymentImp;

    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
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

    @PreAuthorize("hasAuthority('READ_ONE_SALE')")
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

    @PreAuthorize("hasAuthority('CREATE_ONE_SALE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = Sale.builder()
                    .dateSale(new Timestamp(System.currentTimeMillis()))
                    .totalSale(new BigDecimal(request.get("totalSale").toString()))
                    .payment(paymentImp.findById(Long.parseLong(request.get("fkIdPayment").toString())))
                    .build();

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

    @PostMapping("add-detail")
    public ResponseEntity<Map<String, Object>> addDetailToSale(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            Long saleId = Long.parseLong(request.get("saleId").toString());
            Long detailId = Long.parseLong(request.get("detailId").toString());

            this.saleImp.addDetailToSale(saleId, detailId);

            response.put("status", "success");
            response.put("data", "Asociación Exitosa");

        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_SALE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findById(id);

            //FECHA
            // Convertir la cadena de fecha a LocalDateTime con formato específico
            sale.setDateSale(new Timestamp(System.currentTimeMillis()));

            //TOTAL
            sale.setTotalSale(new BigDecimal(request.get("totalSale").toString()));

            //FORÁNEAS
            Payment payment = paymentImp.findById(Long.parseLong(request.get("fkIdPayment").toString()));
            sale.setPayment(payment);

            this.saleImp.update(sale);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_SALE')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findById(id);
            sale.setEliminated(true);

            this.saleImp.delete(sale);

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
