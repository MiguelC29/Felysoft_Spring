package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Author;
import com.felysoft.felysoftApp.services.imp.AuthorImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/api/author/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("*")
public class AuthorController {
    @Autowired
    private AuthorImp authorImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){
        Map<String,Object> response= new HashMap<>();

        try{
            List<Author> authorList = this.authorImp.findAll();
            response.put("status","success");
            response.put("data", authorList);
        }catch (Exception e){
            response.put("status",HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Author author = this.authorImp.findById(id);
            response.put("status", "success");
            response.put("data", author);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String,Object> request){
        Map<String,Object> response= new HashMap<>();

        try{
            //INSTANCIA DEL OBJETO AUTHOR
            Author author = new Author();
            //CAMPOS PROPIOS DE LA TABLA AUTHORS
            author.setName(request.get("name").toString());
            author.setNationality(request.get("nationality").toString());
            author.setDateBirth(Date.valueOf(request.get("dateBirth").toString()));
            author.setBiography(request.get("biography").toString());

            this.authorImp.create(author);

            response.put("status","success");
            response.put("data","Registro Exitoso");
        }catch (Exception e){
            response.put("status",HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Author author = this.authorImp.findById(id);

            author.setName(request.get("name").toString());
            author.setNationality(request.get("nationality").toString());
            author.setDateBirth(Date.valueOf(request.get("dateBirth").toString()));
            author.setBiography(request.get("biography").toString());

            this.authorImp.update(author);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Author author = this.authorImp.findById(id);
            authorImp.delete(author);

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