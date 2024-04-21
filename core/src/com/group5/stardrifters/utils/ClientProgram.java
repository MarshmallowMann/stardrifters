package com.group5.stardrifters.utils;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.screens.GameScreen;

import java.io.IOException;
import java.util.ArrayList;


public class ClientProgram extends Listener {

    //Our client object.
    static Client client;
    //IP to connect to.
    static String ip = "localhost";
    //Ports to connect on.
    static int tcpPort = 27960, udpPort = 27960;
    //A boolean value.
    static boolean messageReceived = false;

    // chatMessage
    static ArrayList<String> chatHistory = new ArrayList<String>();

    public static ArrayList<String> getChatHistory() {
        return chatHistory;
    }

    // Connect Client
    public void connect() throws IOException {
        System.out.println("Connecting to the server...");
        //Create the client
        client = new Client();
        //Register the packet class
        client.getKryo().register(PacketMessage.class);
        client.getKryo().register(NameMessage.class);
        //Connect the client
        client.start();
        client.connect(5000, ip, tcpPort, udpPort);
        //Add the listener
        client.addListener(new ClientProgram());
        System.out.println("Connected! The client program is now waiting for a packet...\n");

    }

    //I'm only going to implement this method from Listener.class because I only need to use this one.
    public void received(Connection c, Object p){
        //Is the received packet the same class as PacketMessage.class?
        if(p instanceof PacketMessage){
            //Cast it, so we can access the message within.
            PacketMessage packet = (PacketMessage) p;
            System.out.println("received a message from the host: "+packet.message);
            chatHistory.add(packet.name+ ": " + packet.message);

            //Limit the chat history to 5 messages.
            if (chatHistory.size() > 5) {
                chatHistory.remove(0);
            }

            System.out.println(chatHistory);

            //We have now received the message!
            messageReceived = true;
        }

        if (p instanceof NameMessage) {
            NameMessage nameMessage = (NameMessage) p;
            Application.playerName = nameMessage.name;
            System.out.println("Received name: " + nameMessage.name);
            System.out.println("Player name: " + Application.playerName);
        }
    }


//    Code for sending a message to the server
    public void sendMessage(String message, String name) {
        PacketMessage packetMessage = new PacketMessage();
        packetMessage.message = message;
        packetMessage.name = Application.playerName;
        client.sendUDP(packetMessage);
    }
}
