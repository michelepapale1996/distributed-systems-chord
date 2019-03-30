public class Test {
    public static void main(String args[]){
        Node node1 = new Node(8, true);
        Node node3 = new Node(8, true);
        Node node5 = new Node(8, true);
        node1.setId(1);
        node3.setId(3);
        node5.setId(5);
        node1.setHandler();
        node3.setHandler();
        node5.setHandler();


        node1.create();
        node3.join(node1);
        node5.join(node3);

        Item item1 = new Item("nodo1", 8);
        item1.setKey(2);
        System.out.println("Id item1: " + item1.getKey());
        node3.addItem(item1);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Node whoKeepsItem = node5.lookUp(item1.getKey());
        System.out.println("Node found: " + whoKeepsItem + " keeps item1");

    }
}
