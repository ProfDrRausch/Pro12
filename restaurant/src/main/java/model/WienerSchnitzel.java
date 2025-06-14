package model;

public class WienerSchnitzel implements Gericht { 
    @Override
    public void druckeBeschreibung() { 
        System.out.print("WienerSchnitzel"); 
    } 
    @Override
    public double getPreis() { 
        return 10.50; 
    } 
} 