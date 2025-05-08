package com.example.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Agent;
import com.example.model.Planning;

@Repository
public interface PlanningJpaRepository extends JpaRepository<Planning, Long> {

    // 1. Méthodes de base fournies par JpaRepository:
    // save(), findById(), findAll(), deleteById(), etc.

    // 2. Recherche par agent
    List<Planning> findByAgent(Agent agent);

    // 3. Recherche par date
    List<Planning> findByDateposition(LocalDate date);

    // 4. Recherche par période
    List<Planning> findByDatepositionBetween(LocalDate startDate, LocalDate endDate);

    // 5. Recherche par position (exact match)
    List<Planning> findByPosition(String position);

    // 6. Recherche par position (contient)
    List<Planning> findByPositionContainingIgnoreCase(String keyword);

    // 7. Recherche combinée agent + période
    @Query("SELECT p FROM Planning p WHERE p.agent = :agent AND p.dateposition BETWEEN :start AND :end")
    List<Planning> findPlanningsForAgentInPeriod(
            @Param("agent") Agent agent,
            @Param("start") LocalDate startDate,
            @Param("end") LocalDate endDate);

    // 8. Vérification d'existence d'un planning pour un agent à une date
    boolean existsByAgentAndDateposition(Agent agent, LocalDate date);

    // 9. Recherche des plannings futurs pour un agent
    @Query("SELECT p FROM Planning p WHERE p.agent = :agent AND p.dateposition >= CURRENT_DATE ORDER BY p.dateposition ASC")
    List<Planning> findFuturePlanningsForAgent(@Param("agent") Agent agent);

    // 10. Suppression en masse par agent
    @Modifying
    @Query("DELETE FROM Planning p WHERE p.agent = :agent")
    void deleteByAgent(@Param("agent") Agent agent);
}