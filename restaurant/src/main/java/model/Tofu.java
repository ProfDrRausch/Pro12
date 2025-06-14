package model;

public class Tofu implements Gericht { 
    @Override
    public void druckeBeschreibung() { 
        System.out.print("Tofu"); 
    } 
    @Override
    public double getPreis() { 
        return 8.50; 
    } 
} 