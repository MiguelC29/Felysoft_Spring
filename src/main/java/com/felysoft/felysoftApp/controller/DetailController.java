package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.imp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detail/")
public class DetailController {
    @Autowired
    private DetailImp detailImp;

    @Autowired
    private ProductImp productImp;

    @Autowired
    private BookImp bookImp;

    @Autowired
    private ServiceImp serviceImp;

    @Autowired
    private PurchaseImp purchaseImp;

    @Autowired
    private SaleImp saleImp;

    @PreAuthorize("hasAuthority('READ_ALL_DETAILS')")
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

    @PreAuthorize("hasAuthority('READ_ONE_DETAIL')")
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

    @PreAuthorize("hasAuthority('CREATE_ONE_DETAIL')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Detail.DetailBuilder detailBuilder = Detail.builder()
                    .quantity(Integer.parseInt(request.get("quantity").toString()))
                    .unitPrice(new BigDecimal(request.get("unitPrice").toString()));

            //FORANEAS
            // Verificar si la clave "fkIdProduct" está presente en el mapa y no es null
            if (request.containsKey("fkIdProduct") && request.get("fkIdProduct") != null) {
                Product product = productImp.findById(Long.parseLong(request.get("fkIdProduct").toString()));
                detailBuilder.product(product);
            }

            // Verificar si la clave "fkIdBook" está presente en el mapa y no es null
            if (request.containsKey("fkIdBook") && request.get("fkIdBook") != null) {
                Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
                detailBuilder.book(book);
            }

            // Construir el objeto Detail
            Detail detail = detailBuilder.build();

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_DETAIL')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Detail detail = this.detailImp.findById(id);

            detail.setQuantity((Integer.parseInt(request.get("quantity").toString()))); //CANTIDAD
            detail.setUnitPrice(new BigDecimal(request.get("unitPrice").toString())); //PRECIO UNICO

            //FORANEAS
            Product product = productImp.findById(Long.parseLong(request.get("fkIdProduct").toString()));
            detail.setProduct(product);

            Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
            detail.setBook(book);

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

    @PreAuthorize("hasAuthority('DISABLE_ONE_DETAIL')")
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

    @PreAuthorize("hasAuthority('READ_ALL_DETAILS')")
    @GetMapping("purchaseDetails/{id}")
    public ResponseEntity<Map<String, Object>> findByIdPurchase(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Purchase purchase = this.purchaseImp.findById(id);
            if (purchase == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Compra no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            List<Detail> detailList = this.detailImp.findByPurchase(purchase);

            response.put("status", "success");
            response.put("data", detailList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ALL_DETAILS')")
    @GetMapping("saleDetails/{id}")
    public ResponseEntity<Map<String, Object>> findByIdSale(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findById(id);
            if (sale == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Venta no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            List<Detail> detailList = this.detailImp.findBySale(sale);

            response.put("status", "success");
            response.put("data", detailList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
