package com.metrix.webportal.validation;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import com.metrix.services.common.domains.MetrixException;

@ControllerAdvice
public class ControllerAdvise {
    
    @ExceptionHandler(MetrixException.class)
    public ModelAndView handleMetrixException(MetrixException e){
        return new ModelAndView("error", "ErrObject", e);
    }
}