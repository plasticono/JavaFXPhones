package sample.phones;

import sample.Main;

import java.io.Serializable;

public class Samsung extends Phone implements Serializable {

    private float size;
    private String displaySize;
    private String CPU;
    private String GPU;
    private String RAM;
    private String storage;

    public Samsung(String name, String year, String os,
                   float size, String displaySize, String CPU, String GPU, String RAM, String storage) {
        super(name, year, os);
        this.size = size;
        this.displaySize = displaySize;
        this.CPU = CPU;
        this.GPU = GPU;
        this.RAM = RAM;
        this.storage = storage;
    }
    @Override
    public void say(){
        Main.print(name + " + " + year + " + " + os + " + " + size + " + " + displaySize + " + " + CPU + " + " + GPU + " + " + RAM + " + " + storage);
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(String displaySize) {
        this.displaySize = displaySize;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getGPU() {
        return GPU;
    }

    public void setGPU(String GPU) {
        this.GPU = GPU;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
