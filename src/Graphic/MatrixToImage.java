package Graphic;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;

public class MatrixToImage {
    public static void fromFileToImage(String inputFilename, String outputFilename, int widthImage, int heightImage) throws IOException {
        // Generate an image from a file:
        final File f = new File(inputFilename);
        // can specify width alone, or width + height
        // constructing does not render; not until getImage() is called
        final Java2DRenderer renderer = new Java2DRenderer(f, widthImage, heightImage);

        // this renders and returns the image, which is stored in the J2R; will not
        // be re-rendered, calls to getImage() return the same instance
        final BufferedImage img = renderer.getImage();

        // write it out, full size, PNG
        // FSImageWriter instance can be reused for different images,
        // defaults to PNG
        final FSImageWriter imageWriter = new FSImageWriter();
        // we can use the same writer, but at a different compression
        imageWriter.setWriteCompressionQuality(1f);
        imageWriter.write(img, outputFilename);
    }

    public static void createFile(String fileName, String tableName, Object rowData[][]) throws IOException{
        String header =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<style type=\"text/css\" media=\"screen\">\n" +
                "        html, body {\n" +
                "            padding: 0px;\n" +
                "            margin: 0px;\n" +
                "        }\n" +
                "        body {\n" +
                "            background-color: #F5F5F5;\n" +
                "            font-size: 30px;\n" +
                "            color: #564b47;\n" +
                "            margin: 0px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            font-size: 30px;\n" +
                "            text-align: left;\n" +
                "            color: #564b47;\n" +
                "            background-color: #F0F8FF;\n" +
                "            padding: 5px 15px;\n" +
                "            margin: 0px;\n" +
                "            border: 1px solid #000;\n" +
                "        }\n" +
                "        table {\n" +
                "          background-color: #FFF;\n" +
                "        }\n" +
                "        tr {\n" +
                "          background-color: #FFF;\n" +
                        "  border: 1px solid #555;\n" +
                "        }\n" +
                "        td {\n" +
                "          background-color: #FFF;\n" +
                "          text-align: center;\n" +
                "        }\n" +
                "        th {\n" +
                "          background-color: #F9F9F9;\n" +
                "          border: 1px solid #555;\n" +
                "          font-weight: plain;\n" +
                "          text-align: center;\n" +
                "        }\n" +
                "    </style>" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Fingertable of " + tableName + "</h1>\n" +
                "   \n" +
                "   <table style=\"width: 100%\">\n" +
                "      <tr> \n" +
                "        <th>Index</th>        \n" +
                "        <th>Node</th>\n" +
                "      </tr>\n";

        String tableData = "";
        for(int i=0; i<rowData.length; i++){
            tableData += "<tr><td>" + rowData[i][0] +"</td><td>" + rowData[i][1] + "</td></tr>";
        }
        String footer = "</table></body></html>";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(header + tableData + footer);
        writer.close();
    }

    public static void print(String tableName, Object rowData[][]) throws IOException{
        createFile("./src/Graphic/output.xhtml", tableName, rowData);
        fromFileToImage("./src/Graphic/output.xhtml", "./src/Graphic/" + tableName + ".png", 512, 512);
        File file = new File("./src/Graphic/output.xhtml");
        file.delete();
    }
}