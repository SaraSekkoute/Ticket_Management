package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

public class AuditForm extends JFrame {
    private JTable auditlogTable;

    public AuditForm() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        auditlogTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(auditlogTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchAuditLogs();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchAuditLogs() {
        SwingUtilities.invokeLater(() -> {
            try {
                URL url = new URL("http://localhost:8082/api/audit-logs");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    JOptionPane.showMessageDialog(this, "Failed to fetch auditlogs: " + conn.getResponseCode());
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

                String responseString = response.toString();
                System.out.println("JSON brut : " + responseString);

                List<AuditLog> auditlogs = objectMapper.readValue(responseString,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, AuditLog.class));
                displayAuditLogs(auditlogs);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching auditlogs: " + e.getMessage());
            }
        });
    }

    private void displayAuditLogs(List<AuditLog> auditLogs) {
        String[] columns = {"Actions", "Details","TimesTamp"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (AuditLog auditLog : auditLogs) {
            Object[] row = {
                    auditLog.getAction(),
                    auditLog.getDetails(),
            };
            tableModel.addRow(row);
        }

        auditlogTable.setModel(tableModel);
    }


}
