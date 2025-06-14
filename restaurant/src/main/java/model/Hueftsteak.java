package model;

public class Hueftsteak implements Gericht { 
    @Override
    public void druckeBeschreibung() { 
        System.out.print("Hüftsteak"); 
    } 
    @Override
    public double getPreis() { 
        return 13.0; 
    } 
} 