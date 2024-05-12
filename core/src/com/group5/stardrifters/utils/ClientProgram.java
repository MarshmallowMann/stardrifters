package com.group5.stardrifters.utils;

import com.group5.stardrifters.Application;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ClientProgram {
    private static final int PORT = 8888;
    private static final String SERVER_ADDRESS = "localhost";
    public static ArrayList<String> chatHistory =  new ArrayList<>();
    public static int playerCount = 1;
    private static DatagramSocket socket;
    private static InetAddress serverAddress;


    public void connect() throws IOException {
        socket = new DatagramSocket();
        serverAddress = InetAddress.getByName(SERVER_ADDRESS);

        Thread receiveThread = new Thread(() -> {
            try {
				System.out.println("Client started on port " + PORT);


                sendMessageToServer("Connected to the Server!");
                receiveMessages();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = "hello";
        while ((message = reader.readLine()) != null) {
            sendMessageToServer(message);
        }
    }


    public void receiveMessages() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            socket.receive(packet);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Message message = (Message) ois.readObject();
            if(message instanceof PacketMessage) {
                PacketMessage packetMessage = (PacketMessage) message;
                System.out.println(packetMessage.getName() + ": " + packetMessage.getText());

                chatHistory.add(packetMessage.getName() + ": " + packetMessage.getText());
                            if (chatHistory.size() > 5) {
                chatHistory.remove(0);
            }
            } else if (message instanceof NameMessage) {
                NameMessage nameMessage = (NameMessage) message;
                Application.playerName = nameMessage.getName();
                System.out.println("Received name: " + nameMessage.getName());
                System.out.println("Player name: " + Application.playerName);
            } else if (message instanceof GameStateMessage) {
                GameStateMessage gameStateMessage = (GameStateMessage) message;
                playerCount = gameStateMessage.getPlayerCount();
                System.out.println("Player count: " + playerCount);
            }

        }
    }

    public static void sendMessageToServer(String message) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        if (Application.playerName == null) {
            Application.playerName = "SERVER";
        }
        oos.writeObject(new PacketMessage(message, Application.playerName));
        byte[] buffer = baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
        socket.send(packet);
    }


}


//package com.group5.stardrifters.utils;
//
//import com.badlogic.gdx.Gdx;
//import com.esotericsoftware.kryonet.Client;
//import com.esotericsoftware.kryonet.Connection;
//import com.esotericsoftware.kryonet.Listener;
//import com.group5.stardrifters.Application;
//import com.group5.stardrifters.screens.GameScreen;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//public class ClientProgram extends Listener {
//
//    //Our client object.
//    static com.esotericsoftware.kryonet.Client client;
//    //IP to connect to.
//    static String ip = "localhost";
//    //Ports to connect on.
//    static int tcpPort = 27960, udpPort = 27960;
//    //A boolean value.
//    static boolean messageReceived = false;
//
//    // chatMessage
//    static ArrayList<String> chatHistory = new ArrayList<String>();
//
//    public static ArrayList<String> getChatHistory() {
//        return chatHistory;
//    }
//
//    // Connect Client
//    public void connect() throws IOException {
//        System.out.println("Connecting to the server...");
//        //Create the client
//        client = new com.esotericsoftware.kryonet.Client();
//        //Register the packet class
//        client.getKryo().register(PacketMessage.class);
//        client.getKryo().register(NameMessage.class);
//        //Connect the client
//        client.start();
//        client.connect(5000, ip, tcpPort, udpPort);
//        //Add the listener
//        client.addListener(new ClientProgram());
//        System.out.println("Connected! The client program is now waiting for a packet...\n");
//
//    }
//
//    //I'm only going to implement this method from Listener.class because I only need to use this one.
//    public void received(Connection c, Object p){
//        //Is the received packet the same class as PacketMessage.class?
//        if(p instanceof PacketMessage){
//            //Cast it, so we can access the message within.
//            PacketMessage packet = (PacketMessage) p;
//            System.out.println("received a message from the host: "+packet.message);
//            chatHistory.add(packet.name+ ": " + packet.message);
//
//            //Limit the chat history to 5 messages.
//            if (chatHistory.size() > 5) {
//                chatHistory.remove(0);
//            }
//
//            System.out.println(chatHistory);
//
//            //We have now received the message!
//            messageReceived = true;
//        }
//
//        if (p instanceof NameMessage) {
//            NameMessage nameMessage = (NameMessage) p;
//            Application.playerName = nameMessage.name;
//            System.out.println("Received name: " + nameMessage.name);
//            System.out.println("Player name: " + Application.playerName);
//        }
//    }
//
//
////    Code for sending a message to the server
//    public void sendMessage(String message, String name) {
//        PacketMessage packetMessage = new PacketMessage();
//        packetMessage.message = message;
//        packetMessage.name = Application.playerName;
//        client.sendUDP(packetMessage);
//    }
//}
