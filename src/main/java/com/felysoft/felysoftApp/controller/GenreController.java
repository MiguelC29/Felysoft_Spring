package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.entity.Genre;
import com.felysoft.felysoftApp.service.imp.GenreImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/genre/")
public class GenreController {
    @Autowired
    private GenreImp genreImp;

    @PreAuthorize("hasAuthority('READ_ALL_GENRES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){
        Map<String,Object> response= new HashMap<>();
        try{
            List<Genre> genreList= this.genreImp.findAll();

            response.put("status","success");
            response.put("data", genreList);
        }catch (Exception e){
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ALL_GENRES_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled(){
        Map<String,Object> response= new HashMap<>();
        try{
            List<Genre> genreList= this.genreImp.findAllDisabled();

            response.put("status","success");
            response.put("data", genreList);
        }catch (Exception e){
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ONE_GENRE')")
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Genre genre = this.genreImp.findById(id);

            response.put("status", "success");
            response.put("data", genre);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_GENRE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String,Object> request){
        Map<String,Object> response= new HashMap<>();

        try{
            final boolean isAdmin = AuthenticationRequest.isAdmin();
            //INSTANCIA DEL OBJETO GENRE
            Genre genre = this.genreImp.findGenreByNameAndEliminated(request.get("name").toString().toUpperCase());

            if(genre!=null){
                response.put("status",HttpStatus.BAD_GATEWAY);
                response.put("data","Datos Desahibilitados");

                String message = (isAdmin) ? "Información ya registrada pero desahibilitada" : "Información ya registrada pero desahibilitada; Contacte al Administrador";

                response.put("detail",message);

                return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

            }else {
                Genre newgenre = Genre.builder()
                        .name(request.get("name").toString().toUpperCase())
                        .description(request.get("description").toString().toUpperCase())
                        .build();

                this.genreImp.create(newgenre);
                response.put("status","success");
                response.put("data","Registro Exitoso");
            }

        }catch (Exception e){
            response.put("status",HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_GENRE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Genre genre = this.genreImp.findById(id);
            genre.setName(request.get("name").toString().toUpperCase());
            genre.setDescription(request.get("description").toString().toUpperCase());

            this.genreImp.update(genre);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_GENRE_DISABLED')")
    @PutMapping("enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Genre genre = this.genreImp.findByIdDisabled(id);
            genre.setEliminated(false);

            this.genreImp.update(genre);

            response.put("status", "success");
            response.put("data", "Habilitado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_GENRE')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Genre genre = this.genreImp.findById(id);
            genre.setEliminated(true);

            this.genreImp.delete(genre);

            response.put("status", "success");
            response.put("data", "Eliminado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //ASOCIATION GENRES - AUTHOR
    @PreAuthorize("hasAuthority('READ_GENRES_BY_AUTHOR')")
    @GetMapping("genresByAuthor/{id}")
    public ResponseEntity<Map<String, Object>> findByIdAuthor(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Genre> genreList = this.genreImp.findByIdAuthor(id);

            response.put("status", "success");
            response.put("data", genreList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ASSOCIATE_GENRE_AUTHOR')")
    @PostMapping("add-author")
    public ResponseEntity<Map<String, Object>> addAuthorToGenre(@RequestBody Map<String,Object> request){
        Map<String,Object> response= new HashMap<>();

        try{
            Long genreId = Long.parseLong(request.get("genreId").toString());
            Long authorId = Long.parseLong(request.get("authorId").toString());

            //Si la Asociación es existente
            boolean associationExists = this.genreImp.checkAssociationExists(genreId,authorId);
            if(associationExists){
                response.put("associationType", "genre-author");
                response.put("entity1", "género");
                response.put("entity2", "autor");
                throw new RuntimeException("Asociación existente");
            }

            this.genreImp.addAuthorToGenre(genreId,authorId);

            response.put("status","success");
            response.put("data","Asociacion Exitosa");
        }catch (Exception e){
            response.put("status",HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
