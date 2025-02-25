package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ListMyTicketForm {
    private JFrame frame;
    private JTable ticketTable;
    private Long employeeId;

    public ListMyTicketForm(Long employeeId) {
        this.employeeId = employeeId;

        frame = new JFrame("Your Tickets");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Tableau pour afficher les tickets
        String[] columnNames = {"ID", "Title", "Priority", "Category", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        ticketTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        loadTickets(tableModel);

        frame.setVisible(true);
    }

    private void loadTickets(DefaultTableModel tableModel) {
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            // URL de l'API avec l'ID de l'employé
            URL url = new URL("http://localhost:8082/api/tickets/employee/" + employeeId);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                JOptionPane.showMessageDialog(frame, "Failed to fetch tickets: " + conn.getResponseCode());
                return;
            }

            // Lecture de la réponse
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Désérialisation de la réponse JSON en une liste de tickets
            List<Ticket> tickets = objectMapper.readValue(response.toString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Ticket.class));

            // Affichage des tickets dans le tableau
            displayTickets(tickets);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading tickets: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (reader != null) reader.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayTickets(List<Ticket> tickets) {
        String[] columns = {"ID", "Title", "Priority", "Category", "Status", "Employee"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Ticket ticket : tickets) {
            Object[] row = {
                    ticket.getId(),
                    ticket.getTitle(),
                    ticket.getPriorityLevel(),
                    ticket.getCategoryticket() != null ? ticket.getCategoryticket().getCategoryName() : "No Category",
                    ticket.getStatusTracking(),
                    ticket.getEmployee() != null ? ticket.getEmployee().getId() : "No Employee"
            };
            tableModel.addRow(row);
        }

        ticketTable.setModel(tableModel);
    }


}
