package com.group5.stardrifters.utils;

import java.util.ArrayList;
import java.util.Date;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


public class ServerProgram extends Listener {

    //Server object
    static Server server;
    //Ports to listen on
    static int udpPort = 27960, tcpPort = 27960;

    // Create an Arraylist of player names player1 to player8 (initialize to PlayerX)
    static ArrayList<String> playerNames = new ArrayList<String>();


    public static void main(String[] args) throws Exception {
        System.out.println("Creating the server...");
            for (int i = 1; i <= 8; i++) {
        playerNames.add("Player" + i);
    }
        //Create the server
        server = new Server();

        //Register a packet class.
        server.getKryo().register(PacketMessage.class);
        server.getKryo().register(NameMessage.class);
        //We can only send objects as packets if they are registered.

        //Bind to a port
        server.bind(tcpPort, udpPort);

        //Start the server
        server.start();

        //Add the listener
        server.addListener(new ServerProgram());

        System.out.println("Server is operational!");
    }

    //This is run when a connection is received!
    public void connected(Connection c){
        System.out.println("Received a connection from "+c.getRemoteAddressTCP().getHostString());
        //Create a message packet.
        PacketMessage packetMessage = new PacketMessage();
        //Assign the message text.

        packetMessage.name = "SERVER";

        // Player name
        String name =  playerNames.remove(0);

        packetMessage.message = "Hello "+ name +"! The time is: "+new Date().toString();

        System.out.println("Player name: " + packetMessage.name);

        //Send the message
//        c.sendTCP(packetMessage);
        //Alternatively, we could do:
        c.setName(name);

        NameMessage player = new NameMessage();
        player.name = name;

        c.sendUDP(player);
        c.sendUDP(packetMessage);


        //To send over UDP.
    }

    //This is run when we receive a packet.
    public void received(Connection c, Object p){
//        When we receive packets, we need to check if they are the same class as the packet we registered.
        if(p instanceof PacketMessage){
            //Cast it, so we can access the message within.
            PacketMessage packet = (PacketMessage) p;
//            Print the message to the console.
            System.out.println("Received a message from "+c.getRemoteAddressTCP().getHostString()+": "+packet.message);
            System.out.println("Received a message from "+c.getRemoteAddressTCP().getHostString()+": "+packet.name);

//            Broadcast the message to all clients.
            server.sendToAllUDP(packet);
        }
    }

    //This is run when a client has disconnected.
    public void disconnected(Connection c){
        System.out.println("A client disconnected!");
    }
}
