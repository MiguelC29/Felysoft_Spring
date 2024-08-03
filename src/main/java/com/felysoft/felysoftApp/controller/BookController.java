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
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam("title") String title,
            @RequestParam("editorial") String editorial,
            @RequestParam("description") String description,
            @RequestParam("yearPublication") int yearPublication,
            @RequestParam("priceTime") BigDecimal priceTime,
            @RequestParam("author") Long authorId,
            @RequestParam("genre") Long genreId,
            @RequestParam("image") MultipartFile image
    ){
        Map<String, Object> response = new HashMap<>();

        try{
            //INSTANCIA DEL OBJETO BOOK
            Book.BookBuilder bookBuilder = Book.builder()
                    .title(title.toUpperCase())
                    .editorial(editorial.toUpperCase())
                    .description(description.toUpperCase())
                    .yearPublication(yearPublication)
                    .priceTime(priceTime);

            if (image != null){
                bookBuilder.nameImg(image.getOriginalFilename())
                        .typeImg(image.getContentType())
                        .image(image.getBytes());
            }
            //CAMPOS DE LA LLAVES FORANEAS
            Author author = authorImp.findById(authorId);

            Genre genre = genreImp.findById(genreId);

            Book book = bookBuilder
                    .author(author)
                    .genre(genre)
                    .build();

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
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "editorial",required = false) String editorial,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "yearPublication",required = false) int yearPublication,
            @RequestParam(value = "priceTime",required = false) BigDecimal priceTime,
            @RequestParam(value = "author",required = false) Long authorId,
            @RequestParam(value = "genre",required = false) Long genreId,
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
            if(editorial != null){
                book.setEditorial(editorial.toUpperCase());
            }
            if(description != null){
                book.setDescription(description.toUpperCase());
            }
            if(yearPublication != 0){
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
            if(inventory == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Inventario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            else {
                book.setEliminated(true);
                inventory.setEliminated(true);

                this.bookImp.delete(book);
                this.inventoryImp.delete(inventory);

                response.put("status", "success");
                response.put("data", "Eliminado Correctamente");
            }
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
