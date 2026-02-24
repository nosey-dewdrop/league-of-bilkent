package tools;

import model.*;
import model.Event;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PosterGenerator {
    private static final int WIDTH = 400, HEIGHT = 500;
    private static final Random rand = new Random();
    private static final Color[] PASTELS = {
        new Color(0xE8, 0xD5, 0xFF), new Color(0xFF, 0xD5, 0xE5), new Color(0xD5, 0xF5, 0xD0),
        new Color(0xD5, 0xE8, 0xFF), new Color(0xFF, 0xE8, 0xD5), new Color(0xD5, 0xFF, 0xF0),
        new Color(0xFF, 0xF0, 0xD5), new Color(0xF0, 0xD5, 0xFF), new Color(0xD5, 0xFF, 0xE8),
        new Color(0xFF, 0xD5, 0xD5), new Color(0xE0, 0xD5, 0xFF), new Color(0xD5, 0xF0, 0xFF)
    };

    private static int pickColor(ArrayList<String> tags) {
        if (tags == null || tags.isEmpty()) return rand.nextInt(PASTELS.length);
        String t = tags.get(0).toLowerCase();
        if (t.contains("software") || t.contains("ai") || t.contains("python")) return 3;
        if (t.contains("algorithm") || t.contains("cyber")) return 10;
        if (t.contains("music") || t.contains("concert")) return 7;
        if (t.contains("sport") || t.contains("fitness")) return 2;
        if (t.contains("art") || t.contains("photo")) return 1;
        if (t.contains("game")) return 4;
        if (t.contains("food") || t.contains("social")) return 5;
        if (t.contains("environment")) return 8;
        if (t.contains("cinema") || t.contains("theater")) return 9;
        if (t.contains("education") || t.contains("workshop")) return 0;
        return rand.nextInt(PASTELS.length);
    }

    public static String generate(String title, ArrayList<String> tags, String dateStr, String location, int xp) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color bg = PASTELS[pickColor(tags)];
        Color bgDarker = new Color(
            Math.max(bg.getRed() - 30, 0),
            Math.max(bg.getGreen() - 30, 0),
            Math.max(bg.getBlue() - 30, 0));

        // Gradient background
        GradientPaint gp = new GradientPaint(0, 0, bg, WIDTH, HEIGHT, bgDarker);
        g.setPaint(gp);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Decorative circles
        g.setColor(new Color(255, 255, 255, 35));
        g.fillOval(WIDTH - 100, -30, 180, 180);
        g.fillOval(-40, HEIGHT - 80, 140, 140);
        g.setColor(new Color(255, 255, 255, 20));
        g.fillOval(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 100);
        g.fillOval(30, 40, 60, 60);
        g.fillOval(WIDTH - 60, HEIGHT - 160, 70, 70);

        // Sparkle dots
        g.setColor(new Color(255, 255, 255, 40));
        Random r = new Random(title.hashCode());
        for (int i = 0; i < 15; i++) {
            int x = r.nextInt(WIDTH);
            int y = r.nextInt(HEIGHT);
            int s = 2 + r.nextInt(4);
            g.fillOval(x, y, s, s);
        }

        // Text color - darker version of bg
        Color textColor = new Color(
            Math.max(bg.getRed() - 100, 20),
            Math.max(bg.getGreen() - 100, 20),
            Math.max(bg.getBlue() - 100, 20));
        Color textLight = new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 180);

        // Small top label
        g.setColor(textLight);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.drawString("\u2728 squirrel event", 30, 40);

        // Event title - word wrap
        g.setColor(textColor);
        g.setFont(new Font("SansSerif", Font.BOLD, 32));
        FontMetrics fm = g.getFontMetrics();
        String[] words = title.split(" ");
        StringBuilder line = new StringBuilder();
        int y = 90;
        for (String word : words) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(test) > WIDTH - 60) {
                g.drawString(line.toString(), 30, y);
                y += fm.getHeight();
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (line.length() > 0) g.drawString(line.toString(), 30, y);

        // Bottom section - location and date
        int bottomY = HEIGHT - 100;

        // Divider line
        g.setColor(new Color(255, 255, 255, 60));
        g.fillRect(30, bottomY, WIDTH - 60, 1);

        g.setColor(textColor);
        g.setFont(new Font("SansSerif", Font.PLAIN, 15));

        if (location != null && !location.isEmpty()) {
            g.drawString("\uD83D\uDCCD " + location, 30, bottomY + 30);
        }
        if (dateStr != null && !dateStr.isEmpty()) {
            g.drawString("\uD83D\uDCC5 " + dateStr, 30, bottomY + 55);
        }

        if (xp > 0) {
            String xpText = "+" + xp + " XP";
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            fm = g.getFontMetrics();
            int xpW = fm.stringWidth(xpText) + 16;
            g.setColor(new Color(255, 255, 255, 120));
            g.fillRoundRect(WIDTH - xpW - 30, bottomY + 40, xpW, 24, 12, 12);
            g.setColor(textColor);
            g.drawString(xpText, WIDTH - xpW - 22, bottomY + 57);
        }

        // Butterfly decoration
        g.setColor(new Color(255, 255, 255, 50));
        drawButterfly(g, WIDTH - 50, 30, 0.7f);
        drawButterfly(g, 60, HEIGHT - 140, 0.5f);

        g.dispose();
        try {
            File dir = new File("posters");
            dir.mkdirs();
            String fname = "posters/poster_" + System.currentTimeMillis() + "_" + rand.nextInt(9999) + ".png";
            javax.imageio.ImageIO.write(img, "png", new File(fname));
            return fname;
        } catch (Exception e) { return null; }
    }

    private static void drawButterfly(Graphics2D g2, int x, int y, float alpha) {
        g2.setColor(new Color(255, 255, 255, (int) (alpha * 80)));
        g2.fillOval(x - 12, y - 7, 12, 14);
        g2.fillOval(x + 2, y - 7, 12, 14);
        g2.setColor(new Color(255, 255, 255, (int) (alpha * 120)));
        g2.fillOval(x - 1, y - 5, 3, 12);
    }

    public static String generateDefault(Event ev) {
        return generate(ev.getTitle(), ev.getTags(), ev.getDateStr(), ev.getLocation(), ev.getXpReward());
    }
}
