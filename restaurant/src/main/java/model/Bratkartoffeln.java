package model;

public class Bratkartoffeln extends Beilage { 
    public Bratkartoffeln(Gericht gericht) { 
        super(gericht); 
    } 
    @Override
    public void druckeBeschreibung() { 
        gericht.druckeBeschreibung(); 
        System.out.print(", Bratkartoffeln"); 
    } 
    @Override
    public double getPreis() { 
        return gericht.getPreis() + 1.50; 
    } 
}  