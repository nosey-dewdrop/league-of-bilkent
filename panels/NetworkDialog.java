package panels;

import javax.swing.*;
import java.awt.*;
import tools.NetworkManager;
import model.AppConstants;
import screens.LoginScreen;

public class NetworkDialog extends JDialog {
    private LoginScreen parent;

    public NetworkDialog(LoginScreen parent) {
        super(parent, "Network Settings", true);
        this.parent = parent;
        setSize(320, 220);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Network Mode");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        p.add(title);
        p.add(Box.createVerticalStrut(15));

        JRadioButton btnClient = new JRadioButton("Client Mode");
        JRadioButton btnHost = new JRadioButton("Host Mode");
        btnClient.setBackground(Color.WHITE);
        btnHost.setBackground(Color.WHITE);

        ButtonGroup bg = new ButtonGroup();
        bg.add(btnClient);
        bg.add(btnHost);

        if (NetworkManager.isClientMode) btnClient.setSelected(true);
        else btnHost.setSelected(true);

        btnClient.addActionListener(e -> NetworkManager.setClientMode(true));
        btnHost.addActionListener(e -> NetworkManager.setClientMode(false));

        p.add(btnClient);
        p.add(Box.createVerticalStrut(5));
        p.add(btnHost);
        p.add(Box.createVerticalStrut(20));

        JButton btnReload = UIHelper.createButton("Reload System", AppConstants.TEAL, Color.WHITE);
        btnReload.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReload.addActionListener(e -> {
            dispose();
            NetworkManager.restartNetwork();
            parent.refreshUsers();
        });

        p.add(btnReload);

        add(p);
    }
}
