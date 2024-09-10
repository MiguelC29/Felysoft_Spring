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

    @Autowired
    private DetailImp detailImp;

    @Autowired
    private InventoryImp inventoryImp;

    @Autowired
    private ProductImp productImp;

    @Autowired
    private BookImp bookImp;

    //CARRITO
    // Lista temporal para almacenar los detalles del carrito
    private List<Detail> cart = new ArrayList<>();

    // Método para agregar productos al carrito
    @PostMapping("cart/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody List<Detail> details) {
        Map<String, Object> response = new HashMap<>();
        try {
            cart.addAll(details);
            response.put("status", "success");
            response.put("data", cart);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY.toString());
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
            response.put("status", HttpStatus.BAD_GATEWAY.toString());
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private BigDecimal calculateTotalSale() {
        return cart.stream()
                .map(detail -> detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @PostMapping("cart/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Verificar si el carrito está vacío
            if (cart == null || cart.isEmpty()) {
                response.put("status", HttpStatus.BAD_REQUEST.toString());
                response.put("data", "El carrito está vacío.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Crear el pago
            Payment payment = Payment.builder()
                    .methodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()))
                    .state(Payment.State.valueOf(request.get("state").toString().toUpperCase()))
                    .total(new BigDecimal(request.get("total").toString())) // Total provisto desde la solicitud
                    .date(new Timestamp(System.currentTimeMillis()))
                    .build();

            this.paymentImp.create(payment); // Guardar el pago en la base de datos

            // Crear la venta
            Sale sale = Sale.builder()
                    .dateSale(payment.getDate())
                    .totalSale(payment.getTotal())
                    .payment(payment) // Asociar el pago con la venta
                    .build();

            this.saleImp.create(sale); // Guardar la venta en la base de datos

            // Registrar los detalles de la compra
            List<Map<String, Object>> detailsRequest = (List<Map<String, Object>>) request.get("details");

            for (Map<String, Object> detailRequest : detailsRequest) {
                Detail detail = new Detail();

                if (detailRequest.get("idProduct") != null) {
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
                    inventory.setStock(inventory.getStock() - cantidad);
                    if(inventory.getStock() < 1) {
                        inventory.setState(Inventory.State.AGOTADO);
                    } else {
                        if(inventory.getStock() < 6) {
                            inventory.setState(Inventory.State.BAJO);
                        } else {
                            inventory.setState(Inventory.State.DISPONIBLE);
                        }
                    }
                    inventoryImp.update(inventory);
                }
                else if (detailRequest.get("idBook") != null) {
                    Book book = bookImp.findById(Long.parseLong(detailRequest.get("idBook").toString()));
                    if (book == null) {
                        response.put("status", HttpStatus.NOT_FOUND);
                        response.put("data", "Libro no encontrado");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }
                    // Si es un libro, solo el precio unitario es requerido
                    detail.setBook(book);
                    detail.setQuantity(1);  // Establecemos una cantidad predeterminada para los libros
                }

                detail.setUnitPrice(new BigDecimal(detailRequest.get("unitPrice").toString()));
                detail.setEliminated(false);
                detail.setSale(sale);

                this.detailImp.create(detail);  // Registrar cada detalle en la base de datos
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

    @DeleteMapping("cart/cancel")
    public ResponseEntity<Map<String, Object>> cancelCart() {
        Map<String, Object> response = new HashMap<>();
        try {
            if (cart.isEmpty()) {
                response.put("status", "info");
                response.put("data", "El carrito ya está vacío");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            // Limpiar el carrito
            cart.clear();

            // Preparar la respuesta
            response.put("status", "success");
            response.put("data", "Carrito cancelado");

        } catch (Exception e) {
            // Manejar cualquier excepción y preparar la respuesta de error
            response.put("status", HttpStatus.BAD_GATEWAY.toString());
            response.put("data", "Error al cancelar el carrito: " + e.getMessage());
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
