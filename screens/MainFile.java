package screens;

import model.*;
import panels.*;
import tools.*;
import javax.swing.*;

/*
 * ┌──────────────────────────────────────────────────────────────┐
 * │                   <<class>> MainFile                         │
 * │                Application entry point                       │
 * ├──────────────────────────────────────────────────────────────┤
 * │ + currentUser: User (static) -> currently logged-in user     │
 * │ + loginScreen: LoginScreen (static) -> login window ref      │
 * ├──────────────────────────────────────────────────────────────┤
 * │ + main(args) -> sets L&F, connects DB, loads sample data     │
 * │   if empty, then shows LoginScreen                           │
 * ├──────────────────────────────────────────────────────────────┤
 * │ USES:    Database, SampleData, LoginScreen                   │
 * │ USED BY: HomeScreen (logout), LoginScreen (login success),   │
 * │          all panels (MainFile.currentUser)                    │
 * └──────────────────────────────────────────────────────────────┘
 */
public class MainFile {

    public static User currentUser;
    public static LoginScreen loginScreen;

    public static void main(String[] args) {
        try {
            // Use system look & feel for modern appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Anti-aliased text
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception ignored) {}

        Database.createConnection();

        if (Database.isDatabaseEmpty()) {
            SampleData.loadSampleData();
        }

        SwingUtilities.invokeLater(() -> {
            loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}
