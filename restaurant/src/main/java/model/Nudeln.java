package model;

public class Nudeln extends Beilage { 
    public Nudeln(Gericht gericht) { 
        super(gericht); 
    } 
    @Override
    public void druckeBeschreibung() { 
        gericht.druckeBeschreibung(); 
        System.out.print(", Nudeln"); 
    } 
    @Override
    public double getPreis() { 
        return gericht.getPreis() + 4.50; 
    } 
} 