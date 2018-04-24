package com.diting.pingxingren.entity;

/**
 * Created by asus on 2017/9/15.
 */

public class PlayEatInfo {
    private String person;
    private String food;

    public PlayEatInfo(String person, String food) {
        this.person = person;
        this.food = food;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
