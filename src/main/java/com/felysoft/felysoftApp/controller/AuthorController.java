package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Author;
import com.felysoft.felysoftApp.service.imp.AuthorImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/author/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class AuthorController {
    @Autowired
    private AuthorImp authorImp;

    @PreAuthorize("hasAuthority('READ_ALL_AUTHORS')")
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

    @PreAuthorize("hasAuthority('READ_ONE_AUTHOR')")
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

    @PreAuthorize("hasAuthority('READ_AUTHORS_BY_GENRE')")
    @GetMapping("authorsByGenre/{id}")
    public ResponseEntity<Map<String, Object>> findByIdGenre(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Author> authorList = this.authorImp.findByIdGenre(id);
            response.put("status", "success");
            response.put("data", authorList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_AUTHOR')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String,Object> request){
        Map<String,Object> response= new HashMap<>();

        try{
            //INSTANCIA DEL OBJETO AUTHOR
            Author author = Author.builder()
                    .name(request.get("name").toString().toUpperCase())
                    .nationality(request.get("nationality").toString().toUpperCase())
                    .dateBirth(Date.valueOf(request.get("dateBirth").toString()))
                    .biography(request.get("biography").toString().toUpperCase())
                    .build();

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_AUTHOR')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Author author = this.authorImp.findById(id);

            author.setName(request.get("name").toString().toUpperCase());
            author.setNationality(request.get("nationality").toString().toUpperCase());
            author.setDateBirth(new Date(new SimpleDateFormat("yyyy-MM-dd").parse((String) request.get("dateBirth")).getTime()));
            author.setBiography(request.get("biography").toString().toUpperCase());

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

    @PreAuthorize("hasAuthority('DISABLE_ONE_AUTHOR')")
    @PutMapping(value = "delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Author author = this.authorImp.findById(id);
            author.setEliminated(true);

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
