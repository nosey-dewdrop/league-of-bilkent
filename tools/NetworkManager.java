package tools;

import java.net.*;
import java.util.*;
import javax.swing.SwingUtilities;

public class NetworkManager {
    public static final int DISCOVERY_PORT = 50055;
    private static DatagramSocket discoverySocket;
    private static boolean isDiscovering = false;
    private static boolean isBroadcasting = false;
    public static final String myUuid = UUID.randomUUID().toString();
    
    public static class DiscoveredHost {
        public String ip;
        public long lastSeen;
        public DiscoveredHost(String ip) { 
            this.ip = ip; 
            this.lastSeen = System.currentTimeMillis();
        }
    }
    
    public static List<DiscoveredHost> discoveredHosts = new ArrayList<>();
    public static Runnable onHostFound;
    
    public static boolean isClientMode = true; 
    
    public static String myIp = "127.0.0.1";
    
    static {
        try {
            try (final DatagramSocket tempSocket = new DatagramSocket()) {
                tempSocket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                myIp = tempSocket.getLocalAddress().getHostAddress();
            }
        } catch (Exception e) {
            try { myIp = InetAddress.getLocalHost().getHostAddress(); } catch (Exception ignored) {}
        }
    }

    public static void setClientMode(boolean client) {
        isClientMode = client;
    }

    public static void restartNetwork() {
        stopDiscovery();
        isBroadcasting = false;
        try { Thread.sleep(200); } catch(Exception ignored){}
        
        discoveredHosts.clear();
        if (isClientMode) {
            model.Database.customDbUrl = null;
            startDiscovery();
        } else {
            model.Database.customDbUrl = null;
            startBroadcasting();
        }
        model.Database.createConnection();
    }
    
    public static void startBroadcasting() {
        if(isBroadcasting) return;
        if(isClientMode) return;
        isBroadcasting = true;
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                while(isBroadcasting) {
                    String message = "LOB_HOST:" + myIp + ":" + myUuid;
                    byte[] buffer = message.getBytes();
                    try { socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), DISCOVERY_PORT)); } catch(Exception ignored){}
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface networkInterface = interfaces.nextElement();
                        if (networkInterface.isLoopback() || !networkInterface.isUp()) continue;
                        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                            InetAddress broadcast = interfaceAddress.getBroadcast();
                            if (broadcast == null) continue;
                            try { socket.send(new DatagramPacket(buffer, buffer.length, broadcast, DISCOVERY_PORT)); } catch (Exception ignored) {}
                        }
                    }
                    Thread.sleep(2000);
                }
            } catch (Exception e) {}
            isBroadcasting = false;
        }).start();
    }
    
    public static void startDiscovery() {
        if(isDiscovering) return;
        if(!isClientMode) return;
        isDiscovering = true;
        discoveredHosts.clear();
        new Thread(() -> {
            try {
                discoverySocket = new DatagramSocket(DISCOVERY_PORT);
                byte[] buffer = new byte[1024];
                while(isDiscovering) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    discoverySocket.receive(packet);
                    String msg = new String(packet.getData(), 0, packet.getLength());
                    if(msg.startsWith("LOB_HOST:")) {
                        String[] parts = msg.split(":");
                        if(parts.length >= 3) {
                            String ip = parts[1].trim();
                            if(ip.equals("127.0.0.1") || ip.startsWith("0:0:0")) ip = "localhost";
                            String uuid = parts[2].trim();
                            
                            if(!uuid.equals(myUuid)) {
                                boolean exists = false;
                                for(DiscoveredHost h : discoveredHosts) {
                                    if(h.ip.equals(ip)) { exists = true; h.lastSeen = System.currentTimeMillis(); break; }
                                }
                                if(!exists) {
                                    discoveredHosts.add(new DiscoveredHost(ip));
                                    if(onHostFound != null) SwingUtilities.invokeLater(onHostFound);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {}
            isDiscovering = false;
        }).start();
        
        new Thread(() -> {
            while(isDiscovering) {
                try {
                    Thread.sleep(5000);
                    long now = System.currentTimeMillis();
                    boolean removed = discoveredHosts.removeIf(h -> (now - h.lastSeen) > 8000);
                    if(removed && onHostFound != null) SwingUtilities.invokeLater(onHostFound);
                } catch(Exception ignored){}
            }
        }).start();
    }
    
    public static void stopDiscovery() {
        isDiscovering = false;
        if(discoverySocket != null && !discoverySocket.isClosed()) discoverySocket.close();
    }
}
