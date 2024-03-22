package com.library.project.Services;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {
    public void removeMessageFromSession(){
        try{
          HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
          session.removeAttribute("msg");
          session.removeAttribute("msg1");
          session.removeAttribute("msg2");
         session.removeAttribute("msg4");
            session.removeAttribute("msg5");
            session.removeAttribute("msg6");
            session.removeAttribute("msg7");
            session.removeAttribute("msg8");
            session.removeAttribute("msg9");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
