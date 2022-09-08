package com.portfolio.dev.thesisinfomgmt.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("/")
  public String helloWorld() {
    return "Hello World!";
  }
}
