package app.mvc.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestionFichier {

    private static final String DOSSIER = "parties";

    public String getDossierSauvegarde() {
        return DOSSIER;
    }
    public void enregistrePartie(Partie p) throws IOException {
        
        String date = p.getDate().toString().replace("-", "_");
        String nomFichier = date + "_" + p.getJ1().getId() + "_" + p.getJ2().getId() + ".txt";

        File dossier = new File(DOSSIER);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }

        File fichier = new File(dossier, nomFichier);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichier))) {
            bw.write(String.valueOf(p.getJ1().getId()));  bw.newLine();
            bw.write(String.valueOf(p.getJ2().getId()));  bw.newLine();
            bw.write(String.valueOf(p.getJ1().getScore())); bw.newLine();
            bw.write(String.valueOf(p.getJ2().getScore())); bw.newLine();
            bw.write(String.valueOf(p.getLisCoupJ().size())); bw.newLine();
            for (Coup c : p.getLisCoupJ()) {
                bw.write(c.getCoupJ1() + ":" + c.getCoupJ2());
                bw.newLine();
            }
        }

        System.out.println("Partie enregistrée : " + fichier.getAbsolutePath());
    }

    public Partie lecture(String cheminFichier) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            int idJ1 = Integer.parseInt(br.readLine().trim());
            int idJ2 = Integer.parseInt(br.readLine().trim());
            int scoreJ1 = Integer.parseInt(br.readLine().trim());
            int scoreJ2 = Integer.parseInt(br.readLine().trim());
            int nbCoups = Integer.parseInt(br.readLine().trim());

            Joueur j1 = new Joueur(idJ1, "Joueur " + idJ1, scoreJ1);
            Joueur j2 = new Joueur(idJ2, "Joueur " + idJ2, scoreJ2);
            Partie partie = new Partie(j1, j2);

           
            for (int i = 0; i < nbCoups; i++) {
                String ligne = br.readLine();
                if (ligne != null && !ligne.isBlank()) {
                    String[] parts = ligne.trim().split(":");
                    int coupJ1 = Integer.parseInt(parts[0]);
                    Coup c = new Coup(coupJ1);
                    if (parts.length > 1 && !parts[1].isBlank()) {
                        c.setCoupJ2(Integer.parseInt(parts[1]));
                    }
                    partie.getLisCoupJ().add(c);
                }
            }

            String nomFichier = new File(cheminFichier).getName();
            String[] parts = nomFichier.replace(".txt", "").split("_");
            if (parts.length >= 3) {
                try {
                    LocalDate date = LocalDate.of(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                    );
                    partie.setDate(date);
                } catch (Exception ignored) {}
            }

            return partie;
        }
    }

    
    public List<String> listFile(String dossier) {
        List<String> liste = new ArrayList<>();
        File f = new File(dossier);
        if (!f.exists() || !f.isDirectory()) return liste;
        String[] fichiers = f.list((dir, name) -> name.endsWith(".txt"));
        if (fichiers != null) {
            for (String nom : fichiers) {
                liste.add(nom);
            }
        }
        return liste;
    }
}
