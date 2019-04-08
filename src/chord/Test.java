package chord;

public class Test {
    private Node whoKeepsItem;

    public static void main(String args[]){
        Node node0 = new Node(3, true);
        Node node2 = new Node(3, true);
        Node node6 = new Node(3, true);
        Node node1 = new Node(3, true);
        Node node3 = new Node(3, true);

        Item item1 = new Item("1", 8);
        item1.setKey(1);
        System.out.println("Id item1: " + item1.getKey());

        Item item2 = new Item("2", 8);
        item2.setKey(2);
        System.out.println("Id item2: " + item2.getKey());

        Item item3 = new Item("3", 8);
        item3.setKey(3);
        System.out.println("Id item3: " + item3.getKey());

        Item item4 = new Item("4", 8);
        item4.setKey(4);
        System.out.println("Id item4: " + item4.getKey());

        Item item5 = new Item("5", 8);
        item5.setKey(5);
        System.out.println("Id item5: " + item5.getKey());

        Item item6 = new Item("6", 8);
        item6.setKey(6);
        System.out.println("Id item6: " + item6.getKey());

        Item item7 = new Item("7", 8);
        item7.setKey(7);
        System.out.println("Id item7: " + item7.getKey());

        node0.setId(0);
        node2.setId(2);
        node6.setId(6);
        node1.setId(1);
        node3.setId(3);
        node0.setHandler();
        node2.setHandler();
        node6.setHandler();
        node1.setHandler();
        node3.setHandler();

        node0.create();
        try {
            Thread.sleep(5000);
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
        node1.join(node2);
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        node3.join(node6);
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        node2.storeItem(item1);
        node2.storeItem(item2);
        node0.storeItem(item3);
        node0.storeItem(item4);
        node2.storeItem(item5);
        node6.storeItem(item6);
        node6.storeItem(item7);



        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*Node whoKeepsItem = node2.lookUp(item1.getKey());
        System.out.println("Node found: " + whoKeepsItem + " keeps item5");*/
        System.out.println("CRASHHHHH");
        node3.leave();

    }


}
