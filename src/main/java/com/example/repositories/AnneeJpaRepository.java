package com.example.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Annee;

@Repository
public interface AnneeJpaRepository extends JpaRepository<Annee, Long> {

    // Trouver une année par sa valeur (retourne Optional pour éviter NullPointerException)
    Optional<Annee> findByAnnee(int annee);

    // Vérifier si une année existe
    boolean existsByAnnee(int annee);

    // Trouver toutes les années actives
    List<Annee> findByActiveTrue();

    // Trouver toutes les années inactives
    List<Annee> findByActiveFalse();

    // Trouver les années dans une plage donnée
    List<Annee> findByAnneeBetween(int startAnnee, int endAnnee);

    // Activer/Désactiver une année spécifique (update direct en base)
    @Modifying
    @Query("UPDATE Annee a SET a.active = :active WHERE a.id = :id")
    void setActiveStatus(@Param("id") Long id, @Param("active") boolean active);

    // Trouver l'année active courante
    @Query("SELECT a FROM Annee a WHERE a.active = true")
    Optional<Annee> findActiveAnnee();

    // Désactiver toutes les années (utile avant d'en activer une nouvelle)
    @Modifying
    @Query("UPDATE Annee a SET a.active = false")
    void deactivateAll();

    // Trouver les années supérieures à une année donnée
    List<Annee> findByAnneeGreaterThan(int annee);

    // Trouver les années inférieures à une année donnée
    List<Annee> findByAnneeLessThan(int annee);

    // Requête personnalisée pour vérifier les années dans une liste
    @Query("SELECT a FROM Annee a WHERE a.annee IN :annees")
    List<Annee> findByAnneesList(@Param("annees") List<Integer> annees);
}