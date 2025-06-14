package model;

public class Garnelen implements Gericht { 
    @Override
    public void druckeBeschreibung() { 
        System.out.print("Garnelen"); 
    } 
    @Override
    public double getPreis() { 
        return 13.50; 
    } 

} 
