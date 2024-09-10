package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.imp.*;
import com.felysoft.felysoftApp.service.imp.DetailImp;
import jakarta.transaction.Transactional;
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
@RequestMapping("/api/sale/")
public class SaleController {
    @Autowired
    private SaleImp saleImp;

    @Autowired
    private PaymentImp paymentImp;

    @Autowired
    private DetailImp detailImp;

    @Autowired
    private InventoryImp inventoryImp;

    @Autowired
    private ProductImp productImp;

    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
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

    @PreAuthorize("hasAuthority('READ_ALL_SALES_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Sale> saleList = this.saleImp.findAllDisabled();
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
    @Transactional
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // TODO: HACER QUE SI DA UN ERRRO HAGA UN ROLLBACK DEL REGISTRO DE PAGOS Y DEMAS
            // INSTANCIA OBJETO PAGO
            Payment payment = Payment.builder()
                    .methodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()))
                    .state(Payment.State.valueOf(request.get("state").toString().toUpperCase()))
                    .total(new BigDecimal(request.get("total").toString()))
                    .date(new Timestamp(System.currentTimeMillis()))
                    .build();

            this.paymentImp.create(payment);

            Sale sale = Sale.builder()
                    .dateSale(payment.getDate())
                    .totalSale(payment.getTotal())
                    .payment(payment)
                    .build();

            this.saleImp.create(sale);

            // Registrar los detalles de la compra
            List<Map<String, Object>> detailsRequest = (List<Map<String, Object>>) request.get("details");

            for (Map<String, Object> detailRequest : detailsRequest) {
                if (detailRequest.get("idProduct") != null) {
                    Detail detail = new Detail();
                    // Si es un producto, necesitamos cantidad y precio unitario
                    Product product = productImp.findById(Long.parseLong(detailRequest.get("idProduct").toString()));
                    if (product == null) {
                        response.put("status", HttpStatus.NOT_FOUND);
                        response.put("data", "Producto no encontrado");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }
                    var cantidad = Integer.parseInt(detailRequest.get("quantity").toString());
                    detail.setProduct(product);
                    detail.setQuantity(cantidad);

                    Inventory inventory = inventoryImp.findByProduct(product);
                    if (cantidad > inventory.getStock()) {
                        response.put("status", HttpStatus.BAD_GATEWAY);
                        response.put("data", "No hay suficientes productos");
                        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
                    } else {
                        inventory.setStock(inventory.getStock() - cantidad);
                        updateInventoryState(inventory);
                        inventoryImp.update(inventory);
                    }
                    detail.setUnitPrice(new BigDecimal(detailRequest.get("unitPrice").toString()));
                    detail.setEliminated(false);
                    detail.setSale(sale);

                    this.detailImp.create(detail);  // Registrar cada detalle en la base de datos
                }
            }
            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void updateInventoryState(Inventory inventory) {
        if (inventory.getStock() < 1) {
            inventory.setState(Inventory.State.AGOTADO);
        } else if (inventory.getStock() < 6) {
            inventory.setState(Inventory.State.BAJO);
        } else {
            inventory.setState(Inventory.State.DISPONIBLE);
        }
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

    @PreAuthorize("hasAuthority('UPDATE_ONE_SALE_DISABLED')")
    @PutMapping(value = "enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findByIdDisabled(id);
            if (sale == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Venta no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            sale.setEliminated(false);
            saleImp.update(sale);

            response.put("status", "success");
            response.put("data", "Habilitada Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_SALE')")
    @PutMapping(value = "delete/{id}")
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
