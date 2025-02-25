package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 30, 80, 25);
        frame.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 30, 160, 25);
        frame.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 70, 80, 25);
        frame.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 70, 160, 25);
        frame.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 110, 160, 25);
        frame.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                authenticateUser(username, password);
            }
        });

        frame.setVisible(true);
    }

    private void authenticateUser(String username, String password) {
        Employee employee = ApiService.authenticate(username, password);

        if (employee != null) {
            frame.dispose();
            if ("EMPLOYEE".equals(employee.getRole())) {
                new EmployeeDashboard(employee.getId());
            } else if ("IT_SUPPORT".equals(employee.getRole())) {
                new ITSupportDashboard(employee.getId());
            } else {
                JOptionPane.showMessageDialog(null, "Invalid role");
                frame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid credentials");
            frame.setVisible(true);
        }
    }


}
