package com.trackfolio.gui;

import com.trackfolio.db.RatingHistoryDAO;
import com.trackfolio.db.SkillDAO;
import com.trackfolio.model.RatingHistory;
import com.trackfolio.model.Skill;
import com.trackfolio.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class AddSkillFrame extends JFrame {

    private User loggedInUser;
    private JTextField skillField;
    private JComboBox<String> levelComboBox;
    private JButton saveButton, backButton;
    private JCheckBox isCodingCheck;
    private JLabel levelLabel, ratingLabel;
    private JTextField ratingField;

    public AddSkillFrame(User user) {
        this.loggedInUser = user;
        setTitle("Add Skill - Portfolio Skill Tracker");
        setSize(500, 350);
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
        isCodingCheck = new JCheckBox("Is Coding Skill?");
        panel.add(isCodingCheck, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        levelLabel = new JLabel("Skill Level:");
        panel.add(levelLabel, gbc);

        gbc.gridx = 1;
        levelComboBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        panel.add(levelComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        ratingLabel = new JLabel("Rating:");
        ratingLabel.setVisible(false);
        panel.add(ratingLabel, gbc);

        gbc.gridx = 1;
        ratingField = new JTextField(20);
        ratingField.setVisible(false);
        panel.add(ratingField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        saveButton = new JButton("Save Skill");
        saveButton.addActionListener(this::saveSkill);
        panel.add(saveButton, gbc);

        gbc.gridx = 1;
        backButton = new JButton("Back");
        backButton.addActionListener(this::backToDashboard);
        panel.add(backButton, gbc);

        isCodingCheck.addActionListener(e -> {
            boolean isCoding = isCodingCheck.isSelected();
            levelLabel.setVisible(!isCoding);
            levelComboBox.setVisible(!isCoding);
            ratingLabel.setVisible(isCoding);
            ratingField.setVisible(isCoding);
        });

        add(panel);
    }

    private void saveSkill(ActionEvent e) {
        try {
            String skillName = skillField.getText().trim();

            if (skillName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a skill name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isCoding = isCodingCheck.isSelected();
            String level = null;
            Integer rating = null;

            if (isCoding) {
                String ratingText = ratingField.getText().trim();
                if (ratingText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a rating.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    rating = Integer.parseInt(ratingText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid rating. Must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                level = (String) levelComboBox.getSelectedItem();
            }

            Skill skill = new Skill(loggedInUser.getUserId(), skillName, level, isCoding, rating);
            SkillDAO skillDAO = new SkillDAO();
            int skillId = skillDAO.addSkill(skill);
            if (skillId != -1) {
                if (isCoding) {
                    RatingHistoryDAO historyDAO = new RatingHistoryDAO();
                    RatingHistory history = new RatingHistory(skillId, rating, LocalDate.now());
                    historyDAO.addRatingHistory(history);
                }
                JOptionPane.showMessageDialog(this, "Skill added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                skillField.setText("");
                ratingField.setText("");
                isCodingCheck.setSelected(false);
                levelLabel.setVisible(true);
                levelComboBox.setVisible(true);
                ratingLabel.setVisible(false);
                ratingField.setVisible(false);
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