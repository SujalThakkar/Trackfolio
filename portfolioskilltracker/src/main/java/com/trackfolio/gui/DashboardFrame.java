package com.trackfolio.gui;

import com.trackfolio.db.CertificateDAO;
import com.trackfolio.db.RatingHistoryDAO;
import com.trackfolio.db.SkillDAO;
import com.trackfolio.db.UserDAO;
import com.trackfolio.model.Certificate;
import com.trackfolio.model.RatingHistory;
import com.trackfolio.model.Skill;
import com.trackfolio.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class DashboardFrame extends JFrame {

    private User user;
    private SkillsSection skillsSection;
    private CertificatesSection certificatesSection;
    private JTextField findField;

    public DashboardFrame(User user) {
        this.user = user;
        setTitle("Portfolio Dashboard - Trackfolio");
        setSize(1000, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        try {
            initComponents();
            System.out.println("DashboardFrame initialized for user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("Error initializing DashboardFrame: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading dashboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true);
    }

    private void initComponents() {
        // Color scheme
        Color headerColor = new Color(13, 71, 161); // Dark Blue
        Color contentColor = Color.WHITE;
        Color buttonColor = new Color(40, 167, 69); // Green
        Color buttonHoverColor = new Color(46, 125, 50); // Darker Green
        Color textColor = new Color(33, 33, 33); // Dark Gray
        Color borderColor = new Color(100, 100, 100); // Gray

        // --- Top: Navbar ---
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(headerColor);
        navbar.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Left: Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        navbar.add(welcomeLabel, BorderLayout.WEST);

        // Right: Find Person and Logout Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(headerColor);

        findField = new JTextField(15);
        findField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        findField.setBorder(new LineBorder(borderColor, 1));
        buttonPanel.add(findField);

        JButton findBtn = new JButton("Find Person");
        styleButton(findBtn, buttonColor, buttonHoverColor);
        buttonPanel.add(findBtn);

        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn, new Color(220, 20, 60), new Color(200, 40, 80)); // Red
        buttonPanel.add(logoutBtn);

        navbar.add(buttonPanel, BorderLayout.EAST);
        add(navbar, BorderLayout.NORTH);

        // --- Left: Sidebar ---
        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        // Add Skill Button
        JButton addSkillBtn = new JButton("Add Skill");
        styleButton(addSkillBtn, buttonColor, buttonHoverColor);
        sidebar.add(addSkillBtn, gbc);

        // Add Certificate Button
        gbc.gridy++;
        JButton addCertBtn = new JButton("Add Certificate");
        styleButton(addCertBtn, buttonColor, buttonHoverColor);
        sidebar.add(addCertBtn, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        sidebar.add(new JLabel(""), gbc); // Spacer
        add(sidebar, BorderLayout.WEST);

        // --- Center: Main Content ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(contentColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Left: Skills Section
        skillsSection = new SkillsSection();
        JPanel skillsPanel = new JPanel(new BorderLayout());
        skillsPanel.setBackground(contentColor);
        skillsPanel.add(skillsSection, BorderLayout.NORTH);

        // Right: Certificates Section
        certificatesSection = new CertificatesSection();
        JPanel certsPanel = new JPanel(new BorderLayout());
        certsPanel.setBackground(contentColor);
        certsPanel.add(certificatesSection, BorderLayout.NORTH);

        // Add vertical divider
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, skillsPanel, certsPanel);
        splitPane.setDividerLocation(500); // Adjust as needed
        splitPane.setBackground(contentColor);
        splitPane.setBorder(null);

        // Summary Box
        JPanel summaryBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        summaryBox.setBackground(new Color(245, 245, 245));
        summaryBox.setBorder(BorderFactory.createTitledBorder(new LineBorder(headerColor), "Summary"));
        JLabel summaryLabel = new JLabel("Username: " + user.getUsername() +
                " | Skills: " + user.getSkills().size() +
                " | Certificates: " + user.getCertificates().size());
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        summaryLabel.setForeground(textColor);
        summaryBox.add(summaryLabel);
        mainPanel.add(summaryBox, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Button Actions
        findBtn.addActionListener(e -> findPerson());
        addSkillBtn.addActionListener(e -> openAddSkillFrame());
        addCertBtn.addActionListener(e -> openAddCertificateFrame());
        logoutBtn.addActionListener(e -> logout());
    }

    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void findPerson() {
        try {
            String username = findField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            UserDAO userDAO = new UserDAO();
            User found = userDAO.getUserByUsername(username);
            if (found != null) {
                PortfolioFrame portfolioFrame = new PortfolioFrame(found);
                portfolioFrame.setVisible(true);
                portfolioFrame.requestFocus(); // Ensure the new frame gains focus
                JOptionPane.showMessageDialog(this, "Displaying " + username + "'s portfolio!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Error finding person: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error finding person: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddSkillFrame() {
        try {
            AddSkillFrame addSkillFrame = new AddSkillFrame(user);
            addSkillFrame.setVisible(true);
            addSkillFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshDashboard();
                }
            });
        } catch (Exception e) {
            System.err.println("Error opening AddSkillFrame: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening Add Skill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddCertificateFrame() {
        try {
            AddCertificateFrame addCertificateFrame = new AddCertificateFrame(user);
            addCertificateFrame.setVisible(true);
            addCertificateFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshDashboard();
                }
            });
        } catch (Exception e) {
            System.err.println("Error opening AddCertificateFrame: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening Add Certificate: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        try {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error logging out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshDashboard() {
        try {
            UserDAO userDAO = new UserDAO();
            User updatedUser = userDAO.getUserByUsername(user.getUsername());
            if (updatedUser != null) {
                this.user = updatedUser;
                System.out.println("Dashboard refreshed. Skills: " + user.getSkills().size() +
                        ", Certificates: " + user.getCertificates().size());
            } else {
                System.err.println("Error: Could not refresh user data for " + user.getUsername());
                JOptionPane.showMessageDialog(this, "Error refreshing user data.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JScrollPane scrollPane = (JScrollPane) getContentPane().getComponent(2);
            JPanel mainPanel = (JPanel) scrollPane.getViewport().getView();
            JPanel summaryBox = (JPanel) mainPanel.getComponent(0);
            summaryBox.removeAll();
            JLabel summaryLabel = new JLabel("Username: " + user.getUsername() +
                    " | Skills: " + user.getSkills().size() +
                    " | Certificates: " + user.getCertificates().size());
            summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            summaryBox.add(summaryLabel);
            skillsSection.updateSkills();
            certificatesSection.updateCertificates();
            revalidate();
            repaint();
        } catch (Exception e) {
            System.err.println("Error refreshing dashboard: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error refreshing dashboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ------------------- Abstract Section Class -------------------
    abstract class DashboardSection extends JPanel {
        protected String title;

        public DashboardSection(String title) {
            this.title = title;
            setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(13, 71, 161), 2), title,
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new Font("Segoe UI", Font.BOLD, 16)));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(10, 10, 10, 10));
        }

        public abstract void displayItems();
    }

    // ------------------- Skills Section -------------------
    class SkillsSection extends DashboardSection {
        public SkillsSection() {
            super("Skills");
            displayItems();
        }

        public void updateSkills() {
            displayItems();
        }

        @Override
        public void displayItems() {
            removeAll();
            List<Skill> skills = user.getSkills();
            if (skills == null || skills.isEmpty()) {
                JLabel noSkillsLabel = new JLabel("No skills added yet.");
                noSkillsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                noSkillsLabel.setForeground(new Color(33, 33, 33));
                add(noSkillsLabel);
            } else {
                for (Skill s : skills) {
                    JPanel skillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                    skillPanel.setBackground(Color.WHITE);
                    String display = s.getSkillName() + " (" + (s.isCoding() ? s.getRating() : s.getLevel()) + ")";
                    JLabel skillLabel = new JLabel(display);
                    skillLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    skillLabel.setForeground(new Color(33, 33, 33));
                    skillPanel.add(skillLabel);
                    JButton deleteBtn = new JButton("Delete");
                    styleButton(deleteBtn, new Color(220, 20, 60), new Color(200, 40, 80));
                    deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    deleteBtn.addActionListener(e -> deleteSkill(s.getSkillId()));
                    skillPanel.add(deleteBtn);
                    if (s.isCoding()) {
                        JButton updateBtn = new JButton("Update Rating");
                        styleButton(updateBtn, new Color(255, 165, 0), new Color(255, 140, 0)); // Orange
                        updateBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        updateBtn.addActionListener(e -> updateRating(s));
                        skillPanel.add(updateBtn);

                        JButton viewProgressBtn = new JButton("View Progress");
                        styleButton(viewProgressBtn, new Color(0, 123, 255), new Color(0, 105, 217)); // Blue
                        viewProgressBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        viewProgressBtn.addActionListener(e -> viewProgress(s.getSkillId()));
                        skillPanel.add(viewProgressBtn);
                    }
                    add(skillPanel);
                }
            }
            revalidate();
            repaint();
        }

        private void deleteSkill(int skillId) {
            try {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this skill?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    SkillDAO skillDAO = new SkillDAO();
                    if (skillDAO.deleteSkill(skillId)) {
                        JOptionPane.showMessageDialog(this, "Skill deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshDashboard();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error deleting skill.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error deleting skill: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting skill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void updateRating(Skill s) {
            try {
                String input = JOptionPane.showInputDialog(this, "Enter new rating:", s.getRating());
                if (input == null) return;
                int newRating = Integer.parseInt(input.trim());
                SkillDAO skillDAO = new SkillDAO();
                if (skillDAO.updateRating(s.getSkillId(), newRating)) {
                    RatingHistoryDAO historyDAO = new RatingHistoryDAO();
                    RatingHistory history = new RatingHistory(s.getSkillId(), newRating, LocalDate.now());
                    historyDAO.addRatingHistory(history);
                    JOptionPane.showMessageDialog(this, "Rating updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating rating.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid rating. Must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                System.err.println("Error updating rating: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating rating: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void viewProgress(int skillId) {
            try {
                ProgressFrame progressFrame = new ProgressFrame(skillId);
                progressFrame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error opening ProgressFrame: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening progress: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ------------------- Certificates Section -------------------
    class CertificatesSection extends DashboardSection {
        public CertificatesSection() {
            super("Certificates");
            displayItems();
        }

        public void updateCertificates() {
            displayItems();
        }

        @Override
        public void displayItems() {
            removeAll();
            List<Certificate> certificates = user.getCertificates();
            if (certificates == null || certificates.isEmpty()) {
                JLabel noCertsLabel = new JLabel("No certificates added yet.");
                noCertsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                noCertsLabel.setForeground(new Color(33, 33, 33));
                add(noCertsLabel);
            } else {
                for (Certificate c : certificates) {
                    JPanel certPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                    certPanel.setBackground(Color.WHITE);
                    JLabel certLabel = new JLabel(c.getCertName() + " - Issued by: " + c.getIssuedBy() + " (" + c.getIssueDate() + ")");
                    certLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    certLabel.setForeground(new Color(33, 33, 33));
                    certPanel.add(certLabel);
                    JButton viewBtn = new JButton("View");
                    styleButton(viewBtn, new Color(40, 167, 69), new Color(46, 125, 50));
                    viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    viewBtn.addActionListener(e -> viewCertificate(c));
                    certPanel.add(viewBtn);
                    JButton deleteBtn = new JButton("Delete");
                    styleButton(deleteBtn, new Color(220, 20, 60), new Color(200, 40, 80));
                    deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    deleteBtn.addActionListener(e -> deleteCertificate(c.getCertId()));
                    certPanel.add(deleteBtn);
                    add(certPanel);
                }
            }
            revalidate();
            repaint();
        }

        private void viewCertificate(Certificate c) {
            try {
                String filePath = c.getFilePath();
                File file = new File(filePath);
                if (file.exists()) {
                    if (filePath.toLowerCase().endsWith(".jpg") || filePath.toLowerCase().endsWith(".png")) {
                        ImageIcon icon = new ImageIcon(filePath);
                        if (icon.getIconWidth() > 0) {
                            Image scaledImage = icon.getImage().getScaledInstance(600, -1, Image.SCALE_SMOOTH);
                            JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(scaledImage)), c.getCertName(), JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid image file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Unsupported file format. Only JPG and PNG are supported.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "File not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.err.println("Error viewing certificate: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error viewing certificate: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void deleteCertificate(int certId) {
            try {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this certificate?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    CertificateDAO certDAO = new CertificateDAO();
                    if (certDAO.deleteCertificate(certId)) {
                        JOptionPane.showMessageDialog(this, "Certificate deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshDashboard();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error deleting certificate.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error deleting certificate: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting certificate: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}