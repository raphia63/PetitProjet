package com.example.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Agent;
import com.example.model.Contrat;
import com.util.TypeDeContrat;


@Repository
public interface ContratJpaRepository extends JpaRepository<Contrat, Long> {

    // Méthodes de base incluses via JpaRepository:
    // save(), findById(), findAll(), delete(), etc.

    // Trouver les contrats d'un agent spécifique
    List<Contrat> findByAgent(Agent agent);

    // Trouver les contrats actifs à une date donnée
    @Query("SELECT c FROM Contrat c WHERE c.datedebutcontrat <= :date AND (c.datefincontrat IS NULL OR c.datefincontrat >= :date)")
    List<Contrat> findContratsActifs(@Param("date") LocalDate date);

    // Trouver les contrats entre deux dates
    List<Contrat> findByDatedebutcontratBetween(LocalDate start, LocalDate end);

    // Trouver les contrats sans date de fin
    List<Contrat> findByDatefincontratIsNull();

    // Vérifier l'existence d'un contrat pour un agent sur une période
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM Contrat c " +
           "WHERE c.agent = :agent " +
           "AND (c.datefincontrat IS NULL OR c.datefincontrat >= :startDate) " +
           "AND c.datedebutcontrat <= :endDate")
    boolean existsContratForAgentInPeriod(
            @Param("agent") Agent agent,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    //Troiuver les contrats par type
    @Query("SELECT c FROM Contrat c WHERE c.typecontrat = :type")
    List<Contrat> findByTypecontrat(@Param("type") TypeDeContrat type);
}