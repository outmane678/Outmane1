package controller;

import exceptions.LivreDejaExisteException;
import exceptions.LivreNonTrouveException;
import model.Livre;
import model.LivreModel;
import view.LivreView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class LivreController {
    private LivreModel model;
    private LivreView view;

    public LivreController() {
        this.model = new LivreModel("C:\\Users\\DELL\\Desktop\\livres.csv");
        this.view = new LivreView();

        // Ajouter les listeners aux boutons
        view.getAjouterButton().addActionListener(e -> ajouterLivre());
        view.getModifierButton().addActionListener(e -> modifierLivre());
        view.getSupprimerButton().addActionListener(e -> supprimerLivre());

        // Charger les livres dans la vue
        chargerLivres();
    }

    private void chargerLivres() {
        List<Livre> livres = model.getLivres();
        DefaultTableModel tableModel = (DefaultTableModel) view.getLivresTable().getModel();
        tableModel.setRowCount(0); // Effacer les lignes actuelles
        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{livre.getId(), livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getGenre()});
        }
    }

    private void ajouterLivre() {
        try {
            int id = Integer.parseInt(view.getIdTextField().getText());
            String titre = view.getTitreTextField().getText();
            String auteur = view.getAuteurTextField().getText();
            int annee = Integer.parseInt(view.getAnneeTextField().getText());
            String genre = view.getGenreTextField().getText();

            Livre livre = new Livre(id, titre, auteur, annee, genre);
            model.ajouterLivre(livre); // Peut lancer LivreDejaExisteException
            JOptionPane.showMessageDialog(view, "Livre ajouté avec succès !");
            chargerLivres();
            // Réinitialisation des champs
            réinitialiserChamps();
        } catch (LivreDejaExisteException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Un livre avec cet ID existe déjà !");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Erreur de saisie : ID et Année doivent être des entiers.");
        }
    }

    private void modifierLivre() {
        int selectedRow = view.getLivresTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un livre.");
            return;
        }

        try {
            int idSelectionne = (int) view.getLivresTable().getValueAt(selectedRow, 0);
            int idSaisi = Integer.parseInt(view.getIdTextField().getText());

            if (idSaisi != idSelectionne) {
                JOptionPane.showMessageDialog(view, "Erreur : Vous ne pouvez pas modifier l'ID du livre sélectionné.");
                return;
            }

            String titre = view.getTitreTextField().getText();
            String auteur = view.getAuteurTextField().getText();
            int annee = Integer.parseInt(view.getAnneeTextField().getText());
            String genre = view.getGenreTextField().getText();

            model.modifierLivre(idSelectionne, titre, auteur, annee, genre); // Peut lancer LivreNonTrouveException
            JOptionPane.showMessageDialog(view, "Livre modifié avec succès !");
            chargerLivres();
            réinitialiserChamps();
        } catch (LivreNonTrouveException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Livre non trouvé.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Erreur de saisie : ID et Année doivent être des entiers.");
        }
    }

    private void supprimerLivre() {
        int selectedRow = view.getLivresTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un livre.");
            return;
        }

        int id = (int) view.getLivresTable().getValueAt(selectedRow, 0);
        try {
            model.supprimerLivre(id); // Peut lancer LivreNonTrouveException
            JOptionPane.showMessageDialog(view, "Livre supprimé avec succès !");
            chargerLivres();
        } catch (LivreNonTrouveException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Livre non trouvé.");
        }
    }

    private void réinitialiserChamps() {
        view.getIdTextField().setText("");
        view.getTitreTextField().setText("");
        view.getAuteurTextField().setText("");
        view.getAnneeTextField().setText("");
        view.getGenreTextField().setText("");
    }

    // Ajout de la méthode getView()
    public LivreView getView() {
        return this.view;
    }
}
