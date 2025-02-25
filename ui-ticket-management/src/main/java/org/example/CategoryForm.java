package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryForm {
    public CategoryForm() {
        JFrame categoryFrame = new JFrame("Category Creation");
        categoryFrame.setSize(400, 200);
        categoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        categoryFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JLabel categoryLabel = new JLabel("Category Name:");
        JTextField categoryField = new JTextField();

        JButton submitButton = new JButton("Create Category");

        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoryName = categoryField.getText();
                JOptionPane.showMessageDialog(categoryFrame, "Category Created: " + categoryName);
            }
        });

        categoryFrame.add(panel);
        categoryFrame.setVisible(true);
    }
}
