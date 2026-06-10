package app.mvc.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Scanner;


public class Joueur implements Comparable<Joueur> {

    private final IntegerProperty id    = new SimpleIntegerProperty();
    private final StringProperty  nom   = new SimpleStringProperty();
    private final IntegerProperty score = new SimpleIntegerProperty();

    public Joueur(int id, String nom, int score) {
        this.id.set(id);
        this.nom.set(nom);
        this.score.set(score);
    }

    
    public Joueur(String nom) {
        this(0, nom, 0);
    }

    public IntegerProperty idProperty()    { return id; }
    public StringProperty  nomProperty()   { return nom; }
    public IntegerProperty scoreProperty() { return score; }

    
    public int    getId()    
    {
    	return id.get(); 
    }
    public String getNom() {
    	return nom.get(); 
    }
    public int    getScore() {
    	return score.get(); 
    }

    public void setId(int id) {
    	this.id.set(id); 
    }
    public void setNom(String nom)  {
    	this.nom.set(nom); 
    }
    public void setScore(int score) {
    	this.score.set(score); 
    }

    public void incrementerScore() {
    	this.score.set(this.score.get() + 1);
    	}
    public void decrementerScore() { 
    	this.score.set(this.score.get() - 1); 
    }

    
    public int choisirCoup() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisir une colonne (1-7) : ");
        while (!scanner.hasNextInt()) {
            System.out.print("Entrez un nombre entier : ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    @Override
    public int compareTo(Joueur j) {
        return Integer.compare(this.getScore(), j.getScore());
    }

    @Override
    public String toString() {
        return getNom() + " (score=" + getScore() + ")";
    }
}
