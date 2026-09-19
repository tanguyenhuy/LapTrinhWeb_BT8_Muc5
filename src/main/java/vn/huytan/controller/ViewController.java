package vn.huytan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/admin/category-ajax")
    public String categoryAjax() {
        return "admin/category-ajax";
    }

    @GetMapping("/admin/product-ajax")
    public String productAjax() {
        return "admin/product-ajax";
    }
    
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }
}