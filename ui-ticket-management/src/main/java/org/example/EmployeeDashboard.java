package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeDashboard {
    private JFrame frame;
    private Long employeeId;

    public EmployeeDashboard(Long employeeId) {
        this.employeeId = employeeId;
        frame = new JFrame("Employee Dashboard");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();

        // Menu Ticket
        JMenu ticketMenu = new JMenu("Tickets");
        JMenuItem createTicketItem = new JMenuItem("Create Ticket");
        JMenuItem viewTicketsItem = new JMenuItem("View Your Tickets");

        // Ajout des items au menu Ticket
        ticketMenu.add(createTicketItem);
        ticketMenu.add(viewTicketsItem);

        // Ajout du menu à la barre de menu
        menuBar.add(ticketMenu);

        // Ajouter la barre de menu à la fenêtre
        frame.setJMenuBar(menuBar);

        // Ajouter une étiquette de bienvenue
        JLabel label = new JLabel("Welcome, Employee!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(label);

        // Actions des éléments du menu
        createTicketItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateTicketForm(employeeId); // Ouvre le formulaire de création de ticket avec l'ID de l'employé
            }
        });

        viewTicketsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ListMyTicketForm(employeeId); // Ouvre la liste des tickets pour cet employé
            }
        });

        frame.setVisible(true);
    }
}
