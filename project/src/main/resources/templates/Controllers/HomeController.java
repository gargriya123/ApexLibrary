package com.library.project.Controllers;


import com.library.project.Model.Librarian;
import com.library.project.Model.LibrarianDto;
import com.library.project.Repositories.LibrarianRepo;
import com.library.project.Services.LibrarianService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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


@Controller
public class HomeController {
    @Autowired
    private LibrarianRepo libRepo;

    @GetMapping("/")
    public String index(ModelMap model) {
        model.addAttribute("title", "Home-Apex Library");
        return "index";
    }
    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("title", "Register-Apex Library");
        LibrarianDto librarian=new LibrarianDto();
        model.addAttribute("librarian",librarian);
        return "register";
    }

    @PostMapping("/register")
    public String addLibrarian(@Valid @ModelAttribute("librarian") LibrarianDto librarian, BindingResult result , ModelMap model, HttpSession session) {

        try {
            String email=librarian.getEmail();
            boolean value = libRepo.existsByEmail(email);
            if (value) {
                session.setAttribute("msg", "Email id already Exists!!");
            } else {
              Librarian lib=new Librarian();
              lib.setFullName(librarian.getFullName());
              lib.setEmail(librarian.getEmail());
              lib.setAddress(librarian.getAddress());
              lib.setContact(librarian.getAddress());
              lib.setPassword(librarian.getPassword());
              System.out.println(lib);
              libRepo.save(lib);
              session.setAttribute("msg", "Register Successfully!!");
                return "register";
            }
            return "register";
        } catch (Exception e) {
           System.out.println(e.getMessage());
            session.setAttribute("msg", "Something error in server!!");
            return "register";
        }

    }
    @GetMapping("/login")
    public String login(ModelMap model) {
        model.addAttribute("title", "Login-Apex Library");
        return "login";
    }
@PostMapping("/login")
    public String login(@RequestParam("email") String email,@RequestParam("password") String password,ModelMap model,HttpSession session){
        Librarian lib = libRepo.findByEmailAndPassword(email,password);
        if(lib !=null){
            model.addAttribute("librarian",lib);
            session.setAttribute("msg1","Login Successfully!!");
            session.setAttribute("value",lib);
            return "home";
        }
        else{
            session.setAttribute("msg1","Failed to Login!!");
        }
        return "login";
    }
@PostMapping("/logout")
public String logout(HttpServletRequest request, HttpServletResponse response){
    request.getSession().invalidate();
    return "login";
}

}
