package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeForm {
    public EmployeeForm() {
        JFrame employeeFrame = new JFrame("Employee Registration");
        employeeFrame.setSize(400, 250);
        employeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        employeeFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Employee Name:");
        JTextField nameField = new JTextField();

        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField();

        JButton submitButton = new JButton("Register Employee");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(roleLabel);
        panel.add(roleField);
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String role = roleField.getText();
                JOptionPane.showMessageDialog(employeeFrame, "Employee Registered:\n" +
                        "Name: " + name + "\n" +
                        "Role: " + role);
            }
        });

        employeeFrame.add(panel);
        employeeFrame.setVisible(true);
    }
}
