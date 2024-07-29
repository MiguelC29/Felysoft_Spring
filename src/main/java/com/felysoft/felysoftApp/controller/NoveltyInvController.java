package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.NoveltyInv;
import com.felysoft.felysoftApp.service.imp.NoveltyInvImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/noveltyInv/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class NoveltyInvController {

    @Autowired
    private NoveltyInvImp noveltyInvImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<NoveltyInv> noveltyInvList = this.noveltyInvImp.findAll();

            response.put("status", "success");
            response.put("data", noveltyInvList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            NoveltyInv noveltyInv = this.noveltyInvImp.findById(id);

            response.put("status", "success");
            response.put("data", noveltyInv);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO NOVEDAD INVENTARIO
            NoveltyInv noveltyInv = NoveltyInv.builder()
                    .description(request.get("description").toString().toUpperCase())
                    .quantity(Integer.parseInt(request.get("quantity").toString()))
                    .date(LocalDateTime.parse(request.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();

            this.noveltyInvImp.create(noveltyInv);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO NOVEDAD INVENTARIO
            NoveltyInv noveltyInv = this.noveltyInvImp.findById(id);

            // CAMPOS PROPIOS ENTIDAD NOVEDAD INVENTARIO
            noveltyInv.setDescription(request.get("description").toString().toUpperCase());
            noveltyInv.setQuantity(Integer.parseInt(request.get("quantity").toString()));
            noveltyInv.setDate(LocalDateTime.parse((String) request.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            this.noveltyInvImp.update(noveltyInv);

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
            NoveltyInv noveltyInv = this.noveltyInvImp.findById(id);
            noveltyInv.setEliminated(true);

            noveltyInvImp.delete(noveltyInv);

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
