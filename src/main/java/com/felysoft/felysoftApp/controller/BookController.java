package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Author;
import com.felysoft.felysoftApp.entity.Book;
import com.felysoft.felysoftApp.entity.Genre;
import com.felysoft.felysoftApp.entity.Inventory;
import com.felysoft.felysoftApp.service.imp.AuthorImp;
import com.felysoft.felysoftApp.service.imp.BookImp;
import com.felysoft.felysoftApp.service.imp.GenreImp;
import com.felysoft.felysoftApp.service.imp.InventoryImp;
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
@RequestMapping(path = "/api/book/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class BookController {
    @Autowired
    private BookImp bookImp;

    @Autowired
    private GenreImp genreImp;

    @Autowired
    private AuthorImp authorImp;

    @Autowired
    private InventoryImp inventoryImp;

    @PreAuthorize("hasAuthority('READ_ALL_BOOKS')")
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

    @PreAuthorize("hasAuthority('READ_ONE_BOOK')")
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

    @PreAuthorize("hasAuthority('CREATE_ONE_BOOK')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String,Object> request){
        Map<String,Object> response= new HashMap<>();

        try{
            //INSTANCIA DEL OBJETO BOOK
            Book book = Book.builder()
                    .title(request.get("title").toString().toUpperCase())
                    .editorial(request.get("editorial").toString().toUpperCase())
                    .description(request.get("description").toString().toUpperCase())
                    .yearPublication(Integer.parseInt(request.get("yearPublication").toString()))
                    .priceTime(new BigDecimal(request.get("priceTime").toString()))
                    .build();

            //CAMPOS DE LA LLAVES FORANEAS
            Author author = authorImp.findById(Long.parseLong(request.get("fkIdAuthor").toString()));
            book.setAuthor(author);

            Genre genre = genreImp.findById(Long.parseLong(request.get("fkIdGenre").toString()));
            book.setGenre(genre);

            this.bookImp.create(book);

            //INSTANCIA OBJETO INVENTARIO
            Inventory inventory = Inventory.builder()
                    .stock(1)
                    .state(Inventory.State.DISPONIBLE)
                    .typeInv(Inventory.TypeInv.LIBROS)
                    // Configurar fechas de creación y actualización
                    .dateRegister(new Timestamp(System.currentTimeMillis()))
                    .lastModification(new Timestamp(System.currentTimeMillis()))
                    //CAMPOS LLAVES FORANEAS
                    .book(book)
                    .build();

            this.inventoryImp.create(inventory);

            response.put("status","success");
            response.put("data","Registro Exitoso");
        }catch (Exception e){
            response.put("status",HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_BOOK')")
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

    @PreAuthorize("hasAuthority('DISABLE_ONE_BOOK')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Book book = this.bookImp.findById(id);
            Inventory inventory= this.inventoryImp.findByBook(book);

            book.setEliminated(true);
            inventory.setEliminated(true);

            this.bookImp.delete(book);
            this.inventoryImp.delete(inventory);

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
