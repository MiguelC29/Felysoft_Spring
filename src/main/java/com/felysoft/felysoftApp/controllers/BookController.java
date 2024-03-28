package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Author;
import com.felysoft.felysoftApp.entities.Book;
import com.felysoft.felysoftApp.entities.Genre;
import com.felysoft.felysoftApp.services.imp.AuthorImp;
import com.felysoft.felysoftApp.services.imp.BookImp;
import com.felysoft.felysoftApp.services.imp.GenreImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(path = "/api/book/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class BookController {
    @Autowired
    private BookImp bookImp;
    @Autowired
    private GenreImp genreImp;
    @Autowired
    private AuthorImp authorImp;
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){
        Map<String,Object> response= new HashMap<>();

        try{
            List<Book> bookList= this.bookImp.findAll();
            response.put("status","success");
            response.put("data", bookList);
        }catch (Exception e){
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
            Book book = this.bookImp.findById(id);
            response.put("status", "success");
            response.put("data", book);
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
            //INSTANCIA DEL OBJETO BOOK
            Book book = new Book();
            //CAMPOS PROPIOS DE LA TABLA BOOKS
            book.setTitle(request.get("title").toString().toUpperCase());
            book.setEditorial(request.get("editorial").toString().toUpperCase());
            book.setDescription(request.get("description").toString().toUpperCase());
            book.setYearPublication(Integer.parseInt(request.get("yearPublication").toString()));
            book.setPriceTime(new BigDecimal(request.get("priceTime").toString()));

            //CAMPOS DE LA LLAVES FORANEAS
            Author author = authorImp.findById(Long.parseLong(request.get("fkIdAuthor").toString()));
            book.setAuthor(author);

            Genre genre = genreImp.findById(Long.parseLong(request.get("fkIdGenre").toString()));
            book.setGenre(genre);

            this.bookImp.create(book);

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
            Book book = this.bookImp.findById(id);

            book.setTitle(request.get("title").toString().toUpperCase());
            book.setEditorial(request.get("editorial").toString().toUpperCase());
            book.setDescription(request.get("description").toString().toUpperCase());
            book.setYearPublication(Integer.parseInt(request.get("yearPublication").toString()));
            book.setPriceTime(new BigDecimal(request.get("priceTime").toString()));

            //CAMPOS DE LA LLAVES FORANEAS
            Author author = authorImp.findById(Long.parseLong(request.get("fkIdAuthor").toString()));
            book.setAuthor(author);

            Genre genre = genreImp.findById(Long.parseLong(request.get("fkIdGenre").toString()));
            book.setGenre(genre);

            this.bookImp.update(book);

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
            Book book = this.bookImp.findById(id);
            book.setEliminated(true);
            this.bookImp.delete(book);

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
