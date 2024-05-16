import ca.qc.bdeb.sim202.tp2.DePipe;

import java.util.Scanner;

public abstract class Case {
    private final String type;
    private String nom;
    private String description;
    private Joueur proprietaire;
    private int montantdelacase;
    private boolean aUnProprietaire = false;

    public Case(String type, String nom, String description, int valeur, int montantapayer) {
        this.type = type;
        this.nom = nom;
        this.description = description;
        this.montantdelacase = montantapayer;
    }

    public String getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getMontantdelacase() {
        return montantdelacase;
    }

    public int getPrix() {
        return 0;
    }

    public int getLoyer() {
        return 0;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Joueur joueur) {
        proprietaire = joueur;
    }

    ;

    public void aUnProprietaire() {
        aUnProprietaire = true;
    }

    public boolean getaUnProprietaire() {
        return aUnProprietaire;
    }

    public void payerLoyer(Joueur joueur, int loyer) {
        //S'il a assez d'argent
        if (joueur.getArgent() >= loyer) {
            joueur.retirerArgent(loyer);
        } // S'il n'a pas assez
        else if (joueur.getArgent() > 0) {
            joueur.setArgent(0);
        } else {
            System.out.println(joueur.getNom() + " est éliminé.");
            joueur.setMort();
        }
    }

    public void payerTaxe(Joueur joueur) {
    }

    public int getTaxe() {
        return 0;
    }

    ;

    public void payerStationnement(Joueur joueur) {
    }

    public void survolerCase(Case[] plateau, int resultatde, Joueur joueur) {
        Scanner sc = new Scanner(System.in);
        //Déterminer la dernière case
        int positionfinale = 0;
        if (joueur.getPosition() + resultatde <= plateau.length) {
            positionfinale = joueur.getPosition() + resultatde;
        } else {
            positionfinale = (joueur.getPosition() + resultatde) - plateau.length;
        }

        //Survoler les cases
        for (int i = joueur.getPosition(); i != positionfinale; i++) {
            joueur.setPosition(joueur.getPosition() + 1);
            //Remettre à 0 si dépasse la longueur du plateau
            if (joueur.getPosition() > plateau.length) {
                joueur.setPosition(0);
                i = 0;
            }

            String type = plateau[i].getType();
            switch (type) {
                case "D" -> {
                    joueur.ajouterArgent(50);
                    System.out.println(joueur.getNom() + ", vous avez maintenant $" + joueur.getArgent());
                }
                case "Tx" -> {
                    if (positionfinale == i) {
                        if (joueur.getArgent() > plateau[i].getTaxe()) {
                            plateau[i].payerTaxe(joueur);
                            System.out.println(joueur.getNom() + ", vous avez maintenant $" + joueur.getArgent());
                        } else {
                            System.out.println(joueur.getNom() + " a fait faillite. La partie est terminée.");
                            Main.quitter();
                        }
                    } else {
                        double taxe = 0.1 * plateau[i].getTaxe();
                        if (joueur.getArgent() > plateau[i].getTaxe()) {
                            joueur.retirerArgent(plateau[i].getTaxe() * 0.1);
                            System.out.println(joueur.getNom() + ", vous avez maintenant $" + joueur.getArgent());
                        } else {
                            System.out.println(joueur.getNom() + " a fait faillite. La partie est terminée.");
                            Main.quitter();
                        }
                    }
                }
                case "P" -> {
                    if (i == positionfinale) {
                        System.out.println("Vous avez fini dans un stationnement, veuillez rouler le dé « \033[94mEntrée\033[39m ». ");
                        String choix = sc.nextLine();
                        while (!choix.isEmpty()) {
                            System.out.println("Veuillez appuyer sur « \033[94mEntrée\033[39m ».");
                        }

                        DePipe de = new DePipe();
                        int resultat = de.lancer();
                        int taxe = resultat * 15;
                        switch (resultat) {
                            case 2 -> {
                                System.out.println("Stationnement interdit les jours de la semaine.");
                                System.out.println("Vous devez payer $" + taxe);
                                joueur.retirerArgent(taxe);
                            }
                            case 4 -> {
                                System.out.println("Stationnement réservé aux détenteurs de permis.");
                                System.out.println("Vous devez payer $" + taxe);
                                joueur.retirerArgent(taxe);
                            }
                            case 6 -> {
                                System.out.println("Place réservée aux handicapés.");
                                System.out.println("Vous devez payer $" + taxe);
                                joueur.retirerArgent(taxe);
                            }
                            default -> {
                                System.out.println("Vous n'avez rien.");
                                System.out.println(joueur.getNom() + " ,vous avez $" + joueur.getArgent());
                            }
                        }
                    }
                }
                case "SP" -> {
                    //Si elle a un proprio autre que le joueur
                    if (plateau[i].getaUnProprietaire() && plateau[i].getProprietaire() != joueur) {
                        joueur.retirerArgent(resultatde * 10);
                        plateau[i].getProprietaire().ajouterArgent(resultatde * 10);
                        System.out.println(joueur.getNom() + ", vous avez maintenant $" + joueur.getArgent());
                    } // Si le proprio est le joueur
                    else if (plateau[i].getProprietaire() == joueur) {
                        System.out.println("Ce service public vous appartient.");
                        System.out.println("Vous avez $" + joueur.getArgent());
                    } // Si elle a pas de proprio et que le joueur a assez d'argent
                    else if (!plateau[i].getaUnProprietaire() && joueur.getArgent() > plateau[i].getPrix()) {
                        //Set le proprio
                        plateau[i].setProprietaire(joueur);
                        //Achat
                        joueur.retirerArgent(plateau[i].getPrix());
                    } else {
                        System.out.println("Vous n'avez pas assez d'argent pour acheter ce service public.");
                    }
                }
                case "T" -> {
                    //Si elle a un propriétaire et que le propriétaire n'est pas le joueur
                    if (plateau[i].getaUnProprietaire() && plateau[i].getProprietaire() != joueur) {
                        joueur.retirerArgent(plateau[i].getLoyer());
                        System.out.println(joueur.getNom() + ", vous avez maintenant $" + joueur.getArgent());
                    } // Si le proprio est le joueur
                    else if (plateau[i].getProprietaire() == joueur) {
                        System.out.println("Ce terrain vous appartient.");
                        System.out.println(joueur.getNom() + ", vous avez maintenant $" + joueur.getArgent());
                    } //Si elle n'a pas de proprio et que le joueur a assez d'argent pour l'acheter
                    else if (!plateau[i].getaUnProprietaire() && joueur.getArgent() > plateau[i].getPrix()) {
                        if (joueur.getArgent() > plateau[i].getLoyer()) {
                            joueur.retirerArgent(plateau[i].getLoyer());
                        } else {
                            System.out.println(joueur.getNom() + ", vous avez fait faillite.");
                            //Partie terminée
                            Main.quitter();
                        }
                    } //Aucun proprio mais pas assez d'argent pour l'acheter
                    else {
                        System.out.println("Vous n'avez pas assez d'argent pour acheter cette propriété.");
                    }
                }
            }
        }
        joueur.setDernierePosition(positionfinale);
    }

}
