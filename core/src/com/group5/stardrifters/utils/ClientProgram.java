package com.group5.stardrifters.utils;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class ClientProgram extends Listener {

    //Our client object.
    static Client client;
    //IP to connect to.
    static String ip = "localhost";
    //Ports to connect on.
    static int tcpPort = 27960, udpPort = 27960;

    //A boolean value.
    static boolean messageReceived = false;

    public static void main(String[] args) throws Exception {
        System.out.println("Connecting to the server...");
        //Create the client.
        client = new Client();

        //Register the packet object.
        client.getKryo().register(PacketMessage.class);

        //Start the client
        client.start();
        //The client MUST be started before connecting can take place.

        //Connect to the server - wait 5000ms before failing.
        client.connect(5000, ip, tcpPort, udpPort);

        //Add a listener
        client.addListener(new ClientProgram());

        System.out.println("Connected! The client program is now waiting for a packet...\n");

        //This is here to stop the program from closing before we receive a message.
        while(!messageReceived){
            Thread.sleep(1000);
        }

        System.out.println("Client will now exit.");
        System.exit(0);
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
}
