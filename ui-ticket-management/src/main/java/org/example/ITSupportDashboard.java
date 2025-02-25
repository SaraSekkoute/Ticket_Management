package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ITSupportDashboard {
    private JFrame frame;
    private JTextArea ticketDisplay;
    private long employeeId;


    public ITSupportDashboard(long employeeId) {
        this.employeeId = employeeId;
        frame = new JFrame("IT Support Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu ticketMenu = new JMenu("Tickets");
        JMenuItem createTicketItem = new JMenuItem("Create Ticket");
        JMenuItem viewyourTicketsItem = new JMenuItem("View All Your Tickets");

        ticketMenu.add(createTicketItem);
        ticketMenu.add(viewyourTicketsItem);

        JMenu employeeTicketMenu = new JMenu("Ticket Employee");
        JMenuItem viewEmployeeTicketsItem = new JMenuItem("View Employee Tickets");
        employeeTicketMenu.add(viewEmployeeTicketsItem);

        menuBar.add(ticketMenu);
        menuBar.add(employeeTicketMenu);



        JMenu logMenu = new JMenu("Logs Tickets");
        JMenuItem viewLogsItem = new JMenuItem("View Logs Tickets");
        logMenu.add(viewLogsItem);

        menuBar.add(ticketMenu);
        menuBar.add(logMenu);



        frame.setJMenuBar(menuBar);

        ticketDisplay = new JTextArea();
        ticketDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ticketDisplay);
        frame.add(scrollPane, BorderLayout.CENTER);


        frame.setVisible(true);

 //creat own tickets
        createTicketItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateTicketForm(employeeId);
            }
        });

        viewyourTicketsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ListMyTicketForm(employeeId);
            }
        });



        viewEmployeeTicketsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ListEmployeeTicketForm();
            }
        });



        viewLogsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AuditForm();
            }
        });


    }



//    private void deleteTicket(String ticketId) {
//        try {
//            URL url = new URL("http://localhost:8082/api/tickets/" + ticketId);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("DELETE");
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == 200 || responseCode == 204) {
//                JOptionPane.showMessageDialog(frame, "Ticket deleted successfully");
//
//            } else {
//                JOptionPane.showMessageDialog(frame, "Failed to delete ticket: " + responseCode);
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(frame, "Error deleting ticket: " + e.getMessage());
//        }
//    }
}
