package com.trackfolio.gui;

import com.trackfolio.db.SkillDAO;
import com.trackfolio.model.Skill;
import com.trackfolio.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddSkillFrame extends JFrame {

    private User loggedInUser;
    private JTextField skillField;
    private JComboBox<String> levelComboBox;
    private JButton saveButton, backButton;

    public AddSkillFrame(User user) {
        this.loggedInUser = user;
        setTitle("Add Skill - Portfolio Skill Tracker");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            initComponents();
            System.out.println("AddSkillFrame initialized for user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("Error initializing AddSkillFrame: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing Add Skill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add Skill");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel skillLabel = new JLabel("Skill Name:");
        panel.add(skillLabel, gbc);

        gbc.gridx = 1;
        skillField = new JTextField(20);
        panel.add(skillField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel levelLabel = new JLabel("Skill Level:");
        panel.add(levelLabel, gbc);

        gbc.gridx = 1;
        levelComboBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        panel.add(levelComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        saveButton = new JButton("Save Skill");
        saveButton.addActionListener(this::saveSkill);
        panel.add(saveButton, gbc);

        gbc.gridx = 1;
        backButton = new JButton("Back");
        backButton.addActionListener(this::backToDashboard);
        panel.add(backButton, gbc);

        add(panel);
    }

    private void saveSkill(ActionEvent e) {
        try {
            String skillName = skillField.getText().trim();
            String level = (String) levelComboBox.getSelectedItem();

            if (skillName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a skill name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Skill skill = new Skill(loggedInUser.getUserId(), skillName, level);
            SkillDAO skillDAO = new SkillDAO();
            boolean success = skillDAO.addSkill(skill);
            if (success) {
                JOptionPane.showMessageDialog(this, "Skill added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                skillField.setText("");
                System.out.println("Skill added to database: " + skill);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding skill.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Failed to add skill to database: " + skill);
            }
        } catch (Exception ex) {
            System.err.println("Error saving skill: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving skill: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToDashboard(ActionEvent e) {
        this.dispose();
    }
}