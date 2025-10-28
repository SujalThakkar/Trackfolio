package com.trackfolio.gui;

import com.trackfolio.model.Certificate;
import com.trackfolio.model.Skill;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class PortfolioFrame extends JFrame {

    private com.trackfolio.model.User user;
    private SkillsSection skillsSection;
    private CertificatesSection certificatesSection;

    public PortfolioFrame(com.trackfolio.model.User user) {
        this.user = user;
        setTitle(user.getUsername() + "'s Portfolio - Trackfolio");
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        try {
            initComponents();
            System.out.println("PortfolioFrame initialized for user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("Error initializing PortfolioFrame: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading portfolio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true);
    }

    private void initComponents() {
        // Color scheme
        Color headerColor = new Color(13, 71, 161); // Dark Blue
        Color contentColor = Color.WHITE;
        Color textColor = new Color(33, 33, 33); // Dark Gray

        // --- Top: Header ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        header.setBackground(headerColor);
        header.setBorder(new EmptyBorder(10, 15, 10, 15));
        JLabel titleLabel = new JLabel(user.getUsername() + "'s Portfolio");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel);
        add(header, BorderLayout.NORTH);

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
        splitPane.setDividerLocation(400); // Adjust as needed
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
                    add(skillPanel);
                }
            }
            revalidate();
            repaint();
        }
    }

    // ------------------- Certificates Section -------------------
    class CertificatesSection extends DashboardSection {
        public CertificatesSection() {
            super("Certificates");
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
                    viewBtn.setBackground(new Color(40, 167, 69));
                    viewBtn.setForeground(Color.BLACK);
                    viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    viewBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
                    viewBtn.addActionListener(e -> viewCertificate(c));
                    certPanel.add(viewBtn);
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
    }
}