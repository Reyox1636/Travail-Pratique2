import ca.qc.bdeb.sim202.tp2.DePipe;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Case[] rnd = new Case[2];
        tourmenu(new DePipe(), rnd, new Joueur(""));
        menu();
    }

    public static void menu() {
        Scanner sc = new Scanner(System.in);
        // Entrer le choix du joueur
        System.out.println("**Montrealopoly**");
        System.out.println("1- Jouer une nouvelle partie.");
        System.out.println("2- Charger une partie précédente.");
        System.out.println("3- Quitter le jeu.");
        int choix = 1;
        while (choix != 1 && choix != 2 && choix != 3) {
            System.out.println("Veuillez saisir une entrée valide");
            choix = sc.nextInt();
        }
        //
        switch (choix) {
            case 1:
                System.out.println("Entrez le nom du fichier binaire.");
                String nomfichier = "src/plateau.bin";//sc.next();
                //Vérification du nom du fichier
//                if (!nomfichier.endsWith(".bin")) {
//                    nomfichier = nomfichier + ".bin";
//                }
//                if (!nomfichier.startsWith("src/")) {
//                    nomfichier = "src/" + nomfichier;
//                }
                jouer("src/plateau.bin");

                break;
            case 2:
                break;
            case 3:
                break;
        }

    }

    public static void jouer(String nomfichier) {
        //Initialiser le plateau
        Case[] plateau = initialiserTableau(nomfichier);

        //Initialiser les joueurs
        LinkedList<Joueur> liste = listeDeJoueurs();
        while (true) {
            tourmenu(new DePipe(), plateau, liste.get(0));
            liste.add(liste.get(0));
            liste.remove(0);
        }
    }
    public static Case[] initialiserTableau(String nomfichier){
        Case[] plateau = new Case[15];
        try {
            DataInputStream reader = new DataInputStream(new FileInputStream(nomfichier));
            for (int i = 0; i < 15; i++) {
                String s = reader.readUTF();
                switch (s) {
                    case "D" -> {
                        plateau[i] = new Depart(s, reader.readUTF(), reader.readUTF(), reader.readInt());
                    }
                    case "Tx" -> {
                        plateau[i] = new Taxe(s, reader.readUTF(), reader.readUTF(), reader.readInt());
                    }
                    case "P" -> {
                        plateau[i] = new Stationnement(s, reader.readUTF(), reader.readUTF(), reader.readInt());
                    }
                    case "SP" -> {
                        plateau[i] = new Servicepublic(s, reader.readUTF(), reader.readInt());
                    }
                    case "T" -> {
                        plateau[i] = new Terrains(s, reader.readUTF(), reader.readInt(), reader.readInt());

                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return plateau;
    }



    public static LinkedList<Joueur> listeDeJoueurs() {
        Scanner sc = new Scanner(System.in);
        LinkedList<Joueur> liste = new LinkedList<Joueur>();
        int nmbdejoueurs = 0;
        while (nmbdejoueurs < 5) {
            //Nom du joueur
            System.out.print("Joueur " + (nmbdejoueurs + 1) + ": ");
            String ligne = sc.nextLine();

            //Minimum de 2 joueurs
            if (ligne.isBlank()) {
                if (nmbdejoueurs < 2) {
                    System.out.println("Vous avex besoin d'au moins 2 joueurs.");
                    nmbdejoueurs--;
                } else {
                    return liste;
                }
            }

            //Ajout du joueurs à la liste
            liste.add(new Joueur(ligne));
            nmbdejoueurs++;
        }
        return liste;
    }

    public static void tourmenu(DePipe de, Case[] plateau, Joueur joueur) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Lancer le dé en appuyant sur « \033[94mEntrée\033[39m ».");
        System.out.println("\033[94mQ\033[39muitter la partie.");
        System.out.println("\033[94mM\033[39mettre fin à la partie.");
        String choix = sc.nextLine();

        if (choix.isBlank()) {
            de.lancer();
            System.out.println("Le dé est tombé sur " + de.getDernierevaleur() + "!");
            avancer(plateau, de.getDernierevaleur(), joueur);

            return;
        } else if (choix.equalsIgnoreCase("Q")) {

        } else if (choix.equalsIgnoreCase("M")) {

        }
    }
    public static void avancer(Case[] plateau, int resultatde, Joueur joueur){

    }
//    public static void tab() {
//        try (DataInputStream reader = new DataInputStream(new FileInputStream("src/plateau.bin"))) {
//            int i = 1;
//            while (i != 15) {
//                String s = reader.readUTF();
//                switch (s) {
//                    case "D", "Tx", "P" -> {
//                        System.out.println(s);
//                        System.out.println(reader.readUTF());
//                        System.out.println(reader.readUTF());
//                        System.out.println(reader.readInt());
//                        System.out.println();
//                    }
//                    case "SP" -> {
//                        System.out.println(s);
//                        System.out.println(reader.readUTF());
//                        System.out.println("Valeur : " + reader.readInt());
//                        System.out.println("Loyer : Valeur du dé * 10");
//                        System.out.println();
//                    }
//                    case "T" -> {
//                        System.out.println(s);
//                        System.out.println(reader.readUTF());
//                        System.out.println("Valeur : " + reader.readInt());
//                        System.out.println("Loyer : " + reader.readInt());
//                        System.out.println();
//                    }
//                }
//                i++;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}