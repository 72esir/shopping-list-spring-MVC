package com.example.controllers;

import com.example.models.Purchase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostPurchase {
    @PostMapping("/purchases")
    public int postPurchase(@RequestBody Purchase purchase){
        return 0;
    }
}
