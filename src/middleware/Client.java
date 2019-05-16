package middleware;

import Utilities.CheckInput;
import Utilities.Debugger;
import chord.InfoNode;
import chord.Item;
import chord.Node;
import chord.NodeInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.NoSuchElementException;

public class Client {

    public static void main(String args[]) {
        Debugger.setDebug(false);
        Client client = new Client();
        client.run();
    }

    void run(){
        boolean flag = true;
        Node myNode = null;
        while (flag) {
            System.out.println("Welcome to Chord protocol.\nWhat do you want to do?");
            System.out.println("1 - Create a new Chord ring");
            System.out.println("2 - Connect to a Chord ring");
            System.out.println("0 - Exit from the application");
            int command = CheckInput.checkRange(0, 2);

            try {
                switch (command) {
                    case 0:
                        System.exit(0);
                    case 1:
                        myNode = this.createNewRing();
                        flag = false;
                        break;
                    case 2:
                        myNode = this.connectToRing();
                        flag = false;
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        try{
            this.mainMenu(myNode);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    private Node createNewRing() {
        System.out.println("What is the max num of bits of the identifiers of the ring?");
        int max_size = CheckInput.checkRange(1, 15);
        System.out.println("Do you want a simple look up algorithm? y/n");
        Boolean simpleLookUpAlgorithm = CheckInput.getBoolean();

        System.out.println("What is the id of your node?");
        int nodeId = CheckInput.checkRange(0, (long) Math.pow(2, max_size) - 1);

        Node myNode = null;
        try {
            myNode = new Node();
            myNode.setId(nodeId);
            myNode.create(max_size, simpleLookUpAlgorithm);
            System.setProperty("java.rmi.server.hostname", myNode.getAddress().getHostAddress());
            System.out.println("IpAddress of current node: " + myNode.getAddress().getHostAddress());

            //create the registry at port "nodeId"
            Registry registry = LocateRegistry.createRegistry((nodeId % 60000) + 2000);
            //bind the node on the registry
            registry.bind(String.valueOf(myNode.getId()), myNode);
        } catch (ExportException e) {
            throw new IllegalArgumentException("There already exists a ring with these parameters.");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        return myNode;
    }

    private Node connectToRing(){
        System.out.println("Insert the IP address of a node contained in the ring: ");
        String IpAddressKnownNode = CheckInput.validateIP();

        //String IpAddressKnownNode = "127.0.0.1";
        System.out.println("Insert the id of the known node contained in the ring: ");
        int knownNodeId = CheckInput.getInt();

        //registry is at port "knownNodeId"
        int port = (knownNodeId % 60000) + 2000;
        Node myNode = null;
        try {
            Registry registry = LocateRegistry.getRegistry(IpAddressKnownNode, port);
            NodeInterface knownNode = (NodeInterface) registry.lookup(String.valueOf(knownNodeId));

            System.out.println("Insert the id of your node: ");
            int nodeId = CheckInput.checkRange(0, (int) Math.pow(2, knownNode.getRing().getNum_bits_identifiers()) - 1);

            myNode = new Node();
            myNode.setId(nodeId);
            myNode.join(knownNode);
            System.out.println("Connected to ring containing node " + IpAddressKnownNode + " and nodeId " + knownNodeId);

            System.setProperty("java.rmi.server.hostname", myNode.getAddress().getHostAddress());
            System.out.println("IpAddress of current node: " + myNode.getAddress().getHostAddress());

            //create the registry at port "nodeId"
            Registry registry1 = LocateRegistry.createRegistry((nodeId % 60000) + 2000);
            //bind the node on the registry
            registry1.bind(String.valueOf(myNode.getId()), myNode);

        }catch (AlreadyBoundException|NotBoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }catch (RemoteException e){
            throw new IllegalArgumentException("Error - possible causes:\n-there no exists a node at the ip you typed\n-there no exists a node with the id you typed\n-you cannot use the id you typed as identifier of your node.");
        }
        return myNode;
    }

    private void mainMenu(Node myNode) throws RemoteException {
        boolean flag = true;
        do {
            System.out.println("What do you want to do?");
            System.out.println("1 - lookup item");
            System.out.println("2 - store an item");
            System.out.println("3 - Show node's info");
            System.out.println("4 - exit");
            int choice = CheckInput.checkRange(1, 4);

            switch (choice) {
                case 1:
                    this.lookupItem(myNode);
                    break;
                case 2:
                    this.storeItem(myNode);
                    break;
                case 3:
                    InfoNode.show(myNode);
                    break;
                default:
                    flag = false;
                    myNode.exitFromRing();
                    System.out.println("Exiting from the application...");
                    System.exit(0);
                    break;
            }
        } while (flag);
    }

    private void lookupItem(Node myNode) throws RemoteException {
        System.out.println("Insert the id of the item you want to find: ");
        int itemId = CheckInput.getInt();
        try {
            NodeInterface owner = myNode.lookUp(itemId);
            System.out.println(owner.print() + " has the item with id: " + itemId);
        } catch (NoSuchElementException e) {
            System.out.println("Given item does not exist.");
        }
    }

    private void storeItem(Node myNode) throws RemoteException {
        System.out.println("What is the id of the item?");
        int itemId =  CheckInput.checkRange(0, (int) Math.pow(2, myNode.getRing().getNum_bits_identifiers()) - 1);
        Item item = new Item("prova", myNode.getRing().getNum_bits_identifiers());
        item.setKey(itemId);
        try {
            myNode.storeItem(item);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

    }
}