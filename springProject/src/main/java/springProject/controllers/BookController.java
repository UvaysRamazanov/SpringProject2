package springProject.controllers;

import com.example.demo.models.Book;
import com.example.demo.models.Person;
import com.example.demo.services.BookService;
import com.example.demo.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springProject.models.Book;
import springProject.services.BookService;
import springProject.services.PeopleService;

import javax.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String showAll(Model model, @RequestParam(value = "page",required = false) Integer page,
                          @RequestParam(value = "books_per_page",required = false) Integer books_per_page,
                          @RequestParam(value = "sort_by_year", required = false) boolean sortByYear){

        if((page==null) || (books_per_page==null)) {
            model.addAttribute("books", bookService.findAll(sortByYear));
        } else {
            model.addAttribute("books", bookService.findAll(page, books_per_page, sortByYear));
        }
        return "book/showAll";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id")int id, @ModelAttribute("person") Person person){
        model.addAttribute("book",bookService.findOne(id));
        Person bookOwner = bookService.getBookOwner(id);
        if(bookOwner!=null){
            model.addAttribute("owner",bookOwner);
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "book/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "book/new";
        } else {
            bookService.save(book);
            return "redirect:/book";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,@PathVariable("id")int id){
        model.addAttribute("book",bookService.findOne(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,@PathVariable("id") int id){
        if(bindingResult.hasErrors()) {
            return "book/edit";
        } else{
            bookService.update(id,book);
            return "redirect:/book";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id")int id){
        bookService.release(id);
        return "redirect:/book/"+id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id")int id,@ModelAttribute("person") Person selectedPerson){
        bookService.assign(id,selectedPerson);
        return "redirect:/book/"+id;
    }

    @GetMapping("/search")
    public String search(){
        return "book/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model,@RequestParam() String query){
        model.addAttribute("books",bookService.searchByName(query));
        return "book/search";
    }
}