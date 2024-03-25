package com.library.project.Controllers;
import com.library.project.Model.*;
import com.library.project.Repositories.BookRepo;
import com.library.project.Repositories.IssueRepo;
import com.library.project.Repositories.UserRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class UserFunctionsController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private IssueRepo issueRepo;

    @ModelAttribute
    public void addCommonData(ModelMap model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user!= null) {
            String name = user.getFullName();
            model.addAttribute("user", user);
            System.out.println(name);
        }
    }
    //view Books
    @GetMapping("/userViewBooks")
    public String viewAllBooks(ModelMap model, HttpSession session, @RequestParam(defaultValue = "") String keyword){

        List<Book> books=null;
        if(keyword.equals("")) {
            books=bookRepo.findAll();
        }
        else {
            books = bookRepo.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword,keyword);
        }
        if (books != null) {
            model.addAttribute("title", "view books");
            model.addAttribute("books", books);
            model.addAttribute("keyword",keyword);
            session.setAttribute("allbooks", books);
            return "userViewBooks";
        }
        return "userViewBooks";
    }
    @GetMapping("/userIssuedBooks")
    public String viewAllIssuedBooks(ModelMap model, HttpSession session){
        List<IssueBook> issues=null;
        User user = (User) session.getAttribute("user");

            issues=issueRepo.findByUser(user);

        if(issues!=null){
            model.addAttribute("title","view Issued books");
            model.addAttribute("issues",issues);
            session.setAttribute("issues",issues);
            return "userIssuedBooks";
        }
        return "userIssuedBooks";
    }

    //feedback

    @GetMapping("/addFeedback/{id}")
    public String feedback(@PathVariable("id") int id, ModelMap model){
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

            return "addFeedback";
        }
        catch(Exception e){
            System.out.println("Exception:"+e.getMessage());
            return "userIssuedBooks";
        }

    }


    @PostMapping("/addFeedback/{id}")
    public String addFeedback(@Valid @ModelAttribute("bookDto") BookDto bookDto, BindingResult result, @PathVariable("id") int id, ModelMap model, HttpSession session) {
        try {
            System.out.println(bookDto.getFeedback());
            Book book = bookRepo.findById(id).get();
            model.addAttribute("book",book);

            book.setIsbn(bookDto.getIsbn());
            book.setTitle(bookDto.getTitle());
            book.setAuthor(bookDto.getAuthor());
            book.setEdition(bookDto.getEdition());
            book.setPrice(bookDto.getPrice());
            book.setQuantity(bookDto.getQuantity());
            book.setFeedback(bookDto.getFeedback());
            bookRepo.save(book);
            session.setAttribute("msg12","Feedback Added Successfully!!");
            return "redirect:/userIssuedBooks";
        } catch (Exception e) {
            System.out.println("Exception:"+e.getMessage());
            session.setAttribute("msg12","Something went wrong!!");
            return "userIssuedBooks";
        }

    }

   //user
    @GetMapping("/userHome")
    public String message(ModelMap model,HttpSession session){
        User user=(User)session.getAttribute("user");
        List<IssueBook> books=issueRepo.findByUser(user);
      //  System.out.println(books);
        for(IssueBook issue:books){
            LocalDate date=issue.getReturnDate();
            LocalDate today=LocalDate.now();
          //  System.out.println(date+" "+today);
         //   System.out.println(date.isEqual(today));
            if(date.isEqual(today) && issue.getStatus().equals("Pending")){
              //  System.out.println("return");
                session.setAttribute("msg13","Please Returned Issued Books!!");
                return "userHome";
            }
        }
        return "userHome";
    }
    @GetMapping("/userProfile")
    public String profile1(ModelMap model,HttpSession session){
        User user= (User) session.getAttribute("user");
        UserDto userDto=new UserDto();
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setContact(user.getContact());
        userDto.setPassword(user.getPassword());
        model.addAttribute("userDto",userDto);
        model.addAttribute("user",user);
        return "userprofile";
    }
    @PostMapping("/userProfile/{id}")
    public String setProfile1(@Valid @ModelAttribute ("userDto") UserDto userDto, BindingResult result, @PathVariable("id") int id, ModelMap model, HttpSession session){
        try{
            model.addAttribute("userDto",userDto);
            User user1=new User();
            user1.setId(id);
            user1.setFullName(userDto.getFullName());
            user1.setEmail(userDto.getEmail());
            user1.setAddress(userDto.getAddress());
            user1.setContact(userDto.getContact());
            user1.setPassword(userDto.getPassword());
            userRepo.save(user1);
            session.setAttribute("msg15","Profile update Successfully!!");
            return "redirect:/userLogin";
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            session.setAttribute("msg15","Something went wrong");
            return "userProfile";
        }
    }

}
