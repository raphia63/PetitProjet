package com.util;

public enum TypeDeContrat {
    CDI, CDD, AUTRE ;
    // Optionnel : méthode pour l'affichage
    public String getLibelle() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
