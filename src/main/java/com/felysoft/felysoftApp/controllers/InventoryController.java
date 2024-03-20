package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.*;
import com.felysoft.felysoftApp.services.imp.BookImp;
import com.felysoft.felysoftApp.services.imp.InventoryImp;
import com.felysoft.felysoftApp.services.imp.NoveltyInvImp;
import com.felysoft.felysoftApp.services.imp.ProductImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/inventory/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("*")
public class InventoryController {

    @Autowired
    private InventoryImp inventoryImp;
    @Autowired
    private ProductImp productImp;
    @Autowired
    private BookImp bookImp;
    @Autowired
    private NoveltyInvImp noveltyInvImp;

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

    /*
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO INVENTARIO
            Inventory inventory = new Inventory();

            // CAMPOS PROPIOS ENTIDAD INVENTARIO
            inventory.setStock(Integer.parseInt(request.get("stock").toString()));
            inventory.setState(Inventory.State.DISPONIBLE);
            // Configurar fechas de creación y actualización
            inventory.setDateRegister(new Timestamp(System.currentTimeMillis()));
            inventory.setLastModification(new Timestamp(System.currentTimeMillis()));

            // CAMPOS LLAVES FORANEAS
            // Verificar si la clave "fkIdProduct" está presente en el mapa y no es null
            if (request.containsKey("fkIdProduct") && request.get("fkIdProduct") != null) {
                Product product = productImp.findById(Long.parseLong(request.get("fkIdProduct").toString()));
                inventory.setProduct(product);
                inventory.setTypeInv(Inventory.TypeInv.PRODUCTOS);
            } else if (request.containsKey("fkIdBook") && request.get("fkIdBook") != null) {
                // Verificar si la clave "fkIdBook" está presente en el mapa y no es null
                Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
                inventory.setBook(book);
                inventory.setTypeInv(Inventory.TypeInv.LIBROS);
            }
            this.inventoryImp.create(inventory);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    */

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO INVENTARIO
            Inventory inventory = this.inventoryImp.findById(id);

            // CAMPOS PROPIOS ENTIDAD INVENTARIO
            inventory.setStock(Integer.parseInt(request.get("stock").toString()));
            inventory.setTypeInv(Inventory.TypeInv.valueOf(request.get("typeInv").toString().toUpperCase()));
            inventory.setState(Inventory.State.valueOf(request.get("state").toString().toUpperCase()));
            // Configurar fechas de creación y actualización
            inventory.setLastModification(new Timestamp(System.currentTimeMillis()));

            // CAMPOS LLAVES FORANEAS
            // Verificar si la clave "fkIdProduct" está presente en el mapa y no es null
            if (request.containsKey("fkIdProduct") && request.get("fkIdProduct") != null) {
                Product product = productImp.findById(Long.parseLong(request.get("fkIdProduct").toString()));
                inventory.setProduct(product);
            } else if (request.containsKey("fkIdBook") && request.get("fkIdBook") != null) {
                // Verificar si la clave "fkIdBook" está presente en el mapa y no es null
                Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
                inventory.setBook(book);
            }

            // Verificar si la clave "fkIdNovelty" está presente en el mapa y no es null
            if (request.containsKey("fkIdNovelty") && request.get("fkIdNovelty") != null) {
                NoveltyInv noveltyInv = noveltyInvImp.findById(Long.parseLong(request.get("fkIdNovelty").toString()));
                inventory.setNoveltyInv(noveltyInv);
            }

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
    }
}