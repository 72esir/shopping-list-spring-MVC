package com.example.controllers;

import com.example.db.DAO.DAOImpl;
import com.example.db.models.Purchase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PurchaseControllers {

    DAOImpl dao = new DAOImpl();

    @PostMapping("/purchases")
    public void postPurchase(@RequestBody Purchase purchaseRequest){
        Purchase purchaseToSave = new Purchase(purchaseRequest.getTitle(), purchaseRequest.getQuantity());
        dao.createPurchase(purchaseToSave);
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<Purchase>> getList(){
        try {
            List<Purchase> purchases = dao.getPurchasesList();
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/purchases/{title}")
    public void putStatus(@PathVariable String title){
        dao.putPurchaseStatus(title);
    }
}
