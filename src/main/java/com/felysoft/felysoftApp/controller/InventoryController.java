package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Inventory;
//import com.felysoft.felysoftApp.service.imp.BookImp;
import com.felysoft.felysoftApp.service.imp.InventoryImp;
//import com.felysoft.felysoftApp.service.imp.NoveltyInvImp;
//import com.felysoft.felysoftApp.service.imp.ProductImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/inventory/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class InventoryController {

    @Autowired
    private InventoryImp inventoryImp;

    /*
    @Autowired
    private ProductImp productImp;

    @Autowired
    private BookImp bookImp;

    @Autowired
    private NoveltyInvImp noveltyInvImp;*/

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Inventory> inventoryList = this.inventoryImp.findAll();

            response.put("status", "success");
            response.put("data", inventoryList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_INVENTORY_PRODUCTS')")
    @GetMapping("inventoryProducts")
    public ResponseEntity<Map<String, Object>> findInventoryProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Inventory> productsInventoryList = this.inventoryImp.findByTypeInv(Inventory.TypeInv.PRODUCTOS);

            response.put("status", "success");
            response.put("data", productsInventoryList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_INVENTORY_BOOKS')")
    @GetMapping("inventoryBooks")
    public ResponseEntity<Map<String, Object>> findInventoryBooks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Inventory> booksInventoryList = this.inventoryImp.findByTypeInv(Inventory.TypeInv.LIBROS);

            response.put("status", "success");
            response.put("data", booksInventoryList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("updateStock/{id}")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO INVENTARIO
            Inventory inventory = this.inventoryImp.findById(id);

            int newStock = inventory.getStock() + Integer.parseInt(request.get("stock").toString());

            // CAMPOS PROPIOS ENTIDAD INVENTARIO
            inventory.setStock(newStock);

            if(inventory.getStock() < 1) {
                inventory.setState(Inventory.State.AGOTADO);
            } else {
                if(inventory.getStock() < 6) {
                    inventory.setState(Inventory.State.BAJO);
                } else {
                    inventory.setState(Inventory.State.DISPONIBLE);
                }
            }
            // Configurar fechas de creación y actualización
            inventory.setLastModification(new Timestamp(System.currentTimeMillis()));

            this.inventoryImp.update(inventory);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("resetStock/{id}")
    public ResponseEntity<Map<String, Object>> resetStock(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO INVENTARIO
            Inventory inventory = this.inventoryImp.findById(id);

            // CAMPOS PROPIOS ENTIDAD INVENTARIO
            inventory.setStock(Integer.parseInt(request.get("stock").toString()));

            if(inventory.getStock() < 1) {
                inventory.setState(Inventory.State.AGOTADO);
            } else {
                if(inventory.getStock() < 6) {
                    inventory.setState(Inventory.State.BAJO);
                } else {
                    inventory.setState(Inventory.State.DISPONIBLE);
                }
            }

            // Configurar fechas de creación y actualización
            inventory.setLastModification(new Timestamp(System.currentTimeMillis()));

            this.inventoryImp.update(inventory);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Inventory inventory = this.inventoryImp.findById(id);
            inventory.setEliminated(true);

            inventoryImp.delete(inventory);

            response.put("status", "success");
            response.put("data", "Eliminado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }*/
}
