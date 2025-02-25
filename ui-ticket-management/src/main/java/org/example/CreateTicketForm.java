package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

public class CreateTicketForm {
    private JFrame frame;
    private JTextField titleField;
    private JTextArea descriptionField;
    private JComboBox<String> priorityBox;
    private JComboBox<String> categoryBox;
    private Long employeeId;  // Récupère l'ID de l'employé

    public CreateTicketForm(Long employeeId) {
        this.employeeId = employeeId;

        frame = new JFrame("Create Ticket");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2));

        // Champs du formulaire
        frame.add(new JLabel("Title:"));
        titleField = new JTextField();
        frame.add(titleField);

        frame.add(new JLabel("Description:"));
        descriptionField = new JTextArea();
        frame.add(descriptionField);

        frame.add(new JLabel("Priority:"));
        String[] priorities = {"Low", "Medium", "High"};
        priorityBox = new JComboBox<>(priorities);
        frame.add(priorityBox);

        frame.add(new JLabel("Category:"));
        String[] categories = {"Network", "Hardware", "Software", "Other"};
        categoryBox = new JComboBox<>(categories);
        frame.add(categoryBox);

        JButton submitButton = new JButton("Submit");
        frame.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTicket();
            }
        });

        frame.setVisible(true);
    }

    private void createTicket() {
        try {
            URL url = new URL("http://localhost:8082/api/tickets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject ticketJson = new JSONObject();
            ticketJson.put("title", titleField.getText());
            ticketJson.put("description", descriptionField.getText());
            ticketJson.put("priorityLevel", priorityBox.getSelectedItem().toString());
            ticketJson.put("statusTracking", "NEW"); // Assurez-vous d'envoyer une valeur valide pour le status
            // Récupération du nom de la catégorie sélectionnée
            String selectedCategoryName = (String) categoryBox.getSelectedItem();
            Long categoryId = getCategoryIdByName(selectedCategoryName);

            if (categoryId != null) {
                ticketJson.put("categoryticketId", categoryId);
            } else {
                ticketJson.put("categoryticketId", JSONObject.NULL);
            }

            ticketJson.put("employeeId", employeeId);
            OutputStream os = conn.getOutputStream();
            os.write(ticketJson.toString().getBytes(StandardCharsets.UTF_8));
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response from server: " + response.toString());

            if (conn.getResponseCode() == 201) {
                JOptionPane.showMessageDialog(frame, "Ticket Created Successfully!");
                frame.dispose();
            } else {
                // Affiche le message d'erreur détaillé
                JOptionPane.showMessageDialog(frame, "Failed to create ticket. Response: " + response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private Long getCategoryIdByName(String categoryName) {
        try {
            URL url = new URL("http://localhost:8082/api/category-tickets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            // Parser la réponse JSON
            JSONArray categoriesArray = new JSONArray(response.toString());

            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject categoryObject = categoriesArray.getJSONObject(i);
                String name = categoryObject.getString("categoryName");

                if (name.equalsIgnoreCase(categoryName)) {
                    return categoryObject.getLong("categoryid");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
