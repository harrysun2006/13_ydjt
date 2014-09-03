package com.free.misc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class GenericBindingInitializer implements WebBindingInitializer {

  public void initBinder(WebDataBinder binder, WebRequest request) {
    System.out.println("When to be executed???");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
  }
}