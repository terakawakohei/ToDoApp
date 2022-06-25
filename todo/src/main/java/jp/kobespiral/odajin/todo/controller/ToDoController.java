package jp.kobespiral.odajin.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.kobespiral.odajin.todo.service.ToDoService;

@Controller
public class ToDoController {

    @GetMapping("/")
    public String showLandingPage() {
        return "login";
    }

}
