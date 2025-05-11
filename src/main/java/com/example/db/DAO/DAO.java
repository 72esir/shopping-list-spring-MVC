package com.example.db.DAO;

import com.example.db.models.Purchase;

import java.util.List;

public interface DAO {
    void createPurchase(Purchase purchase);
    List<Purchase> getPurchasesList();
    boolean putPurchaseStatus(String title);
}
