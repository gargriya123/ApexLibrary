package com.library.project.Controllers;

import com.library.project.Model.*;
import com.library.project.Repositories.BookRepo;
import com.library.project.Repositories.IssueRepo;
import com.library.project.Repositories.ReturnRepo;
import com.library.project.Repositories.UserRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
public class IssueController {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private IssueRepo issueRepo;
    @Autowired
    private ReturnRepo returnRepo;
    @ModelAttribute
    public void addCommonData(ModelMap model, HttpSession session) {
        Librarian lib = (Librarian) session.getAttribute("value");
        if (lib != null) {
            String name = lib.getFullName();
            model.addAttribute("librarian", lib);
            System.out.println(lib.getFullName());
        }

       User user=(User)session.getAttribute("user");
        Book book=(Book)session.getAttribute("book");
        User user1=(User)session.getAttribute("user1");
        Book book1=(Book)session.getAttribute("book1");
        IssueBook issue1=(IssueBook)session.getAttribute("issue1");

        if(user!= null && book!=null){
            model.addAttribute("user",user);
            model.addAttribute("book",book);

        }
        if(user1!=null && book1!=null && issue1!=null){
            model.addAttribute("user1",user1);
            model.addAttribute("book1",book1);
            model.addAttribute("issue1",issue1);
        }
        System.out.println("2"+user);
        System.out.println("2"+book);
        System.out.println("2"+user1);
        System.out.println("2"+book1);
        System.out.println("2"+issue1);
    }

    @PostMapping("/issueBook")
    public String checkDetails(@Valid @ModelAttribute("issueDto") IssueDto issueDto, BindingResult result, ModelMap model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                return "issueBook";
            }

            int userId = issueDto.getUserId();
            User user = userRepo.findById(userId).get();
            int bookId = issueDto.getBookId();
            Book book = bookRepo.findById(bookId).get();

            if (user == null) {
                session.setAttribute("msg9", "User not found!!");
                return "details";
            }
            if (book != null && issueRepo.existsByUserIdAndBookId(userId, bookId)==false) {
                if (book.getQuantity() > 0) {
                    System.out.println(book.getQuantity());
                    book.setQuantity(book.getQuantity() - 1);
                    bookRepo.save(book);
                    System.out.println(book.getQuantity());
                } else {
                    session.setAttribute("msg9", "This book is not available!!");
                    return "details";
                }

                IssueBook issue = new IssueBook();
                issue.setUser(user);
                issue.setBook(book);
                issue.setIssueDate(issueDto.getIssueDate());
                issue.setReturnDate(issueDto.getReturnDate());

                issueRepo.save(issue);
                System.out.println(issue.toString());
                session.setAttribute("msg9", "Book Issued Successfully");
                return "redirect:/details";
            }
            return "details";
        } catch(Exception e) {
            session.setAttribute("msg9","Something went wrong!!");
            return "details";
        }
    }
    @PostMapping("/returnBook")
    public String returnbook1(@Valid @ModelAttribute("returnDto") ReturnDto returnDto,BindingResult result,ModelMap model,HttpSession session){
        System.out.println("hello");
        try{
           System.out.println("hello");
            int userId= returnDto.getUserId();
            int bookId=returnDto.getBookId();
            int issueId=returnDto.getIssueId();

            System.out.println(userId);
            System.out.println(bookId);
           System.out.println(issueId);
            User user=userRepo.findById(userId).get();
            Book book=bookRepo.findById(bookId).get();
            IssueBook issue=issueRepo.findById(issueId).get();
            LocalDate returnDate=issue.getReturnDate();
            LocalDate actualDate=returnDto.getActualReturnDate();
            System.out.println(user);
            System.out.println(book);
            System.out.println(issue);
            System.out.println(issue.getReturnDate());
            System.out.println(returnDto.getActualReturnDate());
            if(user != null && book!=null && issue!=null){
                int fineValue = 0;
                boolean fine=false;
                LocalDate actualDate1 = LocalDate.of(2024, 3, 31);
                long diff = ChronoUnit.DAYS.between(returnDate,actualDate1);
                System.out.println(diff);
                if (diff > 0) {
                    fineValue = (int) diff * 10;
                    fine=true;
                    System.out.println(fineValue);
                }
                ReturnBook returnValue=new ReturnBook();
                returnValue.setIssue(issue);
                returnValue.setActualReturnDate(returnDto.getActualReturnDate());
                returnValue.setFineValue(fineValue);
                returnValue.setFine(fine);
                if(fine){
                    System.out.println("hi");
                    ReturnDto fineDto=new ReturnDto();
                    System.out.println("hi");
                    System.out.println(returnDto);
                    fineDto.setUserId(userId);
                    fineDto.setName(user.getFullName());
                    fineDto.setBookId(bookId);
                    fineDto.setTitle(book.getTitle());
                    fineDto.setIssueId(issueId);
                    fineDto.setFineValue(fineValue);
                    fineDto.setActualReturnDate(actualDate1);
                    System.out.println(fineDto.toString());
                    session.setAttribute("finedto",fineDto);
                    model.addAttribute("fineDto",fineDto);
                    return "fine";
                }
                returnValue.setFinePaid(true);
                returnRepo.save(returnValue);
              System.out.println(returnValue.toString());
                book.setQuantity(book.getQuantity()+1);
                bookRepo.save(book);
                session.setAttribute("msg10","Book Returned Successfully!!");
                return "dummy";
            }
            return "returnDetails";
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            session.setAttribute("msg10","Something went wrong");
            return "returnDetails";
        }
    }
    @PostMapping("/fine")
    public String finedetails(@Valid @ModelAttribute("fineDto") ReturnDto fineDto,BindingResult result,ModelMap model,HttpSession session){
        System.out.println("hello");
        ReturnDto obj=(ReturnDto)session.getAttribute("finedto");
        System.out.println(obj);
        try{
           int issueId=obj.getIssueId();
           System.out.println(issueId);
            IssueBook issue=issueRepo.findById(issueId).get();
            Book book=bookRepo.findById(fineDto.getBookId()).get();
                ReturnBook bookReturn=new ReturnBook();
                bookReturn.setIssue(issue);
                bookReturn.setActualReturnDate(obj.getActualReturnDate());
                bookReturn.setFine(true);
                bookReturn.setFineValue(fineDto.getFineValue());
                bookReturn.setFinePaid(true);
                returnRepo.save(bookReturn);
             System.out.println(bookReturn.toString());
             System.out.println(book.getQuantity());
                book.setQuantity(book.getQuantity()+1);
                bookRepo.save(book);
            System.out.println(book.getQuantity());
                session.setAttribute("msg11","Book Returned Successfully!!");
                return "dummy";

        }
        catch(Exception e){
            System.out.println(e.getMessage());
            session.setAttribute("msg11","Something went wrong");
            return "returnDetails";
        }
    }


}
