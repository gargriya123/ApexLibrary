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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class TransactionController {
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
    }

    @GetMapping("/details")
    public String BookDetails(ModelMap model) {
        model.addAttribute("title", "details book");
        DetailsDto details = new DetailsDto();
        model.addAttribute("details", details);
        return "details";
    }

    @PostMapping("/details")
    public String issueDetails(@Valid @ModelAttribute("details") DetailsDto details, BindingResult result, ModelMap model, HttpSession session) {
        try {

            int userId = details.getUserId();
            int bookId = details.getBookId();

            User user = userRepo.findById(userId).get();
            Book book = bookRepo.findById(bookId).get();

            if(user != null && book != null) {

            model.addAttribute("user",user);
            model.addAttribute("book",book);

            IssueDto issueDto = new IssueDto();
            issueDto.setUserId(userId);
            issueDto.setBookId(bookId);
            model.addAttribute("issueDto", issueDto);

            session.setAttribute("user",user);
            session.setAttribute("book",book);
                System.out.println("first"+user.getId());
                System.out.println("first"+book.getId());

                return "issueBook";
            }
            else if(user ==null){
                session.setAttribute("msg9","User Not Available!!");
            }
            else{
                session.setAttribute("msg9","Book Not Available!!");
            }
            return "details";
        } catch(Exception e) {
            System.out.print(e.getMessage());
            session.setAttribute("msg9","Something went wrong!!");
            return "details";
        }
    }



    @GetMapping("/returnDetails")
    public String bookDetails(ModelMap model) {
        model.addAttribute("title", "details return book");
        DetailsDto detailsDto = new DetailsDto();
        model.addAttribute("detailsDto",detailsDto);
        return "returnDetails";
    }

    @PostMapping("/returnDetails")
    public String returnDetails(@Valid @ModelAttribute("detailsDto") DetailsDto detailsDto, BindingResult result, ModelMap model, HttpSession session) {
        try {
            int userId = detailsDto.getUserId();
            int bookId = detailsDto.getBookId();

            User user1 = userRepo.findById(userId).get();
            Book book1 = bookRepo.findById(bookId).get();

            if(user1 != null && book1 != null) {
                IssueBook issue1 = issueRepo.findByUserIdAndBookId(userId,bookId);
                if(issue1!=null && issue1.getStatus().equals("Pending")) {

                    model.addAttribute("user1", user1);
                    model.addAttribute("book1", book1);
                    model.addAttribute("issue1", issue1);

                    ReturnDto returnDto = new ReturnDto();
                    returnDto.setUserId(user1.getId());
                    returnDto.setName(user1.getFullName());
                    returnDto.setBookId(book1.getId());
                    returnDto.setTitle(book1.getTitle());
                    returnDto.setIssueId(issue1.getId());
                    returnDto.setIssueDate(issue1.getIssueDate());
                    returnDto.setReturnDate(issue1.getReturnDate());

                    model.addAttribute("returnDto", returnDto);

                    session.setAttribute("user1", user1);
                    session.setAttribute("book1", book1);
                    session.setAttribute("issue1", issue1);

                    return "returnBook";
                }
                else if(issue1!=null  && issue1.getStatus().equals("Returned")){
                    System.out.println("hello");
                    List<IssueBook> issues=issueRepo.findAll();
                    for(IssueBook issue:issues){
                        User user=issue.getUser();
                        Book book=issue.getBook();
                        int userid=user.getId();
                        int bookid=book.getId();
                        if(userid==userId && bookid==bookId && issue.getStatus().equals("Pending")){
                            model.addAttribute("user1", user);
                            model.addAttribute("book1", book);
                            model.addAttribute("issue1", issue);

                            ReturnDto returnDto = new ReturnDto();
                            returnDto.setUserId(user.getId());
                            returnDto.setName(user.getFullName());
                            returnDto.setBookId(book.getId());
                            returnDto.setTitle(book.getTitle());
                            returnDto.setIssueId(issue.getId());
                            returnDto.setIssueDate(issue.getIssueDate());
                            returnDto.setReturnDate(issue.getReturnDate());

                            model.addAttribute("returnDto", returnDto);

                            session.setAttribute("user1", user);
                            session.setAttribute("book1", book);
                            session.setAttribute("issue1", issue);

                            return "returnBook";
                        }
                    }
                }
                return "returnDetails";
            }
            else if(user1 ==null){
                session.setAttribute("msg10","User Not Available!!");
            }
            else if(book1 ==null){
                session.setAttribute("msg10","Book Not Available!!");
            }
            else{
                session.setAttribute("msg10","Book is not issued to this user!!");
            }
            return "returnDetails";
        } catch(Exception e) {
            System.out.print(e.getMessage());
            session.setAttribute("msg10","Something went wrong!!");
            return "returnDetails";
        }
    }

}