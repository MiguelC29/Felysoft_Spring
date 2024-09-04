package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.imp.PaymentImp;
import com.felysoft.felysoftApp.service.imp.SaleImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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


    //CARRITO
    // Lista temporal para almacenar los detalles del carrito
    private List<Detail> cart = new ArrayList<>();

    // Método para agregar productos al carrito
    @PostMapping("cart/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Detail detail) {
        Map<String, Object> response = new HashMap<>();
        try {
            cart.add(detail);
            response.put("status", "success");
            response.put("data", cart);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Método para visualizar el carrito
    @GetMapping("cart")
    public ResponseEntity<Map<String, Object>> viewCart() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", "success");
            response.put("data", cart);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Método para calcular el total de la venta
    private BigDecimal calculateTotalSale() {
        return cart.stream()
                .map(detail -> detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Método para finalizar la venta y registrar los detalles
    @PostMapping("cart/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody Map<String, Object> paymentDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Crear la venta
            Sale sale = Sale.builder()
                    .dateSale(new Timestamp(System.currentTimeMillis()))
                    .totalSale(calculateTotalSale())
                    .payment(paymentImp.findById(Long.parseLong(paymentDetails.get("fkIdPayment").toString())))
                    .build();

            Sale savedSale = saleImp.create(sale);

            // Registrar los detalles asociados a la venta
            for (Detail detail : cart) {
                detail.setSale(savedSale);
                // Aquí llamas al método en tu servicio que maneja la persistencia de los detalles
                // detailImp.create(detail);
            }

            // Limpiar el carrito después de la venta
            cart.clear();

            response.put("status", "success");
            response.put("data", "Venta registrada exitosamente");

        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Método para cancelar el carrito
    @DeleteMapping("cart/cancel")
    public ResponseEntity<Map<String, Object>> cancelCart() {
        Map<String, Object> response = new HashMap<>();
        try {
            cart.clear();
            response.put("status", "success");
            response.put("data", "Carrito cancelado");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }






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
            response.put("paymentMethod", sale.getPayment());  // Añadir el método de pago
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_SALE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
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

    /*
    @PostMapping("add-detail")
    public ResponseEntity<Map<String, Object>> addDetailToSale(@RequestBody Map<String, Object> request) {
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
*/
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
