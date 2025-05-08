package com.example.controller;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Agent;
import com.example.model.Contrat;
import com.example.repositories.AgentJpaRepository;
import com.example.repositories.ContratJpaRepository;

@Controller
@RequestMapping("/agents")
public class AgentController {

    private final AgentJpaRepository agentRepository;
    private final ContratJpaRepository contratRepository;

    public AgentController(AgentJpaRepository agentRepository,
            ContratJpaRepository contratRepository) {
this.agentRepository = agentRepository;
this.contratRepository = contratRepository;
}

    @GetMapping
    public String listAgents(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("keyword") Optional<String> keyword) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String searchKeyword = keyword.orElse("");

        Page<Agent> agentPage;

        if (!searchKeyword.isEmpty()) {
            agentPage = agentRepository.searchByKeyword(
                searchKeyword,
                PageRequest.of(currentPage - 1, pageSize)
            );
        } else {
            agentPage = agentRepository.findAll(
                PageRequest.of(currentPage - 1, pageSize)
            );
        }

        model.addAttribute("agents", agentPage);
        model.addAttribute("keyword", searchKeyword);

        int totalPages = agentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "agents/listeagents";
    }
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("agent", new Agent());
        return "agents/formulaireagent";
    }

    @PostMapping("/save")
    public String saveAgent(@ModelAttribute Agent agent) {
        agentRepository.save(agent);
        return "redirect:/agents";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent invalide Id:" + id));
        model.addAttribute("agent", agent);
        return "agents/formulaireagent";
    }

    @GetMapping("/delete/{id}")
    public String deleteAgent(@PathVariable Long id) {
        agentRepository.deleteById(id);
        return "redirect:/agents";
    }

    @GetMapping("/{id}/contrats")
    public String showAgentContrats(@PathVariable Long id, Model model) {
        try {
            Agent agent = agentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Agent ID invalide : " + id));

            List<Contrat> contrats = contratRepository.findByAgent(agent);
            /*
            // Log de debug
            System.out.println("=== DEBUG CONTRATS ===");
            System.out.println("Agent: " + agent.getNom() + " " + agent.getPrenom());
            contrats.forEach(c -> {
                System.out.println("Contrat ID: " + c.getId() +
                                 ", Type: " + c.getTypecontrat() +
                                 ", Début: " + c.getDatedebutcontrat() +
                                 ", Fin: " + c.getDatefincontrat());
            });
            */
            model.addAttribute("agent", agent);
            model.addAttribute("contrats", contrats);

            return "agents/contrats";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{agentId}/contrats/new")
    public String showAddContratForm(@PathVariable Long agentId, Model model) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent ID invalide: " + agentId));

        Contrat contrat = new Contrat();
        contrat.setAgent(agent); // Associer directement à l'agent

        model.addAttribute("agent", agent);
        model.addAttribute("contrat", contrat);

        return "agents/contrat-form"; // Nous créerons ce template
    }

    @PostMapping("/{agentId}/contrats/save")
    public String saveContrat(@PathVariable Long agentId,
                            @ModelAttribute Contrat contrat,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "agents/contrat-form";
        }

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent ID invalide: " + agentId));

        contrat.setAgent(agent);
        contratRepository.save(contrat);

        return "redirect:/agents/" + agentId + "/contrats";
    }

}