package middleware;

import Test.Debugger;
import chord.Item;
import chord.Node;
import chord.NodeInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);

    public static void main(String args[]) {
        Debugger.setDebug(false);
        Client client = new Client();
        try {
            client.run();
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    void run() throws RemoteException{
        System.out.println("Welcome to Chord protocol.\nWhat do you want to do?");
        System.out.println("1 - Create a new Chord ring");
        System.out.println("2 - Connect to a Chord ring");
        System.out.println("0 - Exit from the application");
        String command = scanner.nextLine();

        Node myNode;
        switch (command) {
            case "1":
                myNode = this.createNewRing();
                this.mainMenu(myNode);
                break;
            case "2" :
                myNode = this.connectToRing();
                this.mainMenu(myNode);
                break;
            default:
                break;
        }
    }

    private Node createNewRing(){
        System.out.println("What is the max num of bits of the identifiers of the ring?");
        int max_size = getInt();
        System.out.println("What is the id of the node?");
        int nodeId = getInt();

        Node myNode = null;
        try {
            myNode = new Node(max_size, true);
            myNode.setId(nodeId);
            myNode.create();

            //create the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            //bind the node on the registry
            registry.bind(String.valueOf(myNode.getId()), myNode);
            InetAddress IpAddress = InetAddress.getLocalHost();
            System.out.println("IpAddress of current node: " + IpAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myNode;
    }

    private Node connectToRing(){
        //System.out.println("Insert the IP address of a node contained in the ring: ");
        //String IpAddressKnownNode = scanner.nextLine();
        String IpAddressKnownNode = "127.0.0.1";

        System.out.println("Insert the id of the node contained in the ring: ");
        int knownNodeId = getInt();

        System.out.println("Insert the id of the node: ");
        int nodeId = getInt();

        Node myNode = null;
        try {
            Registry registry = LocateRegistry.getRegistry(IpAddressKnownNode, 1099);
            myNode = new Node(3, true);
            myNode.setId(nodeId);

            NodeInterface knownNode = (NodeInterface) registry.lookup(String.valueOf(knownNodeId));
            System.out.println("Connected to ring containing node " + IpAddressKnownNode + " and nodeId " + knownNodeId);

            myNode.join(knownNode);
            return myNode;
        } catch (NotBoundException e) {
            System.out.println("There no exist a Chord ring containing the node you typed.");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            //Node cannot join the ring because there is already a node with his id.
            System.out.println(e);
        }
        return myNode;
    }

    private void mainMenu(Node myNode) throws RemoteException{
        int flag = 0;
        do{
            System.out.println("What do you want to do?");
            System.out.println("1 - lookup item");
            System.out.println("2 - store an item");
            System.out.println("3 - Show node's info");
            System.out.println("4 - exit");
            int choice = getInt();

            switch (choice) {
                case 1:
                    this.lookupItem(myNode);
                    break;
                case 2 :
                    this.storeItem(myNode);
                    break;
                case 3:
                    this.infoCurrentNode(myNode);
                    break;
                default:
                    flag=1;
                    System.out.println("Exiting from the application...");
                    break;
            }
        }while(flag==0);
    }

    private void lookupItem(Node myNode) throws RemoteException {
        System.out.println("Insert the id of the item you want to find: ");
        int itemId = getInt();
        try{
            NodeInterface owner = myNode.lookUp(itemId);
            System.out.println(owner.print() + " has the item with id: " + itemId);
        }catch (NoSuchElementException e){
            System.out.println("Given item does not exist.");
        }
    }


    private void storeItem(Node myNode) throws RemoteException {
        System.out.println("What is the id of the item?");
        int itemId = getInt();
        Item item = new Item("prova", 8);
        item.setKey(itemId);
        try{
            myNode.storeItem(item);
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }

    }

    private void infoCurrentNode(Node myNode) throws RemoteException {
        System.out.println("Info current node:");
        System.out.println("- Node id: " + myNode.print());
        System.out.println("- Successor: " + myNode.getSuccessor().print());
        try{
            System.out.println("- Predecessor: " + myNode.getPredecessor().print());
        }catch (NullPointerException e){
            System.out.println("- Predecessor: null");
        }
        System.out.println("- Items of the node: " + myNode.getItems() );
    }

    private int getInt(){
        boolean isInt = false;
        int inputToInt = -1;
        while(!isInt){
            String input = scanner.nextLine();
            try{
                inputToInt = Integer.parseInt(input);
                isInt = true;
            }catch (NumberFormatException e){
                System.out.println("Given input is not an integer. Try again:");
            }
        }
        return inputToInt;
    }
}
