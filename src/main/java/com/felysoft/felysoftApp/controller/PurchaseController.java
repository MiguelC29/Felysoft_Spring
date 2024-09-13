package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.imp.*;
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
@RequestMapping("/api/purchase/")
public class PurchaseController {
    @Autowired
    private PurchaseImp purchaseImp;

    @Autowired
    private DetailImp detailImp;

    @Autowired
    private InventoryImp inventoryImp;

    @Autowired
    private ProductImp productImp;

    @Autowired
    private ServiceImp serviceImp;

    @Autowired
    private BookImp bookImp;

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

    @PreAuthorize("hasAuthority('READ_ALL_PURCHASES_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Purchase> purchaseList = this.purchaseImp.findAllDisabled();

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

    @PreAuthorize("hasAuthority('CREATE_ONE_PURCHASE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PAGO
            Payment payment = Payment.builder()
                    .methodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()))
                    .state(Payment.State.valueOf(request.get("state").toString().toUpperCase()))
                    .total(new BigDecimal(request.get("total").toString()))
                    .date(new Timestamp(System.currentTimeMillis()))
                    .build();

            this.paymentImp.create(payment);

            // Construir el objeto Purchase usando el patrón Builder
            Purchase purchase = Purchase.builder()
                    .date(payment.getDate())
                    .total(payment.getTotal())
                    .provider(providerImp.findById(Long.parseLong(request.get("fkIdProvider").toString())))
                    .payment(payment)
                    .build();

            this.purchaseImp.create(purchase);

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

                    product.setSalePrice(new BigDecimal(detailRequest.get("salePrice").toString()));
                    productImp.update(product);

                    Inventory inventory = inventoryImp.findByProduct(product);

                    if (inventory == null) {
                        // Construir el objeto Inventory usando el patrón Builder
                        inventory = Inventory.builder()
                                .stock(cantidad)
                                .state((cantidad < 6 ? Inventory.State.BAJO : Inventory.State.DISPONIBLE))
                                .typeInv(Inventory.TypeInv.PRODUCTOS)
                                .dateRegister(new Timestamp(System.currentTimeMillis()))
                                .lastModification(new Timestamp(System.currentTimeMillis()))
                                .product(product)
                                .build();

                        this.inventoryImp.create(inventory); // Guardar inventario
                    } else {
                        inventory.setStock(inventory.getStock() + cantidad);
                        updateInventoryState(inventory);
                        inventoryImp.update(inventory);
                    }
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
                detail.setPurchase(purchase);

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

    private void updateInventoryState(Inventory inventory) {
        if (inventory.getStock() < 1) {
            inventory.setState(Inventory.State.AGOTADO);
        } else if (inventory.getStock() < 6) {
            inventory.setState(Inventory.State.BAJO);
        } else {
            inventory.setState(Inventory.State.DISPONIBLE);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Recuperar la compra existente
            Purchase purchase = purchaseImp.findById(id);
            if (purchase == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Compra no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Actualizar la información de la compra
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            purchase.setDate(currentTimestamp);
            purchase.setTotal(new BigDecimal(request.get("total").toString()));

            // No actualizar el proveedor; mantén el proveedor actual
            // purchase.setProvider(providerImp.findById(Long.parseLong(request.get("fkIdProvider").toString())));

            // Actualizar el pago
            Payment payment = purchase.getPayment();
            payment.setMethodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()));
            payment.setState(Payment.State.valueOf(request.get("state").toString().toUpperCase()));
            payment.setTotal(purchase.getTotal());
            payment.setDate(purchase.getDate());

            paymentImp.update(payment);
            purchaseImp.update(purchase);

            // Primero, eliminar los detalles antiguos y ajustar el stock
            for (Detail oldDetail : purchase.getDetails()) {
                if (oldDetail.getProduct() != null) {
                    Product oldProduct = oldDetail.getProduct();
                    Inventory inventory = inventoryImp.findByProduct(oldProduct);
                    int oldQuantity = oldDetail.getQuantity();
                    inventory.setStock(inventory.getStock() - oldQuantity);
                    updateInventoryState(inventory);
                    inventoryImp.update(inventory);
                }
            }

            // Actualizar o crear nuevos detalles de la compra
            List<Map<String, Object>> detailsRequest = (List<Map<String, Object>>) request.get("details");

            for (Map<String, Object> detailRequest : detailsRequest) {
                Detail detail = null;

                if (detailRequest.get("idDetail") != null) {
                    // Buscar el detalle en la base de datos usando el idDetail
                    Long detailId = Long.parseLong(detailRequest.get("idDetail").toString());
                    detail = detailImp.findById(detailId);

                    if (detail == null) {
                        response.put("status", HttpStatus.NOT_FOUND);
                        response.put("data", "Detalle no encontrado con id: " + detailId);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }
                } else {
                    // Si idDetail no está presente, crear un nuevo detalle
                    response.put("data", "Detalle no encontrado");
                }

                // Verificar si es un producto o un libro y actualizar en consecuencia
                if (detailRequest.get("idProduct") != null) {
                    Product product = productImp.findById(Long.parseLong(detailRequest.get("idProduct").toString()));
                    if (product == null) {
                        response.put("status", HttpStatus.NOT_FOUND);
                        response.put("data", "Producto no encontrado");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }

                    int newQuantity = Integer.parseInt(detailRequest.get("quantity").toString());
                    detail.setProduct(product);
                    detail.setQuantity(newQuantity);

                    // Ajustar el stock en función de la diferencia de cantidad
                    Inventory inventory = inventoryImp.findByProduct(product);
                    int oldQuantity = (detail.getIdDetail() != null) ? detailImp.findById(detail.getIdDetail()).getQuantity() : 0;
                    int difference = newQuantity - oldQuantity;
                    inventory.setStock(inventory.getStock() + difference);
                    updateInventoryState(inventory);
                    inventoryImp.update(inventory);
                } else if (detailRequest.get("idBook") != null) {
                    Book book = bookImp.findById(Long.parseLong(detailRequest.get("idBook").toString()));
                    if (book == null) {
                        response.put("status", HttpStatus.NOT_FOUND);
                        response.put("data", "Libro no encontrado");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }
                    detail.setBook(book);
                    detail.setQuantity(1); // La cantidad predeterminada para libros
                }

                // Actualizar el resto de los campos del detalle
                detail.setUnitPrice(new BigDecimal(detailRequest.get("unitPrice").toString()));
                detail.setEliminated(false);
                detail.setPurchase(purchase);

                // Actualizar el detalle en la base de datos
                if (detail.getIdDetail() != null) {
                    detailImp.update(detail);  // Actualizar detalle existente
                } else {
                    detailImp.create(detail);  // Crear nuevo detalle si no tiene id
                }
            }

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_PURCHASE_DISABLED')")
    @PutMapping("enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Purchase purchase = this.purchaseImp.findByIdDisabled(id);
            purchase.setEliminated(false);
            purchaseImp.update(purchase);

            response.put("status", "success");
            response.put("data", "Habilitado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_PURCHASE')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Purchase purchase = this.purchaseImp.findById(id);
            if (purchase == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Compra no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            purchase.setEliminated(true);
            this.purchaseImp.delete(purchase);

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
