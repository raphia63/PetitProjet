package com.example.model;


import com.util.IdEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "annee",
uniqueConstraints = @UniqueConstraint(columnNames = "annee")) // Ajout de la contrainte d'unicité
public class Annee extends IdEntity {
	// constructeur
	public Annee(int annee, boolean active) {
		this.annee = annee;
		this.active = active;
	}

	public Annee() {

	}

	@Column(name = "annee", nullable = false)


	@Digits(integer = 4, fraction = 0, message = "L'année doit contenir exactement 4 chiffres")
    @Min(value = 2000, message = "L'année doit être ≥ 2000")
    @Max(value = 2100, message = "L'année doit être ≤ 2100")

	private int annee;

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getAnnee() {
		return annee;
	}

	@Column(name = "active", nullable = false,columnDefinition = "boolean default false")
	private boolean active;

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getActive() {
		return active;
	}
}
