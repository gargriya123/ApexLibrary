package com.library.project.Controllers;


import com.library.project.Model.Librarian;
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
    private LibrarianService libService;

    @GetMapping("/")
    public String index(ModelMap model) {
        model.addAttribute("title", "Home-Apex Library");
        return "index";
    }
    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("title", "Register-Apex Library");
        model.addAttribute("librarian", new Librarian());
        return "register";
    }
    @GetMapping("/login")
    public String login(ModelMap model) {
        model.addAttribute("title", "Login-Apex Library");
        return "login";
    }

    @PostMapping("/do_register")
    public String addLibrarian(@Valid @ModelAttribute("librarian") Librarian lib, BindingResult result,@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, ModelMap model, HttpSession session) {

        try {
            if(result.hasErrors()){
                model.addAttribute("librarian",lib);
                return "register";
            }
            if (!agreement) {
                System.out.println("You have not agreed the terms and conditions");
                throw new Exception("You have not agreed the terms and conditions");
            }

            boolean value = libService.checkEmail(lib.getEmail());
            if (value) {
                session.setAttribute("msg", "Email id already Exists!!");
            } else {
                Librarian libr = libService.addLibrarian(lib);
                if (libr != null) {
                    model.addAttribute("librarian", new Librarian());
                    session.setAttribute("msg", "Register Successfully!!");
                }
                else {
                    session.setAttribute("msg", "Something error in server!!");
                }
            }
            return "register";
        } catch (Exception e) {
            model.addAttribute("librarian", lib);
            session.setAttribute("msg", "Something error in server!!");
            return "register";
        }

    }
@PostMapping("/do_login")
    public String login(@RequestParam("email") String email,@RequestParam("password") String password,ModelMap model,HttpSession session){
        Librarian lib = libService.findLogin(email,password);
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
