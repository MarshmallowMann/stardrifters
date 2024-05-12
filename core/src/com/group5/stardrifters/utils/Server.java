package com.group5.stardrifters.utils;

import com.group5.stardrifters.Application;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 9000;
    private static DatagramSocket socket;
    private static List<SocketAddress> clients = new ArrayList<>();
    static ArrayList<String> playerNames = new ArrayList<>();
    public static void main(String[] args) throws IOException, ClassNotFoundException {
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
    } else if (message instanceof SyncGamePacket) {
        SyncGamePacket syncGamePacket = (SyncGamePacket) message;
        broadcastToAllClients(clientSocketAddress, syncGamePacket);
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
        } else if (message instanceof GameStateMessage) {
            GameStateMessage gameStateMessage = (GameStateMessage) message;
            System.out.println("Broadcasting game state player count: " + gameStateMessage.getPlayerCount());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameStateMessage);
            buffer = baos.toByteArray();
        } else if (message instanceof SyncGamePacket) {
            SyncGamePacket syncGamePacket = (SyncGamePacket) message;
            System.out.println("Broadcasting sync game packet");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(syncGamePacket);
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
}