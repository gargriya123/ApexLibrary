package com.library.project.Controllers;

import com.library.project.Model.*;
import com.library.project.Repositories.BookRepo;
import com.library.project.Repositories.UserRepo;
import com.library.project.Services.BookService;
import com.library.project.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class LibrarianController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BookRepo bookRepo;
@ModelAttribute
    public void addCommonData(ModelMap model, HttpSession session){
      Librarian lib= (Librarian) session.getAttribute("value");
      if(lib !=null) {
          String name = lib.getFullName();
          model.addAttribute("librarian",lib);
          System.out.println(lib.getFullName());
      }

    }
    @GetMapping("/home")
    public String homePage(ModelMap model){
        model.addAttribute("title","HomePage");
        return "home";
    }
    // User Controller
    @GetMapping("/addUser")
  public String addUser(ModelMap model){
  model.addAttribute("title","Add User");
  UserDto userDto=new UserDto();
  model.addAttribute("userDto",userDto);
  return "addUser";
  }


    @PostMapping("/addUser")
    public String processUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result,ModelMap model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                return "addUser";
            }
            String email=userDto.getEmail();
            User user=new User();
            user.setEmail(email);
           boolean value= userRepo.existsByEmail(user.getEmail());
            if (value) {
                session.setAttribute("msg2", "User Already Exists");
            } else {
                user.setFullName(userDto.getFullName());
                user.setEmail(userDto.getEmail());
                user.setAddress(userDto.getAddress());
                user.setContact(userDto.getContact());
                user.setPassword(userDto.getPassword());
                user.setJoinBatch(userDto.getJoinBatch());
                userRepo.save(user);
                session.setAttribute("msg2", "User Added Successfully");
            }
            return "redirect:/addUser";
        } catch (Exception e) {
          System.out.println(e.getMessage());
            session.setAttribute("msg2", "Something error in server!!");
            return "addUser";
        }
    }
@GetMapping("/viewUsers")
public String listUser(ModelMap model,HttpSession session){
    List<User> users=userRepo.findAll();
    if(users!=null) {
        model.addAttribute("title", "viewUsers-Apex Library");
        model.addAttribute("users", users);
        session.setAttribute("value2",users);
        return "viewUsers";
    }
    return "addUser";
}
    @GetMapping("/updateUser")
    public String update(ModelMap model){
        model.addAttribute("title","update User");
        return "updateUser";
    }
    @GetMapping("/update/{id}")
    public String updateValue(@PathVariable("id") int id,ModelMap model){
        try {
            User user = userRepo.findById(id).get();
                model.addAttribute("user", user);
                model.addAttribute("id",id);

                UserDto userDto = new UserDto();
            userDto.setFullName(user.getFullName());
            userDto.setEmail(user.getEmail());
            userDto.setAddress(user.getAddress());
            userDto.setContact(user.getContact());
            userDto.setPassword(user.getPassword());
            userDto.setJoinBatch(user.getJoinBatch());
            System.out.println(userDto.getFullName());
            System.out.println(userDto.getEmail());
            model.addAttribute("userDto",userDto);
            System.out.println("-----------------------------------------------------");
            System.out.println(user.getEmail());
            System.out.println(userDto.toString());
            return "updateUser";
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return "redirect:/viewUsers";
        }
    }


    @PostMapping("/update/{id}")
    public String update_user(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, @PathVariable("id") int id, ModelMap model, HttpSession session) {
        try {
            User user = userRepo.findById(id).get();
            model.addAttribute("user", user);
            model.addAttribute("id",id);
            System.out.println("7"+user.toString());
            if (result.hasErrors()) {
                return "updateUser";
            }
        System.out.println(user.toString());
                user.setFullName(userDto.getFullName());
                user.setEmail(userDto.getEmail());
                user.setAddress(userDto.getAddress());
                user.setContact(userDto.getContact());
                user.setPassword(userDto.getPassword());
                user.setJoinBatch(userDto.getJoinBatch());
                userRepo.save(user);
            session.setAttribute("msg4", "User Updated Successfully!!");
                return "redirect:/viewUsers";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.setAttribute("msg4", "Something error in server try again!!");
            return "addUser";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id,ModelMap model,HttpSession session){
        User user = userRepo.findById(id).get();
        System.out.println(user.toString());
        try {
            userRepo.delete(user);
            session.setAttribute("msg7", " User Deleted Successfully!!");
            return "redirect:/viewUsers";
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            session.setAttribute("msg7", " Something went wrong!!");
            return "redirect:/viewUsers";
        }
    }

    //Book controller

 @GetMapping("/addBook")
    public String addNewBook(ModelMap model){
        BookDto bookdto = new BookDto();
        model.addAttribute("bookdto",bookdto);
        return "addBook";
    }
    @PostMapping("/addBook")
    public String addBookNew(@Valid @ModelAttribute("bookdto") BookDto bookdto,BindingResult result,ModelMap model,HttpSession session){
      try{
          if(result.hasErrors()){
              return "addBook";
          }
          String isbn=bookdto.getIsbn();
          Book book=new Book();
          book.setIsbn(isbn);
         boolean value=bookRepo.existsByIsbn(book.getIsbn());
          if(value){
              session.setAttribute("msg5","Book Already Present!!");
          }
          else{
              book.setIsbn(bookdto.getIsbn());
              book.setTitle(bookdto.getTitle());
              book.setAuthor(bookdto.getAuthor());
              book.setEdition(bookdto.getEdition());
              book.setPrice(bookdto.getPrice());
              book.setQuantity(bookdto.getQuantity());
               bookRepo.save(book);
              session.setAttribute("msg5","Book Added Successfully!!");

          }
          return "redirect:/addBook";
      }
      catch(Exception e){
          session.setAttribute("msg5","Something wrong in the server!!!!!");
          return "addBook";
      }
    }

    @GetMapping("/viewBooks")
    public String viewAllBooks(ModelMap model,HttpSession session){
         List<Book> books=bookRepo.findAll();
         if(books!=null){
             model.addAttribute("title","view books");
             model.addAttribute("books",books);
             session.setAttribute("allbooks",books);
             return "viewBooks";
         }
         return "addBook";
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") int id,ModelMap model,HttpSession session) {
        try {
            Book book = bookRepo.findById(id).get();
            bookRepo.delete(book);
            session.setAttribute("msg8", "Book deleted Successfully!!");
            return "redirect:/viewBooks";
        } catch (Exception e) {
              System.out.println(e.getMessage());
            session.setAttribute("msg8", "Something went wrong!!");
            return "redirect:/viewBooks";
        }
    }
    @GetMapping("/updateBook")
    public String updateBook(ModelMap model){
        model.addAttribute("title","update Book");
        return "updateBook";
    }

    @GetMapping("/updateBook/{id}")
    public String updateValue1(@PathVariable("id") int id,ModelMap model){
        try {
            Book book = bookRepo.findById(id).get();
                model.addAttribute("book", book);
                model.addAttribute("id",id);

                BookDto bookDto = new BookDto();
                bookDto.setIsbn(book.getIsbn());
                bookDto.setTitle(book.getTitle());
                bookDto.setAuthor(book.getAuthor());
                bookDto.setEdition(book.getEdition());
                bookDto.setPrice(book.getPrice());
                bookDto.setQuantity(book.getQuantity());

                model.addAttribute("bookDto", bookDto);

            return "updateBook";
        }
        catch(Exception e){
            System.out.println("Exception:"+e.getMessage());
            return "viewBooks";
        }

    }


    @PostMapping("/updateBook/{id}")
    public String update_book(@Valid @ModelAttribute("bookDto") BookDto bookDto,BindingResult result, @PathVariable("id") int id, ModelMap model, HttpSession session) {
        try {
            Book book = bookRepo.findById(id).get();
            model.addAttribute("book",book);
            if(result.hasErrors()){
                return "updateBook";
            }
            book.setIsbn(bookDto.getIsbn());
                book.setTitle(bookDto.getTitle());
                book.setAuthor(bookDto.getAuthor());
                book.setEdition(bookDto.getEdition());
                book.setPrice(bookDto.getPrice());
                book.setQuantity(bookDto.getQuantity());
                bookRepo.save(book);
                session.setAttribute("msg6","Book Update Successfully!!");
            return "redirect:/viewBooks";
        } catch (Exception e) {
            System.out.println("Exception:"+e.getMessage());
            session.setAttribute("msg6","Something went wrong!!");
            return "addBook";
        }

    }
}
