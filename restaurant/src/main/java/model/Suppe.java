package model;

public class Suppe extends Beilage { 
    public Suppe(Gericht gericht) { 
        super(gericht); 
    } 
    @Override
    public void druckeBeschreibung() { 
        gericht.druckeBeschreibung(); 
        System.out.print(", Suppe"); 
    } 
    @Override
    public double getPreis() { 
        return gericht.getPreis() + 1.50; 
    } 
} 
