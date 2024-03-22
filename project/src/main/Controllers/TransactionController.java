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
            System.out.println(lib.getFullName());
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
            if(result.hasErrors()) {
                return "details";
            }
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
            return "details";
        } catch(Exception e) {
            System.out.print(e.getMessage());
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
            if(result.hasErrors()) {
                return "returnDetails";
            }
            int userId = detailsDto.getUserId();
            int bookId = detailsDto.getBookId();

            User user1 = userRepo.findById(userId).get();
            Book book1 = bookRepo.findById(bookId).get();
            IssueBook issue1 = issueRepo.findByUserIdAndBookId(userId,bookId);

            int issueId=issue1.getId();

            if(user1 != null && book1 != null && issue1!=null) {

                model.addAttribute("user1",user1);
                model.addAttribute("book1",book1);
                model.addAttribute("issue1",issue1);

                ReturnDto returnDto=new ReturnDto();
                  returnDto.setUserId(user1.getId());
                  returnDto.setName(user1.getFullName());
                  returnDto.setBookId(book1.getId());
                  returnDto.setTitle(book1.getTitle());
                  returnDto.setIssueId(issue1.getId());
                  returnDto.setIssueDate(issue1.getIssueDate());
                  returnDto.setReturnDate(issue1.getReturnDate());

                  model.addAttribute("returnDto", returnDto);

                session.setAttribute("user1",user1);
                session.setAttribute("book1",book1);
                session.setAttribute("issue1",issue1);


                System.out.println("return"+user1.getId());
                System.out.println("return"+book1.getId());
                System.out.println("return"+issue1.getId());
                System.out.println(returnDto.getIssueDate());
                System.out.println(returnDto.getReturnDate());

                return "returnBook";
            }
            return "returnDetails";
        } catch(Exception e) {
            System.out.print(e.getMessage());
            return "returnDetails";
        }
    }

}