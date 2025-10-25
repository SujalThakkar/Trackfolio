package com.trackfolio.gui;

import com.trackfolio.db.CertificateDAO;
import com.trackfolio.model.Certificate;
import com.trackfolio.model.User;
import com.trackfolio.util.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;

public class AddCertificateFrame extends JFrame {

    private User loggedInUser;
    private JTextField certNameField, issuedByField;
    private JButton selectFileButton, saveButton, backButton;
    private JLabel selectedFileLabel;
    private File selectedFile;

    public AddCertificateFrame(User user) {
        this.loggedInUser = user;
        setTitle("Add Certificate - Portfolio Skill Tracker");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            initComponents();
            System.out.println("AddCertificateFrame initialized for user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("Error initializing AddCertificateFrame: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing Add Certificate: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add Certificate");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 33, 33));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel certNameLabel = new JLabel("Certificate Name:");
        certNameLabel.setForeground(new Color(33, 33, 33));
        panel.add(certNameLabel, gbc);

        gbc.gridx = 1;
        certNameField = new JTextField(20);
        panel.add(certNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel issuedByLabel = new JLabel("Issued By:");
        issuedByLabel.setForeground(new Color(33, 33, 33));
        panel.add(issuedByLabel, gbc);

        gbc.gridx = 1;
        issuedByField = new JTextField(20);
        panel.add(issuedByField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        selectFileButton = new JButton("Select File");
        selectFileButton.setBackground(new Color(40, 167, 69));
        selectFileButton.setForeground(Color.BLACK);
        selectFileButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectFileButton.addActionListener(this::selectFileAction);
        panel.add(selectFileButton, gbc);

        gbc.gridx = 1;
        selectedFileLabel = new JLabel("No file selected");
        selectedFileLabel.setForeground(new Color(33, 33, 33));
        panel.add(selectedFileLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        saveButton = new JButton("Save Certificate");
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(this::saveCertificate);
        panel.add(saveButton, gbc);

        gbc.gridx = 1;
        backButton = new JButton("Back");
        backButton.setBackground(new Color(220, 20, 60));
        backButton.setForeground(Color.BLACK);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(this::backToDashboard);
        panel.add(backButton, gbc);

        panel.setBackground(Color.WHITE);
        add(panel);
    }

    private void selectFileAction(ActionEvent e) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png"));
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null && selectedFile.exists()) {
                    selectedFileLabel.setText(selectedFile.getName());
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                } else {
                    selectedFileLabel.setText("Invalid file selected");
                    selectedFile = null;
                    System.err.println("Invalid file selected in JFileChooser");
                    JOptionPane.showMessageDialog(this, "Please select a valid file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error selecting file: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error selecting file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCertificate(ActionEvent e) {
        try {
            String certName = certNameField.getText().trim();
            String issuedBy = issuedByField.getText().trim();

            if (certName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Certificate name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (issuedBy.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Issued by cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedFile == null || !selectedFile.exists()) {
                JOptionPane.showMessageDialog(this, "Please select a valid file.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String savedFilePath = FileHandler.saveCertificateFile(loggedInUser.getUsername(), selectedFile);
            if (savedFilePath == null) {
                JOptionPane.showMessageDialog(this, "Error saving file. Check permissions or directory.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("FileHandler.saveCertificateFile returned null for user: " + loggedInUser.getUsername());
                return;
            }
            System.out.println("File saved at: " + savedFilePath);

            Certificate certificate = new Certificate(
                    loggedInUser.getUserId(),
                    certName,
                    issuedBy,
                    LocalDate.now(),
                    savedFilePath
            );

            CertificateDAO certDAO = new CertificateDAO();
            boolean success = certDAO.addCertificate(certificate);
            if (success) {
                JOptionPane.showMessageDialog(this, "Certificate added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                certNameField.setText("");
                issuedByField.setText("");
                selectedFile = null;
                selectedFileLabel.setText("No file selected");
                System.out.println("Certificate added to database: " + certificate);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding certificate to database.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Failed to add certificate to database: " + certificate);
            }
        } catch (Exception ex) {
            System.err.println("Error saving certificate: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving certificate: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToDashboard(ActionEvent e) {
        this.dispose();
    }
}