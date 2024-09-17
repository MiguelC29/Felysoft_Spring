package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Inventory;
import com.felysoft.felysoftApp.service.imp.InventoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory/")
public class InventoryController {

    @Autowired
    private InventoryImp inventoryImp;

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

    @PreAuthorize("hasAuthority('READ_INVENTORY_BOOKS')")
    @GetMapping("invBooksNoReserved")
    public ResponseEntity<Map<String, Object>> findInvBooksNoReserved() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Inventory> booksInventoryList = this.inventoryImp.findByTypeInvAndState(Inventory.TypeInv.LIBROS, Inventory.State.DISPONIBLE);

            response.put("status", "success");
            response.put("data", booksInventoryList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
