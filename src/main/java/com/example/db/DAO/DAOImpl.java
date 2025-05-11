package com.example.db.DAO;

import com.example.db.models.Purchase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DAOImpl implements DAO{
    private final SessionFactory factory;

    public DAOImpl(){
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    @Override
    public void createPurchase(Purchase purchase) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.persist(purchase);
            session.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Create purchase error!");
            e.printStackTrace();
        }
    }

    @Override
    public List<Purchase> getPurchasesList() {
        List<Purchase> purchases = new ArrayList<>();
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            String hql = "FROM Purchase";
            Query<Purchase> query = session.createQuery(hql, Purchase.class);
            purchases = query.list();

            session.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Get purchases list error!");
            e.printStackTrace();
        }
        return purchases;
    }

    @Override
    public void putPurchaseStatus(String title) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            Purchase purchaseToUpdate = session.byNaturalId(Purchase.class)
                    .using("title", title)
                    .load();
            purchaseToUpdate.setBoughtStatus();

            session.getTransaction().commit();
        }catch (Exception e){
            System.err.println("Put purchase status error!");
            e.printStackTrace();
        }
    }
}
