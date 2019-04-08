package Graphic;

import chord.FingerTable;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PrintChordRing {
    public static void run(ArrayList<FingerTable> fingerTables) throws IOException {
        showChordRing();
        printFingerTables(fingerTables);
        ArrayList<Integer> ids = new ArrayList<>();
        for (FingerTable f: fingerTables) {
            ids.add(f.getOwner().getId());
        }
        mergeTables(ids);
    }

    private static void showChordRing(){
        Graph graph = new SingleGraph("Tutorial 1");
        graph.addAttribute("ui.quality");
        ArrayList<Node> nodes = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            nodes.add(graph.addNode(String.valueOf(i)));
            nodes.get(i).addAttribute("ui.label", nodes.get(i).getId());
            nodes.get(i).addAttribute("ui.style", " size: 10;");
            if( i > 0 ){
                graph.addEdge(i-1 + "" + i, String.valueOf(i-1), String.valueOf(i), true);
            }
        }
        graph.addEdge(7 + "" + 0, 7, 0);
        graph.display();
        //graph.addAttribute("ui.screenshot", "./src/Graphic/ringScreenshot.png");
    }

    private static void printFingerTables(ArrayList<FingerTable> fingerTables) throws IOException{
        for (FingerTable table: fingerTables) {
            int numBitIdentifiers = 3;
            Object rowData[][] = new Object[numBitIdentifiers][2];
            for (int i = 0; i < numBitIdentifiers; i++) {
                rowData[i][0] = table.getPosition(i);
                rowData[i][1] = table.getNode((int) rowData[i][0]);
            }

            MatrixToImage.print(String.valueOf(table.getOwner().getId()), rowData);
        }
    }

    private static void mergeTables(ArrayList<Integer> idNodes) throws IOException{
        //BufferedImage img1 = ImageIO.read(new File("./src/Graphic/ringScreenshot.png"));
        BufferedImage img1 = ImageIO.read(new File("./src/Graphic/" + idNodes.get(0) + ".png"));
        for(int i = 1; i < idNodes.size(); i++){
            BufferedImage img2 = ImageIO.read(new File("./src/Graphic/" + idNodes.get(i) + ".png"));
            img1 = MergeMultipleImages.joinBufferedImage(img1, img2);
        }
        ImageIO.write(img1, "png", new File("./src/Graphic/FinalImage.png"));
    }
}
