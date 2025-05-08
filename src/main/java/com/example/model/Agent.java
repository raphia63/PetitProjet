package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.util.IdEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "agent")


public class Agent extends IdEntity{
// Constructeurs

    public Agent(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public Agent(String nom) {
		this.nom = nom;
			}
	 public Agent() {
	        // Constructeur vide requis par JPA
	    }
//Propriétés
  @Column(name="nom")
  @NotBlank(message = "Le nom doit être renseigné")
  private String nom;

@Column(name="prenom")
@NotBlank(message = "Le prénom doit être renseigné")
  private String prenom;


@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Planning> plannings = new ArrayList<>(); // Changé au pluriel

@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Contrat> contrats = new ArrayList<>(); // Changé au pluriel



	@Override
    public String toString() {
        return "Agent{" +
               "id=" + this.getId() +
               ", nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               '}';
    }
	// Getters, Setters

	public void setNom(String nom) {
			this.nom= nom;
		}
		public String getNom() {
			return nom;
		}
		public void setPrenom(String prenom) {
			this.prenom= prenom;
		}
		public String getPrenom() {
			return prenom;
		}
	    // Getter (recommandé: retourne une liste non modifiable)
		public List<Planning> getPlannings() {
	        return plannings;
	    }



	    // Setter (version sécurisée)
		 public void setPlannings(List<Planning> plannings) {
		        this.plannings.clear();
		        if (plannings != null) {
		            plannings.forEach(this::addPlanning);
		        }
		    }

	    // Méthode pour ajouter un Planning

		 public void addPlanning(Planning planning) {
		        if (planning != null && !this.plannings.contains(planning)) {
		            this.plannings.add(planning);
		            planning.setAgent(this);
		        }
		    }


	    // Méthode corrigée pour supprimer un Planning
		 public void removePlanning(Planning planning) {
		        if (planning != null && this.plannings.remove(planning)) {
		            planning.setAgent(null);
		        }
		    }


	    // Getter (recommandé: retourne une liste non modifiable)
	    public List<Contrat> getContrats() {
	        return contrats;
	    }
	    // Setter (version sécurisée)
	    public void setContrats(List<Contrat> contrats) {
	        this.contrats.clear();
	        if (contrats != null) {
	            contrats.forEach(this::addContrat);
	        }
	    }

	    // Méthode pour ajouter un Contrat

	    public void addContrat(Contrat contrat) {
	        if (contrat != null && !this.contrats.contains(contrat)) {
	            this.contrats.add(contrat);
	            contrat.setAgent(this);
	        }
	    }


	    // Méthode corrigée pour supprimer un contrat
	    public void removeContrat(Contrat contrat) {
	        if (contrat != null && this.contrats.remove(contrat)) {
	            contrat.setAgent(null);
	        }
	    }
}