package chord;

public class Test {
    private Node whoKeepsItem;

    public static void main(String args[]){
        Node node0 = new Node(4, false);
        Node node2 = new Node(4, false);
        Node node6 = new Node(4, false);
        Node node8 = new Node(4, false);
        Node node13 = new Node(4, false);
        Node node14 = new Node(4, false);
        node0.setId(0);
        node2.setId(2);
        node6.setId(6);
        node8.setId(8);
        node13.setId(13);
        node14.setId(14);
        node0.setHandler();
        node2.setHandler();
        node6.setHandler();
        node8.setHandler();
        node13.setHandler();
        node14.setHandler();


        node0.create();
        try {
            Thread.sleep(2900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        node2.join(node0);
        try {
            Thread.sleep(2800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        node6.join(node2);
        try {
            Thread.sleep(2800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        node14.join(node2);
        try {
            Thread.sleep(2800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        node8.join(node14);
        try {
            Thread.sleep(2800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        node13.join(node0);

        /*Item item1 = new Item("nodo1", 8);
        item1.setKey(5);
        System.out.println("Id item1: " + item1.getKey());
        node6.addItem(item1);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Node whoKeepsItem = node2.lookUp(item1.getKey());
        System.out.println("Node found: " + whoKeepsItem + " keeps item5");*/

    }
}
