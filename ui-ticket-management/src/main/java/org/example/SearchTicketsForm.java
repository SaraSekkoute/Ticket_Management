package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SearchTicketsForm extends JFrame {
    private JTextField ticketIdField;
    private JComboBox<String> statusComboBox;
    private JTable ticketsTable;
    private DefaultTableModel tableModel;

    public SearchTicketsForm() {
        setTitle("Search & Filter Tickets");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for search input fields
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Ticket ID field
        JLabel ticketIdLabel = new JLabel("Ticket ID:");
        ticketIdField = new JTextField(10);

        // Status dropdown
        JLabel statusLabel = new JLabel("Status:");
        String[] statuses = {"NEW", "IN_PROGRESS", "CLOSED", "ON_HOLD"};
        statusComboBox = new JComboBox<>(statuses);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTickets());

        // Add components to searchPanel
        searchPanel.add(ticketIdLabel);
        searchPanel.add(ticketIdField);
        searchPanel.add(statusLabel);
        searchPanel.add(statusComboBox);
        searchPanel.add(searchButton);

        // Table setup
        String[] columns = {"Ticket ID", "Title", "Status", "Priority"};
        tableModel = new DefaultTableModel(columns, 0);
        ticketsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ticketsTable);

        // Layout for the main frame
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void searchTickets() {
        String ticketId = ticketIdField.getText();
        String status = (String) statusComboBox.getSelectedItem();

        // TODO: Call your service to get tickets based on ticketId and status
        List<Ticket> tickets = getFilteredTickets(ticketId, status);

        // Update the table with the filtered tickets
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear previous rows
            for (Ticket ticket : tickets) {
                tableModel.addRow(new Object[]{ticket.getId(), ticket.getTitle(), ticket.getStatusTracking(), ticket.getPriorityLevel()});
            }
        });
    }


    private List<Ticket> getFilteredTickets(String ticketId, String status) {
        try {
            // Construction de l'URL de l'API avec les paramètres de filtrage
            String apiUrl = "http://localhost:8082/api/tickets?ticketId=" + ticketId + "&status=" + status;
            URL url = new URL(apiUrl);

            // Création de la connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Vérification du code de réponse HTTP
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lecture du flux de la réponse
                InputStream responseStream = conn.getInputStream();

                // Utilisation de Jackson pour désérialiser la réponse JSON
                ObjectMapper objectMapper = new ObjectMapper();
                // Définir le type de la réponse attendue
                List<Ticket> tickets = objectMapper.readValue(responseStream, new TypeReference<List<Ticket>>() {});

                // Fermeture de la connexion
                conn.disconnect();

                return tickets;
            } else {
                System.err.println("Échec de la récupération des tickets - Code: " + responseCode);
            }

            // Si la réponse n'est pas OK, on retourne une liste vide
            return List.of();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des tickets: " + e.getMessage());
            return List.of();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(SearchTicketsForm::new);
    }
}
