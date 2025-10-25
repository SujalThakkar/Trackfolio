package com.trackfolio.gui;

import com.trackfolio.db.UserDAO;
import com.trackfolio.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame {

    private JTextField usernameField, emailField, fullNameField;
    private JPasswordField passwordField;
    private JButton registerButton, backButton;

    public RegisterFrame() {
        setTitle("Register - Portfolio Skill Tracker");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel fullNameLabel = new JLabel("Full Name:");
        panel.add(fullNameLabel, gbc);

        gbc.gridx = 1;
        fullNameField = new JTextField(15);
        panel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registerButton = new JButton("Register");
        registerButton.addActionListener(this::registerAction);
        panel.add(registerButton, gbc);

        gbc.gridx = 1;
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        panel.add(backButton, gbc);

        add(panel);
    }

    private void registerAction(ActionEvent e) {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String email = emailField.getText().trim();
            String fullName = fullNameField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User(0, username, password, email, fullName);
            UserDAO userDAO = new UserDAO();
            if (userDAO.registerUser(user)) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error registering user. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("Error during registration: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during registration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}