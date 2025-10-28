package com.trackfolio.gui;

import com.trackfolio.db.RatingHistoryDAO;
import com.trackfolio.model.RatingHistory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProgressFrame extends JFrame {

    private int skillId;

    public ProgressFrame(int skillId) {
        this.skillId = skillId;
        setTitle("Skill Progress");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new ProgressPanel());
    }

    class ProgressPanel extends JPanel {
        private List<RatingHistory> history;

        public ProgressPanel() {
            RatingHistoryDAO dao = new RatingHistoryDAO();
            history = dao.getRatingHistoryBySkill(skillId);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (history.isEmpty()) {
                g.drawString("No history available", 20, 20);
                return;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int graphWidth = width - 2 * padding;
            int graphHeight = height - 2 * padding;

            // Draw axes
            g2d.drawLine(padding, height - padding, padding, padding);
            g2d.drawLine(padding, height - padding, width - padding, height - padding);

            // Find min/max rating
            int minRating = Integer.MAX_VALUE;
            int maxRating = Integer.MIN_VALUE;
            for (RatingHistory rh : history) {
                minRating = Math.min(minRating, rh.getRating());
                maxRating = Math.max(maxRating, rh.getRating());
            }
            if (minRating == maxRating) {
                minRating -= 1;
                maxRating += 1;
            }

            // Y labels
            for (int i = 0; i <= 5; i++) {
                int y = height - padding - (i * graphHeight / 5);
                int val = minRating + i * (maxRating - minRating) / 5;
                g2d.drawString(String.valueOf(val), padding - 20, y + 5);
            }

            // X labels (dates)
            for (int i = 0; i < history.size(); i++) {
                int x = padding + i * graphWidth / (history.size() - 1);
                g2d.drawString(history.get(i).getUpdateDate().toString(), x - 30, height - padding + 20);
            }

            // Plot lines
            g2d.setColor(Color.BLUE);
            for (int i = 0; i < history.size() - 1; i++) {
                int x1 = padding + i * graphWidth / (history.size() - 1);
                int y1 = height - padding - (history.get(i).getRating() - minRating) * graphHeight / (maxRating - minRating);
                int x2 = padding + (i + 1) * graphWidth / (history.size() - 1);
                int y2 = height - padding - (history.get(i + 1).getRating() - minRating) * graphHeight / (maxRating - minRating);
                g2d.drawLine(x1, y1, x2, y2);
            }

            // Plot points
            for (int i = 0; i < history.size(); i++) {
                int x = padding + i * graphWidth / (history.size() - 1);
                int y = height - padding - (history.get(i).getRating() - minRating) * graphHeight / (maxRating - minRating);
                g2d.fillOval(x - 5, y - 5, 10, 10);
            }
        }
    }
}
