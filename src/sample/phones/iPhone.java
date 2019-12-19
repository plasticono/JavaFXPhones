package sample.phones;

import sample.Main;

import java.io.Serializable;

public class iPhone extends Phone implements Serializable {

    private String discontinued;
    private String ended;
    private int price;
    private String finalOs;

    private String minLifespan, maxLifespan;


    public iPhone(String name, String year, String os, String discontinued, String ended, String finalOs, String minLifespan, String maxLifespan,  int price) {
        super(name, year, os);
        this.discontinued = discontinued;
        this.ended = ended;
        this.price = price;
        this.finalOs = finalOs;
        this.minLifespan = minLifespan;
        this.maxLifespan = maxLifespan;
    }
    @Override
    public void say(){
        Main.print(name + " + " + year + " + " + os + " + " + discontinued + " + " + ended + " + " + finalOs + " + " + minLifespan + " + " + maxLifespan + " + " + price);
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(String discontinued) {
        this.discontinued = discontinued;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public String getFinalOs() {
        return finalOs;
    }

    public void setFinalOs(String finalOs) {
        this.finalOs = finalOs;
    }

    public String getMinLifespan() {
        return minLifespan;
    }

    public void setMinLifespan(String minLifespan) {
        this.minLifespan = minLifespan;
    }

    public String getMaxLifespan() {
        return maxLifespan;
    }

    public void setMaxLifespan(String maxLifespan) {
        this.maxLifespan = maxLifespan;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
