public class Test {
    public static void main(String args[]){
        Node node1 = new Node(new Ip("nodo2"), 8, true);
        Node node2 = new Node(new Ip("ns7"), 8, true);
        Node node3 = new Node(new Ip("nodo1"), 8, true);

        System.out.println("Id nodo1: " + node1.getId());
        System.out.println("Id nodo2: " + node2.getId());
        System.out.println("Id nodo3: " + node3.getId());

        node1.setSuccessor(node2.getId(), node2);
        node2.setSuccessor(node3.getId(), node3);
        node3.setSuccessor(node1.getId(), node1);

        Item item1 = new Item("nodo1", 8);
        System.out.println("Id item1: " + item1.getKey());
        node3.addItem(item1);

        Node whoKeepsItem = node3.lookUp(item1.getKey());
        System.out.println(whoKeepsItem);
    }
}
