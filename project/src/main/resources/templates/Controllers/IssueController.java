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
import java.util.List;
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

    }

    @PostMapping("/issueBook")
    public String checkDetails(@Valid @ModelAttribute("issueDto") IssueDto issueDto, BindingResult result, ModelMap model, HttpSession session) {
        try {
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
                    book.setQuantity(book.getQuantity() - 1);
                    bookRepo.save(book);
                } else {
                    session.setAttribute("msg9", "This book is not available!!");
                    return "details";
                }

                IssueBook issue = new IssueBook();
                issue.setUser(user);
                issue.setBook(book);
                issue.setIssueDate(issueDto.getIssueDate());
                issue.setReturnDate(issueDto.getReturnDate());
                issue.setStatus("Pending");
                issueRepo.save(issue);
                session.setAttribute("msg9", "Book Issued Successfully");
                return "redirect:/details";
            }
            else if(issueRepo.existsByUserIdAndBookId(userId, bookId)){
                IssueBook issue=issueRepo.findByUserIdAndBookId(userId,bookId);
               if(issue.getStatus().equals("Returned")){
                   if (book.getQuantity() > 0) {
                       book.setQuantity(book.getQuantity() - 1);
                       bookRepo.save(book);
                   } else {
                       session.setAttribute("msg9", "This book is not available!!");
                       return "details";
                   }

                   IssueBook issue1 = new IssueBook();
                   issue1.setUser(user);
                   issue1.setBook(book);
                   issue1.setIssueDate(issueDto.getIssueDate());
                   issue1.setReturnDate(issueDto.getReturnDate());
                   issue1.setStatus("Pending");
                   issueRepo.save(issue1);
                   session.setAttribute("msg9", "Book Issued Successfully");
                   return "redirect:/details";
                }
               else if(issue.getStatus().equals("Pending")){
                   System.out.println("issued");
                   session.setAttribute("msg9", "Book Already Issued to this User");
                   return "redirect:/details";
               }
            }
            else{
                session.setAttribute("msg9", "This book is not available!!");
            }
            return "details";
        } catch(Exception e) {
            System.out.println(e.getMessage());
            session.setAttribute("msg9","Something went wrong!!");
            return "details";
        }
    }
    @GetMapping("/viewIssuedBooks")
    public String viewAllIssuedBooks(ModelMap model,HttpSession session,@RequestParam(defaultValue = "") LocalDate date1,@RequestParam(defaultValue = "") LocalDate date2,@RequestParam(defaultValue="") String status){
        List<IssueBook> issues=null;
        if(date1 !=null && date2 !=null && status.equals("Pending")){
            issues=issueRepo.findByIssueDateAndReturnDateAndStatus(date1,date2,status);
        }
      else{
          issues=issueRepo.findAll();
        }
        if(issues!=null){
            model.addAttribute("title","view Issued books");
            model.addAttribute("issues",issues);
            session.setAttribute("issues",issues);
            return "viewIssuedBooks";
        }
        return "issueBook";
    }
    @PostMapping("/returnBook")
    public String returnbook1(@Valid @ModelAttribute("returnDto") ReturnDto returnDto,BindingResult result,ModelMap model,HttpSession session){
        try{
            int userId= returnDto.getUserId();
            int bookId=returnDto.getBookId();
            int issueId=returnDto.getIssueId();

            User user=userRepo.findById(userId).get();
            Book book=bookRepo.findById(bookId).get();
            IssueBook issue=issueRepo.findById(issueId).get();
            LocalDate returnDate=issue.getReturnDate();
            LocalDate actualDate=returnDto.getActualReturnDate();

            if(user != null && book!=null && issue!=null){
                int fineValue = 0;
                boolean fine=false;
                LocalDate actualDate1 = LocalDate.of(2024, 3, 31);
                long diff = ChronoUnit.DAYS.between(returnDate,actualDate1);

                if (diff > 0) {
                    fineValue = (int) diff * 10;
                    fine=true;

                }
                ReturnBook returnValue=new ReturnBook();
                returnValue.setIssue(issue);
                returnValue.setActualReturnDate(returnDto.getActualReturnDate());
                returnValue.setFineValue(fineValue);
                returnValue.setFine(fine);
                if(fine){
                    ReturnDto fineDto=new ReturnDto();
                    fineDto.setUserId(userId);
                    fineDto.setName(user.getFullName());
                    fineDto.setBookId(bookId);
                    fineDto.setTitle(book.getTitle());
                    fineDto.setIssueId(issueId);
                    fineDto.setFineValue(fineValue);
                    fineDto.setActualReturnDate(actualDate1);
                    session.setAttribute("finedto",fineDto);
                    model.addAttribute("fineDto",fineDto);
                    return "fine";
                }
                returnValue.setFinePaid(true);
                returnRepo.save(returnValue);
                book.setQuantity(book.getQuantity()+1);
                bookRepo.save(book);
               issue.setStatus("Returned");
               issueRepo.save(issue);
                session.setAttribute("msg10","Book Returned Successfully!!");
                return "redirect:/returnDetails";
            }
            else if(user==null){
                session.setAttribute("msg10","User Not Available!!");
            }
            else if(book==null){
                session.setAttribute("msg10","Book Not Available!!");
            }
            else{
                session.setAttribute("msg10","This book is not issued to this user!!");
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
        ReturnDto obj=(ReturnDto)session.getAttribute("finedto");
        try{
           int issueId=obj.getIssueId();
            IssueBook issue=issueRepo.findById(issueId).get();
            Book book=bookRepo.findById(fineDto.getBookId()).get();
                ReturnBook bookReturn=new ReturnBook();
                bookReturn.setIssue(issue);
                bookReturn.setActualReturnDate(obj.getActualReturnDate());
                bookReturn.setFine(true);
                bookReturn.setFineValue(fineDto.getFineValue());
                bookReturn.setFinePaid(true);
                returnRepo.save(bookReturn);

                book.setQuantity(book.getQuantity()+1);
                bookRepo.save(book);
            issue.setStatus("Returned");
            issueRepo.save(issue);

                session.setAttribute("msg10","Fine Paid!!! " +
                        "Book Returned Successfully!!");
                return "redirect:/returnDetails";
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            session.setAttribute("msg10","Something went wrong");
            return "returnDetails";
        }
    }

    @GetMapping("/viewReturnedBooks")
    public String viewAllReturnedBooks(ModelMap model,HttpSession session,@RequestParam(defaultValue = "") LocalDate date){
        List<ReturnBook> returns=null;
        if(date!=null){
            returns=returnRepo.findByActualReturnDate(date);
        }
        else{
            returns=returnRepo.findAll();
        }
        if(returns!=null){
            model.addAttribute("title","view returned books");
            model.addAttribute("returns",returns);
            session.setAttribute("returns",returns);
            return "viewReturnedBooks";
        }
        return "returnBook";
    }
}
