package com.example.controllers;

import com.example.db.DAO.DAOImpl;
import com.example.db.models.Purchase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PurchaseControllers {

    DAOImpl dao = new DAOImpl();

    @PostMapping("/purchases")
    public ResponseEntity<String> postPurchase(@RequestBody Purchase purchaseRequest){
        try {
            Purchase purchaseToSave = new Purchase(purchaseRequest.getTitle(), purchaseRequest.getQuantity());
            dao.createPurchase(purchaseToSave);
            return ResponseEntity.status(HttpStatus.CREATED).body("Purchase created successfully.");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create purchase.");
        }
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
    public ResponseEntity<String> putStatus(@PathVariable String title) {
        try {
            boolean updated = dao.putPurchaseStatus(title);
            if (updated) {
                return ResponseEntity.ok("Purchase status updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Purchase not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update status.");
        }
    }
}
