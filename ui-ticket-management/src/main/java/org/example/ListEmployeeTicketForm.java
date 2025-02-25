package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class ListEmployeeTicketForm extends JFrame {
    private JTable ticketTable;
    private JButton changeStatusButton;
    private JButton addCommentButton;

    public ListEmployeeTicketForm() {
        setTitle("Employee Tickets");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ticketTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        changeStatusButton = new JButton("Change Status");
        addCommentButton = new JButton("Add Comment");

        buttonPanel.add(changeStatusButton);
        buttonPanel.add(addCommentButton);
        add(buttonPanel, BorderLayout.SOUTH);

        fetchTickets();

        changeStatusButton.addActionListener(this::changeStatus);
        addCommentButton.addActionListener(this::addComment);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchTickets() {
        SwingUtilities.invokeLater(() -> {
            try {
                URL url = new URL("http://localhost:8082/api/tickets");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    JOptionPane.showMessageDialog(this, "Failed to fetch tickets: " + conn.getResponseCode());
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                List<Ticket> tickets = objectMapper.readValue(response.toString(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Ticket.class));
                displayTickets(tickets);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error fetching tickets: " + e.getMessage());
            }
        });
    }

    private void displayTickets(List<Ticket> tickets) {
        String[] columns = {"ID", "Title", "Status", "Employee"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Ticket ticket : tickets) {
            Object[] row = {
                    ticket.getId(),
                    ticket.getTitle(),
                    ticket.getStatusTracking(),
                    ticket.getEmployee() != null ? ticket.getEmployee().getId() : "No Employee"
            };
            tableModel.addRow(row);
        }

        ticketTable.setModel(tableModel);
    }

    private void changeStatus(ActionEvent e) {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket first.");
            return;
        }

        Long ticketId = (Long) ticketTable.getValueAt(selectedRow, 0);

        // Définition des trois statuts possibles
        String[] statusOptions = {"NEW", "IN_PROGRESS", "CLOSED"};

        int selectedOption = JOptionPane.showOptionDialog(
                this,
                "Select new status:",
                "Change Ticket Status",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOptions,
                statusOptions[0]
        );

        if (selectedOption >= 0) { // Vérifier si l'utilisateur n'a pas annulé
            updateTicketStatus(ticketId, statusOptions[selectedOption]);
        }
    }

    private void updateTicketStatus(Long ticketId, String newStatus) {
        try {
            URL url = new URL("http://localhost:8082/api/tickets/" + ticketId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{\"statusTracking\": \"" + newStatus + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInputString.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Mise à jour du ticket - Response Code: " + responseCode);

            if (responseCode == 200) {
                System.out.println(" Statut du ticket mis à jour avec succès.");
                fetchTickets();
            } else {
                System.err.println(" Échec de la mise à jour du ticket - Code: " + responseCode);
                if (conn.getErrorStream() != null) {
                    String errorMessage = new String(conn.getErrorStream().readAllBytes());
                    System.err.println("⚠️ Erreur du serveur: " + errorMessage);
                }
            }
            conn.disconnect();

            // 🔍 Ajouter un log d'audit pour cette mise à jour
            URL logUrl = new URL("http://localhost:8082/api/audit-logs");
            HttpURLConnection logConn = (HttpURLConnection) logUrl.openConnection();
            logConn.setRequestMethod("POST");
            logConn.setDoOutput(true);
            logConn.setRequestProperty("Content-Type", "application/json");

            String logDetail = "{\"action\": \"Update\", \"detail\": \"Updated status to " + newStatus + "\", \"timestamp\": \"" + LocalDateTime.now().toString() + "\"}";

            try (OutputStream os = logConn.getOutputStream()) {
                os.write(logDetail.getBytes());
                os.flush();
            }

            int logResponseCode = logConn.getResponseCode();
            System.out.println("Envoi du log d'audit - Response Code: " + logResponseCode);

            if (logResponseCode == 200) {
                System.out.println("Log d'audit enregistré avec succès.");
            } else {
                System.err.println("Échec de l'enregistrement du log d'audit - Code: " + logResponseCode);
                if (logConn.getErrorStream() != null) {
                    String logErrorMessage = new String(logConn.getErrorStream().readAllBytes());
                    System.err.println("Erreur du serveur (audit log): " + logErrorMessage);
                }
            }
            logConn.disconnect();

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du statut: " + e.getMessage());
            e.printStackTrace(); // Affiche la pile complète des erreurs
        }
    }

    private void addTicketComment(Long ticketId, String comment) {
        try {
            URL url = new URL("http://localhost:8082/api/comment-tickets/" + ticketId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Construire le JSON correctement
            String jsonInputString = "{\"content\": \"" + comment.replace("\"", "\\\"") + "\"}";

            // Envoyer la requête
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Comment added successfully.");
                JOptionPane.showMessageDialog(null, "Comment added successfully.");
                fetchTickets();
            } else {
                // Lire la réponse d'erreur
                try (InputStream is = conn.getErrorStream()) {
                    if (is != null) {
                        String errorResponse = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        System.out.println("Error Response: " + errorResponse);
                        JOptionPane.showMessageDialog(null, "Failed to add comment: " + responseCode + "\n" + errorResponse);
                    }
                }
            }
            conn.disconnect();

            // Ajouter log pour l'ajout de commentaire
            URL logUrl = new URL("http://localhost:8082/api/audit-logs");
            HttpURLConnection logConn = (HttpURLConnection) logUrl.openConnection();
            logConn.setRequestMethod("POST");
            logConn.setDoOutput(true);
            logConn.setRequestProperty("Content-Type", "application/json");

            // Remplacer par la méthode pour obtenir le ticket
            Ticket ticket = getTicketById(ticketId); // Remplacer par la méthode pour obtenir le ticket


            String logDetail = "{\"action\": \"Added\", \"detail\": \"Added comment to ticket: " + ticket.getDescription() + "\", \"timestamp\": \"" + LocalDateTime.now().toString() + "\"}";

            try (OutputStream os = logConn.getOutputStream()) {
                os.write(logDetail.getBytes());
                os.flush();
            }
            int responseCode_ = conn.getResponseCode();
            System.out.println(" Response Code: " + responseCode_);

            if (responseCode_ == HttpURLConnection.HTTP_OK) {
                System.out.println("✅ Audit Log envoyé avec succès.");

                // Lire et afficher la réponse du serveur
                String responseMessage = new String(conn.getInputStream().readAllBytes());
                System.out.println("🔹 Réponse du serveur: " + responseMessage);
            } else {
                System.err.println("Échec de l'envoi de l'Audit Log - Code: " + responseCode_);

                // Lire et afficher l'erreur du serveur
                if (conn.getErrorStream() != null) {
                    String errorMessage = new String(conn.getErrorStream().readAllBytes());
                    System.err.println("Erreur du serveur: " + errorMessage);
                } else {
                    System.err.println("Aucune erreur retournée par le serveur.");
                }
            }

            conn.disconnect();
        } catch (Exception e) {
            System.err.println("🚨 Erreur lors de l'envoi du log d'audit: " + e.getMessage());
            e.printStackTrace();  // Afficher la pile d'erreurs pour plus de détails
        }
    }

    private Ticket getTicketById(Long ticketId) {
        try {
            URL url = new URL("http://localhost:8082/api/tickets/" + ticketId); // URL de l'API pour récupérer un ticket par ID
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.out.println("Failed to fetch ticket: " + conn.getResponseCode());
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            // Convertir la réponse JSON en un objet Ticket
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(response.toString(), Ticket.class);

        } catch (Exception e) {
            System.out.println("Error fetching ticket: " + e.getMessage());
            return null;
        }
    }


    private void addComment(ActionEvent e) {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket first.");
            return;
        }

        Long ticketId = (Long) ticketTable.getValueAt(selectedRow, 0);

        String comment = JOptionPane.showInputDialog(this, "Enter your comment:");
        if (comment != null && !comment.trim().isEmpty()) {
            addTicketComment(ticketId,comment);
        }
    }


    // Lire la réponse d'erreur et l'afficher dans la console
    private String readErrorResponse(HttpURLConnection conn) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            return "No additional error details available.";
        }
    }
}


