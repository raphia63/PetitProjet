package com.example.model;

import java.time.LocalDate;

import com.util.IdEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "planning", uniqueConstraints = @UniqueConstraint(columnNames = { "dateposition", "idagent" }))

public class Planning extends IdEntity {

	// constructeur
	public Planning(LocalDate dateposition, String position, Agent agent) {
		// les champs ne peuvent pas être nuls
		if (dateposition == null || position == null || agent == null) {
			throw new IllegalArgumentException("Arguments cannot be null");
		}

		this.dateposition = dateposition;
		this.position = position;
		this.agent = agent;
	}

	public Planning() {
	}

	@Column(name = "dateposition", nullable = false)
	private LocalDate dateposition;

	@Column(name = "position", nullable = false,length=3)
	private String position;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idagent", nullable = false)
	private Agent agent;

	// Getters, Setters

	// Getters
	public LocalDate getDateposition() {
		return dateposition;
	}

	public String getPosition() {
		return position;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setDateposition(LocalDate dateposition) {
		if (dateposition == null) {
			throw new IllegalArgumentException("dateposition cannot be null");
		}
		this.dateposition = dateposition;
	}

	public void setPosition(String position) {
		if (position == null || position.trim().isEmpty()) {
			throw new IllegalArgumentException("position cannot be null or empty");
		}
		this.position = position;
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
		if (!agent.getPlannings().contains(this)) {
			agent.getPlannings().add(this);
		}
	}
}
