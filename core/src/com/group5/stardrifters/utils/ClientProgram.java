package com.group5.stardrifters.utils;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;


public class ClientProgram extends Listener {

    //Our client object.
    static Client client;
    //IP to connect to.
    static String ip = "localhost";
    //Ports to connect on.
    static int tcpPort = 27960, udpPort = 27960;

    //A boolean value.
    static boolean messageReceived = false;

    // Connect Client
    public void connect() throws IOException {
        System.out.println("Connecting to the server...");
        //Create the client
        client = new Client();
        //Register the packet class
        client.getKryo().register(PacketMessage.class);
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

            //We have now received the message!
            messageReceived = true;
        }
    }

//    Code for sending a message to the server
    public void sendMessage(String message){
        PacketMessage packetMessage = new PacketMessage();
        packetMessage.message = message;
        client.sendUDP(packetMessage);
    }
}
