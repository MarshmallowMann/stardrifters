package com.group5.stardrifters.utils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private static final int PORT = 9000;
    private static DatagramSocket socket;
    private static List<SocketAddress> clients = new ArrayList<>();
    static ArrayList<String> playerNames = new ArrayList<>();
    private static long startTime;
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        // Send time every second
        socket = new DatagramSocket(PORT);
         for (int i = 1; i <= 8; i++) {
            playerNames.add("Player" + i);
         }
        System.out.println("Server started on port " + PORT);

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            socket.receive(packet);
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            SocketAddress clientSocketAddress = new InetSocketAddress(clientAddress, clientPort);

            if (!clients.contains(clientSocketAddress)) {
                clients.add(clientSocketAddress);
                connected(clientSocketAddress);
            } else {
                received(clientSocketAddress, packet.getData());
            }

        }
    }

    public static void startTimer () {
         Timer timer = new Timer();
                    System.out.println("Timer started");
                    startTime = System.currentTimeMillis();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                long elapsedTime = System.currentTimeMillis() - startTime;
                                broadcastTimeToAllClients(elapsedTime);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0, 1000);
    }

    private static void connected(SocketAddress clientSocketAddress) throws IOException {
        String name = playerNames.remove(0);
        String message = "You are connected.";
        NameMessage nameMessage = new NameMessage(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(nameMessage);
        byte[] buffer = baos.toByteArray();
        InetSocketAddress socketAddress = (InetSocketAddress) clientSocketAddress;
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, socketAddress.getAddress(), socketAddress.getPort());
        socket.send(packet);

        broadcastToClient(clientSocketAddress, new PacketMessage("Welcome to the server!", "SERVER"));
        broadcastToAllClients(clientSocketAddress, new PacketMessage(name + " has joined the server.", "SERVER"));
        broadcastToAllClients(clientSocketAddress, new GameStateMessage(clients.size()));
        System.out.println("Client connected: " + socketAddress.getAddress().getHostAddress() + ":" + socketAddress.getPort());


    }

    private static void received(SocketAddress clientSocketAddress, byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ObjectInputStream ois = new ObjectInputStream(bais);
    Message message = (Message) ois.readObject();

    InetSocketAddress socketAddress = (InetSocketAddress) clientSocketAddress;
    System.out.println("Received from " + socketAddress.getAddress().getHostAddress() + ":" + socketAddress.getPort() + ": " + message);

    if (message instanceof PacketMessage) {
        PacketMessage packetMessage = (PacketMessage) message;
        broadcastToAllClients(clientSocketAddress, packetMessage);
    } else if (message instanceof ControlMessage) {
        ControlMessage controlMessage = (ControlMessage) message;
        broadcastControlToAllClients(clientSocketAddress, controlMessage.getText(), controlMessage.getName());
    } else if (message instanceof GameStartMessage) {
        GameStartMessage gameStartMessage = (GameStartMessage) message;
        startTimer();
        broadcastToAllClients(clientSocketAddress, gameStartMessage);
    } else if (message instanceof ScoreMessage) {
        ScoreMessage scoreMessage = (ScoreMessage) message;
        broadcastToAllClients(clientSocketAddress, scoreMessage);
    } else if (message instanceof SyncGamePacket) {
        SyncGamePacket syncGamePacket = (SyncGamePacket) message;
        broadcastToAllClients(clientSocketAddress, syncGamePacket);
    } else if (message instanceof GameObject) {
        GameObject gameObject = (GameObject) message;
        broadcastToAllClients(clientSocketAddress, gameObject);
    }
        // handle other types of messages here
}

    private static void broadcastToAllClients(SocketAddress senderSocketAddress, Message message) throws IOException {
        byte[] buffer = null;
        if (message instanceof PacketMessage) {
            PacketMessage packetMessage = (PacketMessage) message;
            System.out.println("Broadcasting message: " + packetMessage.getText());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(packetMessage);
            buffer = baos.toByteArray();
        } else if (message instanceof GameStartMessage) {
            GameStartMessage gameStartMessage = (GameStartMessage) message;
            System.out.println("Broadcasting game start: " + gameStartMessage.getText());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameStartMessage);
            buffer = baos.toByteArray();
        } else if (message instanceof GameStateMessage) {
            GameStateMessage gameStateMessage = (GameStateMessage) message;
            System.out.println("Broadcasting game state player count: " + gameStateMessage.getPlayerCount());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameStateMessage);
            buffer = baos.toByteArray();
        } else if (message instanceof ScoreMessage) {
            ScoreMessage scoreMessage = (ScoreMessage) message;
            System.out.println("Broadcasting score(" + scoreMessage.getScore() + ") to client: " + scoreMessage.getName());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(scoreMessage);
            buffer = baos.toByteArray();
        } else if (message instanceof SyncGamePacket) {
            SyncGamePacket syncGamePacket = (SyncGamePacket) message;
            System.out.println("Broadcasting sync game packet");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(syncGamePacket);
            buffer = baos.toByteArray();
        } else if (message instanceof GameObject) {
            GameObject gameObject = (GameObject) message;
            System.out.println("Broadcasting game object");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameObject);
            buffer = baos.toByteArray();
        }

        for (SocketAddress clientSocketAddress : clients) {
//            if (clientSocketAddress.equals(senderSocketAddress)) {
            InetSocketAddress socketAddress = (InetSocketAddress) clientSocketAddress;
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, socketAddress.getAddress(), socketAddress.getPort());
                socket.send(packet);
//            }
        }
    }

    private static void broadcastControlToAllClients(SocketAddress senderSocketAddress, String message, String name) throws IOException {
        ControlMessage controlMessage = new ControlMessage(message, name);
        System.out.println("Broadcasting control message: " + controlMessage.getText());
        System.out.println("Broadcasting control name: " + controlMessage.getName());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(new ControlMessage(message, name));
        byte[] buffer = baos.toByteArray();
        for (SocketAddress clientSocketAddress : clients) {
//            if (clientSocketAddress.equals(senderSocketAddress)) {
                InetSocketAddress socketAddress = (InetSocketAddress) clientSocketAddress;
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, socketAddress.getAddress(), socketAddress.getPort());
                socket.send(packet);
        }
    }

    private static void broadcastToClient(SocketAddress clientSocketAddress, Message message) throws IOException {
        System.out.println("Broadcasting message to one client: " + message);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(message);
        byte[] buffer = baos.toByteArray();
        InetSocketAddress socketAddress = (InetSocketAddress) clientSocketAddress;
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, socketAddress.getAddress(), socketAddress.getPort());
        socket.send(packet);
    }

    private static void disconnect(SocketAddress clientSocketAddress) {
        clients.remove(clientSocketAddress);
        InetSocketAddress socketAddress = (InetSocketAddress) clientSocketAddress;
        System.out.println("Client disconnected: " + socketAddress.getAddress().getHostAddress() + ":" + socketAddress.getPort());
    }

    private static void broadcastTimeToAllClients(long time) throws IOException {
        TimeMessage timeMessage = new TimeMessage(time);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(timeMessage);
        byte[] buffer = baos.toByteArray();
        for (SocketAddress clientSocketAddress : clients) {
            InetSocketAddress socketAddress = (InetSocketAddress) clientSocketAddress;
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, socketAddress.getAddress(), socketAddress.getPort());
            socket.send(packet);
        }
    }
}