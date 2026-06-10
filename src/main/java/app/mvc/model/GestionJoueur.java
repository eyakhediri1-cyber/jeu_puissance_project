package app.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GestionJoueur {
    private List<Joueur> list = new ArrayList<>();

    public void remplirList() {
        list.add(new Joueur(1, "Joueur 01", 10));
        list.add(new Joueur(2, "Joueur 02", 20));
        list.add(new Joueur(3, "Joueur 03", 30));
        list.add(new Joueur(4, "Joueur 04", 10));
        list.add(new Joueur(5, "Joueur 05", 20));
        list.add(new Joueur(6, "Joueur 06", 30));
    }

    public List<Joueur> listJoueurTrieByScore() {
        Collections.sort(list); 
        return list;
    }
    public List<Joueur> listJoueurTrieByNom() {
        Collections.sort(this.list, new Comparator<Joueur>() {
            @Override
            public int compare(Joueur j1, Joueur j2) {
                return j1.getNom().compareTo(j2.getNom());
            }
        });
        return this.list;
    }

    public List<Joueur> listJoueurTrieById() {
        this.list.sort((j1, j2) -> Integer.compare(j1.getId(), j2.getId()));
        return this.list;
    }
    public List<Joueur> listFiltreByNom(String sousChaine) {
        return list.stream().filter(j -> j.getNom().contains(sousChaine)).collect(Collectors.toList());
    }

    public List<Joueur> listFiltreByScore(int minScore) {
        return list.stream().filter(j -> j.getScore() > minScore).collect(Collectors.toList());
    }
    public List<Joueur> getList() {
         return list; 
    }
}
