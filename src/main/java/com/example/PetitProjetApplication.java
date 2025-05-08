package com.example;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.model.Agent;
import com.example.model.Annee;
import com.example.model.Contrat;
import com.example.repositories.AgentJpaRepository;
import com.example.repositories.AnneeJpaRepository;
import com.example.repositories.ContratJpaRepository;
import com.util.TypeDeContrat;

@SpringBootApplication
@EntityScan("com.example.model")
@EnableJpaRepositories("com.example.repositories")
public class PetitProjetApplication {
	private static final Logger log = LoggerFactory.getLogger(PetitProjetApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(PetitProjetApplication.class, args);
	}

/*

	@Bean
	  CommandLineRunner runner(AgentJpaRepository repository) {
	    return args -> {
log.trace("détails: ");
	    	// Vérification pour éviter les doublons
	        if (repository.count() == 0) {

	        	 Agent agent3 = new Agent("Test","Michel");

	            // Sauvegarde
	            repository.save(agent3);



	            // Sauvegarde multiple en une opération
	            Agent agent1 = new Agent("Doumé","Paul");
	            Agent agent2 = new Agent("Carnaval","Régine");

	            repository.saveAll(List.of(agent1, agent2));



	         // Tri par nom (ascendant) puis prénom (descendant)
	            Sort multiSort = Sort.by(Sort.Direction.DESC, "nom")
	                              .and (Sort.by("prenom"));

	            List<Agent> agents = repository.findAll(multiSort);

	            // Log pour vérification

	            agents.forEach(p ->
	            log.info("Agent - Nom: {}, Prénom: {}", p.getNom(), p.getPrenom())

	        );

	 	            // Avec numérotation et formatage tableau
	 	           System.out.println("Liste triée des agents:");
	 	           System.out.println("-------------------------");
	 	           IntStream.range(0, agents.size())
	 	          .forEach(i -> {
	 	              Agent p = agents.get(i);
	 	              System.out.printf("%2d | %-15s | %-15s%n",
	 	                  i + 1, p.getNom(), p.getPrenom());
	 	          });


	        }
	    };
	}
*/

	@Bean
    CommandLineRunner initDatabase(
        AnneeJpaRepository anneeRepository,
        ContratJpaRepository contratRepository,
        AgentJpaRepository agentRepository) {

        return args -> {
            // Initialisation des années
            if (anneeRepository.count() == 0) {
                Annee annee2023 = new Annee(2023, false);
                Annee annee2024 = new Annee(2024, false);
                Annee annee2025 = new Annee(2025, true);
                anneeRepository.saveAll(List.of(annee2023, annee2024, annee2025));
            }

            // Initialisation des agents
            if (agentRepository.count() == 0) {
                Agent agent1 = new Agent("Dupont", "Jean");
                Agent agent2 = new Agent("Martin", "Alice");
                Agent agent3 = new Agent("Lechopier", "Pierre");
                agentRepository.saveAll(List.of(agent1, agent2,agent3));
            }

            // Initialisation des contrats
            if (contratRepository.count() == 0) {
                Agent agent = agentRepository.findByNom("Dupont").orElseThrow();
                Contrat contrat = new Contrat(TypeDeContrat.CDD,
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 12, 31),
                    agent
                );

                Contrat contrat2 = new Contrat(TypeDeContrat.CDI,
                        LocalDate.of(2024, 2, 5),
                        LocalDate.of(2024, 7, 12),
                        agent
                    );

                contratRepository.saveAll(List.of(contrat,contrat2));

                Agent agentx = agentRepository.findByNom("Lechopier").orElseThrow();
                Contrat contratx = new Contrat(TypeDeContrat.AUTRE,
                    LocalDate.of(2023, 1, 1),
                    null,
                    agentx
                );
                contratRepository.save(contratx);
            }
        };
    }



}

