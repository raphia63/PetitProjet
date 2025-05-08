package com.example.model;

import java.time.LocalDate;

import com.util.IdEntity;
import com.util.TypeDeContrat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "contrat", uniqueConstraints = @UniqueConstraint(columnNames = { "datedebutcontrat","idagent"}))
public class Contrat extends IdEntity {

	// constructeurs
	public Contrat(TypeDeContrat typecontrat, LocalDate datedebutcontrat, LocalDate datefincontrat, Agent agent) {
		if (datedebutcontrat == null || agent == null) {
			throw new IllegalArgumentException("datedebutcontrat cannot be null");
		}
		this.typecontrat = typecontrat;
		this.datedebutcontrat = datedebutcontrat;
		this.datefincontrat = datefincontrat;
		this.agent = agent;
	}

	public Contrat() {
	}

	public Contrat(LocalDate datedebutcontrat, Agent agent) {
		this.datedebutcontrat = datedebutcontrat;
		this.agent = agent;
	}

	// Propriétés

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idagent", nullable = false)
	private Agent agent;

	@Column(name = "typecontrat", columnDefinition = "varchar(40) default 'CDD'")
	@NotNull(message = "Le type de contrat est obligatoire")
	@Enumerated(EnumType.STRING)
	private TypeDeContrat typecontrat;

	@Column(name = "datedebutcontrat", nullable = false)
	private LocalDate datedebutcontrat;

	public void setDatedebutcontrat(LocalDate datedebutcontrat) {
		this.datedebutcontrat = datedebutcontrat;
	}

	public LocalDate getDatedebutcontrat() {
		return datedebutcontrat;
	}

	@Column(name = "datefincontrat")
	private LocalDate datefincontrat;

	public void setDatefincontrat(LocalDate datefincontrat) {
		this.datefincontrat = datefincontrat;
	}

	public LocalDate getDatefincontrat() {
		return datefincontrat;
	}

	public void setTypecontrat(TypeDeContrat typecontrat) {
		this.typecontrat = typecontrat;
	}

	public TypeDeContrat getTypecontrat() {
		return typecontrat;
	}

	public Agent getAgent() {
		return agent;
	}
	// Setter spécial pour la relation bidirectionnelle
	public void setAgent(Agent agent) {
		if (agent == null) {
			throw new IllegalArgumentException("agent cannot be null");
		}

		// Détache l'ancien agent si nécessaire
		if (this.agent != null) {
			this.agent.getPlannings().remove(this);
		}

		this.agent = agent;

		// Met à jour la référence inverse
		if (!agent.getContrats().contains(this)) {
			agent.getContrats().add(this);
		}
	}

}
