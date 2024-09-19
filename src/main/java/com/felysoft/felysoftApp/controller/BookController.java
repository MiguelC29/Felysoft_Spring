package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.imp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/book/")
public class BookController {
    @Autowired
    private BookImp bookImp;

    @Autowired
    private GenreImp genreImp;

    @Autowired
    private AuthorImp authorImp;

    @Autowired
    private InventoryImp inventoryImp;

    @Autowired
    private EditorialImp editorialImp;

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
    @PreAuthorize("hasAuthority('READ_ALL_BOOKS_IN_INVENTORY')")
    @GetMapping("inInventory")
    public ResponseEntity<Map<String, Object>> findAllInInventory() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Book> booksInInventory = this.bookImp.findBooksInInventory();
            response.put("data", booksInInventory);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ALL_BOOKS_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled(){
        Map<String,Object> response= new HashMap<>();

        try{
            List<Book> bookList= this.bookImp.findAllDisabled();
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
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("yearPublication") Year yearPublication,
            @RequestParam("author") Long authorId,
            @RequestParam("genre") Long genreId,
            @RequestParam("editorial") Long editorialId,
            @RequestParam("image") MultipartFile image
    ){
        Map<String, Object> response = new HashMap<>();

        try{
            final boolean isAdmin = AuthenticationRequest.isAdmin();

            Book bookByTitle = this.bookImp.findBookByTitleAndEliminated(title.toUpperCase());
            if(bookByTitle != null){
                response.put("status",HttpStatus.BAD_GATEWAY);
                response.put("data","Datos Desahibilitados");

                // Verifica si el rol es de administrador
                String message = (isAdmin) ? "Información ya registrada pero desahibilitada" : "Información ya registrada pero desahibilitada; Contacte al Administrador";

                response.put("detail", message);

                return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
            }else{
            //INSTANCIA DEL OBJETO BOOK
            Book.BookBuilder bookBuilder = Book.builder()
                    .title(title.toUpperCase())
                    .description(description.toUpperCase())
                    .yearPublication(yearPublication)
                    .priceTime(new BigDecimal(0));

            if (image != null){
                bookBuilder.nameImg(image.getOriginalFilename())
                        .typeImg(image.getContentType())
                        .image(image.getBytes());
            }
            //CAMPOS DE LA LLAVES FORANEAS
            Author author = authorImp.findById(authorId);

            Genre genre = genreImp.findById(genreId);

            Editorial editorial = editorialImp.findById(editorialId);

            Book book = bookBuilder
                    .author(author)
                    .genre(genre)
                    .editorial(editorial)
                    .build();

            this.bookImp.create(book);

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_BOOK')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "yearPublication",required = false) Year yearPublication,
            @RequestParam(value = "priceTime",required = false) BigDecimal priceTime,
            @RequestParam(value = "author",required = false) Long authorId,
            @RequestParam(value = "genre",required = false) Long genreId,
            @RequestParam(value = "editorial",required = false) Long editorialId,
            @RequestParam(value = "image",required = false) MultipartFile image
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Book book = this.bookImp.findById(id);

            if(book == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Libro no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if(title != null){
                book.setTitle(title.toUpperCase());
            }

            if(description != null){
                book.setDescription(description.toUpperCase());
            }
            if(yearPublication != null){
                book.setYearPublication(yearPublication);
            }
            if(priceTime != null){
                book.setPriceTime(priceTime);
            }

            //CAMPOS DE LA LLAVES FORANEAS
            if(authorId != null){
                Author author = authorImp.findById(authorId);
                if(author == null){
                    response.put("status", HttpStatus.NOT_FOUND);
                    response.put("data", "Autor no encontrado");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                book.setAuthor(author);
            }
            if(genreId != null){
                Genre genre = genreImp.findById(genreId);
                if(genre == null){
                    response.put("status", HttpStatus.NOT_FOUND);
                    response.put("data", "Género no encontrado");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                book.setGenre(genre);
            }
            if(editorialId != null){
                Editorial editorial = editorialImp.findById(editorialId);
                if(editorial == null){
                    response.put("status", HttpStatus.NOT_FOUND);
                    response.put("data", "Editorial no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                book.setEditorial(editorial);
            }

            if (image != null){
                book.setNameImg(image.getOriginalFilename());
                book.setTypeImg(image.getContentType());
                book.setImage(image.getBytes());
            }
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

    @PreAuthorize("hasAuthority('UPDATE_ONE_BOOK_DISABLED')")
    @PutMapping("enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Book book = this.bookImp.findByIdDisabled(id);
            if(book == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Libro no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Inventory inventory= this.inventoryImp.findByBookDisable(book);
            if(inventory != null){
                //response.put("status", HttpStatus.NOT_FOUND);
                //response.put("data", "Inventario no encontrado");
                //return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                inventory.setEliminated(false);

                this.inventoryImp.update(inventory);
            }
                book.setEliminated(false);

                this.bookImp.update(book);

                response.put("status", "success");
                response.put("data", "Habilitado Correctamente");

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
            if(book == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Libro no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Inventory inventory= this.inventoryImp.findByBook(book);
            if(inventory != null){
                //response.put("status", HttpStatus.NOT_FOUND);
                //response.put("data", "Inventario no encontrado");
                //return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                inventory.setEliminated(true);

                this.inventoryImp.delete(inventory);
            }
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

    // LIST PRODUCTS - PROVIDER
    @PreAuthorize("hasAuthority('READ_BOOKS_BY_EDITORIAL')")
    @GetMapping("booksByEditorial/{id}")
    public ResponseEntity<Map<String, Object>> findByIdEditorial(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Book> bookList = this.bookImp.findByIdEditorial(id);
            response.put("status", "success");
            response.put("data", bookList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
