public class Test {
    public static void main(String args[]){
        Node node1 = new Node(new Ip("nodo1"), 8, true);
        Node node3 = new Node(new Ip("nodo3"), 8, true);
        Node node5 = new Node(new Ip("nodo2"), 8, true);

        node1.setId(1);
        node3.setId(3);
        node5.setId(5);

        node1.setHandler();
        node3.setHandler();
        node5.setHandler();

        System.out.println("Id nodo1: " + node1.getId());
        System.out.println("Id nodo2: " + node3.getId());
        System.out.println("Id nodo3: " + node5.getId());

        node1.create();



        /*Item item1 = new Item("nodo1", 8);
        System.out.println("Id item1: " + item1.getKey());
        node3.addItem(item1);

        Node whoKeepsItem = node3.lookUp(item1.getKey());
        System.out.println(whoKeepsItem);*/
    }
}
