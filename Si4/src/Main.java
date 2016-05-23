import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by konrad on 22.05.16.
 */
public class Main {

    public static void main(String[] args) {
        Parser p = new Parser();
        String path1 = "stacja1";
        String path2 = "stacja2";
        PictureAttribute[] picture1 = p.getValues("/home/konrad/Pulpit/zdjeciaSI/" + path1 + ".png.haraff.sift");
        PictureAttribute[] picture2 = p.getValues("/home/konrad/Pulpit/zdjeciaSI/" + path2 + ".png.haraff.sift");

        int kNearest = 100;


        for (int i = 0; i < picture1.length; i++) {
            KNN knn = new KNN(picture2, picture1[i]);
            picture1[i].setNearestNeighbour(knn.getNearestNeighbour());
        }
        for (int i = 0; i < picture2.length; i++) {
            KNN knn = new KNN(picture1, picture2[i]);
            picture2[i].setNearestNeighbour(knn.getNearestNeighbour());
        }

        Map<PictureAttribute, PictureAttribute> keyPoint = new HashMap<>();

        for (int i = 0; i < picture1.length; i++) {
            if (picture2[picture1[i].getNearestNeighbourLabel()].getNearestNeighbourLabel() == i) {
                keyPoint.put(picture1[i], picture1[i].getNearestNeighbour());
            }
        }

        for (int i = 0; i < picture1.length; i++) {
            Neighbourhood neighbourhood = new Neighbourhood(keyPoint.keySet().toArray(), picture1[i]);
            picture1[i].setNeighbourHood(neighbourhood.getNearestNeighbours(kNearest));
        }
        for (int i = 0; i < picture2.length; i++) {
            Neighbourhood neighbourhood = new Neighbourhood(keyPoint.values().toArray(), picture2[i]);
            picture2[i].setNeighbourHood(neighbourhood.getNearestNeighbours(kNearest));
        }

//        Set<PictureAttribute> keySet = keyPoint.keySet();
//
//        Iterator<PictureAttribute> it = keySet.iterator();
//
//        while(it.hasNext()){
//            PictureAttribute key = it.next();
//            System.out.println(key + "   " + keyPoint.get(key));
//        }

        Map<PictureAttribute, PictureAttribute> compactKeyPointsPairs = Algorithms.getCompactNeighbourhood(keyPoint, 80);

        Set<PictureAttribute> keySet = compactKeyPointsPairs.keySet();

        Iterator<PictureAttribute> it = keySet.iterator();

        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File("/home/konrad/Pulpit/zdjeciaSI/stacja1.png"));
            img2 = ImageIO.read(new File("/home/konrad/Pulpit/zdjeciaSI/stacja2.png"));
            while (it.hasNext()) {
                PictureAttribute pictureAttribute1 = it.next();
                PictureAttribute pictureAttribute2 = compactKeyPointsPairs.get(pictureAttribute1);
                img1.setRGB((int) pictureAttribute1.getCoordinateX(), (int) pictureAttribute1.getCoordinateY(), Color.RED.getRGB());
                img2.setRGB((int) pictureAttribute2.getCoordinateX(), (int) pictureAttribute2.getCoordinateY(), Color.RED.getRGB());
            }
            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(new JLabel(new ImageIcon(img1)));
            frame.pack();
            frame.setVisible(true);

            JFrame frame1 = new JFrame();
            frame1.getContentPane().setLayout(new FlowLayout());
            frame1.getContentPane().add(new JLabel(new ImageIcon(img2)));
            frame1.pack();
            frame1.setVisible(true);
        } catch (IOException e) {
        }
    }
}
