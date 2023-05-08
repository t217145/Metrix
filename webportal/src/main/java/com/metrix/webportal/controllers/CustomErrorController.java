package com.metrix.webportal.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.metrix.services.common.domains.MetrixException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    
    @GetMapping("/customError")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        MetrixException err = new MetrixException();

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                err = new MetrixException(404, "Page not found!", "/");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                err = new MetrixException(500, "Server error!", "/");
            }
        } 
        
        if(err.getCustomErrMsg() == null || err.getCustomErrMsg().isEmpty()) {
            err = new MetrixException(-1, "Unknown Error Occur!", "/");
        }
        return new ModelAndView("error", "ErrObject", err);
    } 
}
