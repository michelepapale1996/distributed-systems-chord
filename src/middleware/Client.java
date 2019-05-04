package middleware;

import Test.Debugger;
import chord.InfoNode;
import chord.Item;
import chord.Node;
import chord.NodeInterface;

import java.rmi.AlreadyBoundException;
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

    void run() throws RemoteException, NullPointerException{
        boolean flag = true;
        while(flag){
            System.out.println("Welcome to Chord protocol.\nWhat do you want to do?");
            System.out.println("1 - Create a new Chord ring");
            System.out.println("2 - Connect to a Chord ring");
            System.out.println("0 - Exit from the application");
            int command = checkRange(0,2);

            Node myNode;
            switch (command) {
                case 0:
                    System.exit(0);
                case 1:
                    myNode = this.createNewRing();
                    this.mainMenu(myNode);
                    break;
                case 2:
                    myNode = this.connectToRing();
                    this.mainMenu(myNode);
                    break;
            }
        }
    }

    private Node createNewRing(){
        System.out.println("What is the max num of bits of the identifiers of the ring?");
        int max_size = getInt();
        System.out.println("Do you want a simple lookup algorithm? y/n");
        Boolean simpleLookUpAlgorithm = getBoolean();

        System.out.println("What is the id of your node?");
        int nodeId = checkRange(0, (int) Math.pow(2, max_size) - 1);

        Node myNode = null;
        try {
            myNode = new Node();
            myNode.setId(nodeId);
            myNode.create(max_size,simpleLookUpAlgorithm);
            System.setProperty("java.rmi.server.hostname", myNode.getAddress().getHostAddress());
            System.out.println("IpAddress of current node: " + myNode.getAddress().getHostAddress());

            //create the registry at port "nodeId"
            Registry registry = LocateRegistry.createRegistry(nodeId + 2000);
            //bind the node on the registry
            registry.bind(String.valueOf(myNode.getId()), myNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myNode;
    }

    private Node connectToRing(){
        System.out.println("Insert the IP address of a node contained in the ring: ");
        String IpAddressKnownNode = scanner.nextLine();
        //String IpAddressKnownNode = "127.0.0.1";

        System.out.println("Insert the id of the known node contained in the ring: ");
        int knownNodeId = getInt();

        System.out.println("Insert the id of your node: ");
        int nodeId = getInt();

        Node myNode = null;
        try {
            //registry is at port "knownNodeId"
            int port =  knownNodeId + 2000;
            Registry registry = LocateRegistry.getRegistry(IpAddressKnownNode, port);
            myNode = new Node();
            myNode.setId(nodeId);

            //NodeInterface knownNode = (NodeInterface) registry.lookup("rmi://" + IpAddressKnownNode + ":" + port + "/" + String.valueOf(knownNodeId));
            NodeInterface knownNode = (NodeInterface) registry.lookup(String.valueOf(knownNodeId));
            myNode.join(knownNode);
            System.out.println("Connected to ring containing node " + IpAddressKnownNode + " and nodeId " + knownNodeId);

            System.setProperty("java.rmi.server.hostname", myNode.getAddress().getHostAddress());
            System.out.println("IpAddress of current node: " + myNode.getAddress().getHostAddress());

            //create the registry at port "nodeId"
            Registry registry1 = LocateRegistry.createRegistry(nodeId + 2000);
            //bind the node on the registry
            registry1.bind(String.valueOf(myNode.getId()), myNode);
            return myNode;

        } catch (NotBoundException e) {
            System.out.println("There no exist a Chord ring containing the node you typed.");
            throw new NullPointerException();
        } catch (RemoteException e) {
            System.out.println(e);
        } catch (IllegalArgumentException e){
            //Node cannot join the ring because there is already a node with his id.
            System.out.println(e);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        return myNode;
    }

    private void mainMenu(Node myNode) throws RemoteException{
        boolean flag = true;
        do{
            System.out.println("What do you want to do?");
            System.out.println("1 - lookup item");
            System.out.println("2 - store an item");
            System.out.println("3 - Show node's info");
            System.out.println("4 - exit");
            int choice = checkRange(1,4);

            switch (choice) {
                case 1:
                    this.lookupItem(myNode);
                    break;
                case 2 :
                    this.storeItem(myNode);
                    break;
                case 3:
                    new InfoNode(myNode);
                    break;
                default:
                    flag = false;
                    this.exit(myNode);
                    System.out.println("Exiting from the application...");
                    System.exit(0);
                    break;
            }
        }while(flag);
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

    private int checkRange(int lower, int upper){
        boolean isInt = false;
        int inputToInt = -1;
        while(!isInt){
            String input = scanner.nextLine();
            try{
                inputToInt = Integer.parseInt(input);
                if(inputToInt >= lower && inputToInt <= upper){
                    isInt = true;
                }else{
                    System.out.println("The input must be between " + lower + " and " + upper + ". Try again:");
                }
            }catch (NumberFormatException e){
                System.out.println("Given input is not an integer. Try again:");
            }
        }
        return inputToInt;
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

    private Boolean getBoolean(){
        Boolean isBoolean = false;
        Boolean simple = true;
        while (!isBoolean){
            String input = scanner.nextLine();
            if (input.equals("y")){
                isBoolean = true;
            }else {
                if (input.equals("n")){
                    simple = false;
                    isBoolean =true;
                }
                else {
                    System.out.println("Given input is neither y or n. Try again: ");
                }
            }
        }
        return simple;
    }

    private void exit(Node mynode) throws RemoteException {
        mynode.exitFromRing();
    }
}
