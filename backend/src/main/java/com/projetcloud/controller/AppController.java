package com.projetcloud.controller;

import com.projetcloud.exceptions.*;
import com.projetcloud.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;


@RestController
public class AppController {

    private final Facade facade;

    public AppController(Facade facade) {
        this.facade = facade;
    }

    /**
     * Jouer un coup
     *
     * @return
     */
    @PostMapping("/partie/{idPartie}/coup")
    public ResponseEntity<?> jouerCoup(@PathVariable UUID idPartie, @RequestParam int colonne, @RequestParam String joueur){
        try {
            facade.jouerCoup(idPartie, colonne, joueur);
            return ResponseEntity.ok().build();
        } catch (MauvaisesCoordonneesExcpetion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coordonnées invalides");
        } catch (CoupNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Coup non autorisé");
        } catch (PartieInexistanceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partie introuvable");
        } catch (MauvaisTourException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mauvais tour de jeu");
        } catch (PartieTermineException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La partie est déjà terminée");
        }
    }


    @GetMapping("/partie/{idPartie}")
    public ResponseEntity<?> getPartie(@PathVariable UUID idPartie) {
        try {
            return ResponseEntity.ok(facade.getPartie(idPartie));
        } catch (PartieInexistanceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partie introuvable");
        }
    }

    @GetMapping("/partie")
    public ResponseEntity<?> creerPartie(@RequestParam UUID idPartie, @RequestParam ArrayList<String> listeJoueur) {
        return ResponseEntity.ok(facade.creerPartie(idPartie, listeJoueur));
    }

    @PostMapping("/salon")
    public ResponseEntity<?> creerSalon(@RequestParam String joueur) {
        return ResponseEntity.ok(facade.creerSalon(joueur));
    }

    @PostMapping("/salon/{idSalon}")
    public ResponseEntity<?> rejoindreSalon(@PathVariable UUID idSalon, @RequestParam String joueur) {
        try {
            return ResponseEntity.ok(facade.rejoindreSalon(idSalon, joueur));
        } catch (TropDeJoueurException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trop de joueurs dans le salon");
        }
    }

    @GetMapping("/salon/{idSalon}")
    public ResponseEntity<?> getSalon(@PathVariable UUID idSalon) {
        try {
            return ResponseEntity.ok(facade.getSalon(idSalon));
        } catch (SalonInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
        }
    }

}
