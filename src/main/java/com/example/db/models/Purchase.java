package com.example.models;

public class Purchase {
    private final String title;
    private final int count;
    private Status status;

    public Purchase(String title, int count){
        this.count = count;
        this.title = title;
        status = Status.NOT_BOUGHT;
    }

    public void setBoughtStatus(){
        status = Status.BOUGHT;
    }
}
