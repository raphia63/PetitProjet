package com.example.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Agent;

@Repository
public interface AgentJpaRepository extends JpaRepository<Agent, Long> {
    // Pagination
    @Override
	Page<Agent> findAll(Pageable pageable);

    // Tri
    @Override
	List<Agent> findAll(Sort sort);

    Optional<Agent> findByNom(String nom);

    // Recherche avec pagination (nom ou prénom)
    Page<Agent> findByNomContainingOrPrenomContainingIgnoreCase(
        String nom, String prenom, Pageable pageable);

    // Méthode avec requête JPQL
    @Query("SELECT a FROM Agent a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    	       "LOWER(a.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    	Page<Agent> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Suppression en batch
    @Modifying
    @Query("DELETE FROM Agent a WHERE a.nom IS NULL")
    void deleteWithoutName();
}