package com.library.project.Controllers;
import com.library.project.Model.Book;
import com.library.project.Model.User;
import com.library.project.Repositories.BookRepo;
import com.library.project.Repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/index")
    public String index(ModelMap model) {
        model.addAttribute("title", "Home-Apex Library");
        return "index1";
    }
    @GetMapping("/userLogin")
    public String login(ModelMap model) {
        model.addAttribute("title", "Login-Apex Library");
        return "userLogin";
    }
    @PostMapping("/userLogin")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap model, HttpSession session){
        User user=userRepo.findByEmailAndPassword(email,password);
        if(user !=null){
            model.addAttribute("user",user);
            session.setAttribute("msg11"," User Login Successfully!!");
            session.setAttribute("user",user);
            return "redirect:/userHome";
        }
        else{
            session.setAttribute("msg11","Failed to Login!!");
        }
        return "userLogin";
    }
    @PostMapping("/Logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();
        return "userLogin";
    }


}
