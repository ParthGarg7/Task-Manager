package com.taskmanager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelpWindow extends JFrame {

    public HelpWindow() {
        setTitle("Help & Support");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // FAQs Panel
        JPanel faqPanel = new JPanel();
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));

        String[][] faqs = {
            {"How do I create a task?", "Click the 'Add Task' button on the dashboard and fill in the task details."},
            {"Can I edit a task?", "Yes, select the task and click 'Edit' to change its details."},
            {"How do I delete a task?", "Select the task and click the 'Delete' button to remove it."},
            {"How can I track completed tasks?", "Completed tasks are shown in the 'Completed Tasks' section."}
        };

        for (String[] faq : faqs) {
            faqPanel.add(createCollapsiblePanel(faq[0], faq[1]));
        }

        JScrollPane faqScroll = new JScrollPane(faqPanel);
        tabbedPane.add("FAQs", faqScroll);

        // Support Panel
        JPanel supportPanel = new JPanel(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField contactField = new JTextField(20);
        JTextArea messageArea = new JTextArea(5, 20);
        JButton submitButton = new JButton("Submit");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Your Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Your Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Your Contact Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Message:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(messageArea), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();
            String message = messageArea.getText().trim();

            if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Support request submitted. We'll get back to you soon.", "Submitted", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Contact Support Section (Formatted)
        JPanel contactInfoPanel = new JPanel(new GridBagLayout());
        contactInfoPanel.setBorder(BorderFactory.createTitledBorder("Contact Support Team"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 5, 10);
        c.anchor = GridBagConstraints.WEST;

        String[][] contacts = {
            {"Akshat Agrawal", "akshatagrawal1976@gmail.com", "6367750560"},
            {"Manu Yadav", "manuy3711@gmail.com", "9555518207"},
            {"Parth Garg", "parthgarg750@gmail.com", "7983655087"},
            {"Manvi Sethi", "manvi.120341@stu.upes.ac.in", "9999630365"}
        };

        for (int i = 0; i < contacts.length; i++) {
            c.gridx = 0; c.gridy = i;
            contactInfoPanel.add(new JLabel("Name:"), c);
            c.gridx = 1;
            contactInfoPanel.add(new JLabel(contacts[i][0]), c);

            c.gridx = 2;
            contactInfoPanel.add(new JLabel("Email:"), c);
            c.gridx = 3;
            contactInfoPanel.add(new JLabel(contacts[i][1]), c);

            c.gridx = 4;
            contactInfoPanel.add(new JLabel("Contact Number:"), c);
            c.gridx = 5;
            contactInfoPanel.add(new JLabel(contacts[i][2]), c);
        }

        // Add to support panel
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(formPanel);
        wrapper.add(Box.createVerticalStrut(20));
        wrapper.add(contactInfoPanel);

        JScrollPane supportScroll = new JScrollPane(wrapper);
        tabbedPane.add("Support", supportScroll);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCollapsiblePanel(String title, String content) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton toggleButton = new JButton(title);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setVisible(false);

        toggleButton.addActionListener(e -> contentArea.setVisible(!contentArea.isVisible()));

        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HelpWindow().setVisible(true));
    }
}
