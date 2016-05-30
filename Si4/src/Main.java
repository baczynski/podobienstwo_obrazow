import Jama.Matrix;
import algorithms.Algorithms;
import model.Model;
import model.PictureAttribute;
import neighbourhoodAnalysis.KNN;
import neighbourhoodAnalysis.Neighbourhood;
import parser.Parser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by konrad on 22.05.16.
 */
public class Main {

    private static final String path1 = "castle1";
    private static final String path2 = "castle2";
    private static final int kNearest = 100;

    public static void main(String[] args) {
        Parser parser = new Parser();
        PictureAttribute[] picture1 = parser.getValues("/home/konrad/Pulpit/zdjeciaSI/" + path1 + ".png.haraff.sift");
        PictureAttribute[] picture2 = parser.getValues("/home/konrad/Pulpit/zdjeciaSI/" + path2 + ".png.haraff.sift");

        setNearestNeighbour(picture1, picture2);

        Map<PictureAttribute, PictureAttribute> keyPoint = getKeyPointsPairs(picture1, picture2);

        setPictureNeighbourhood(picture1, picture2, keyPoint);

        List<BufferedImage> neighbourAnalysisImagesList = getNeighbourhoodAnalysisImage(keyPoint);
        List<BufferedImage> ransacBufferedImagesList = getRansacImages(keyPoint);

        try {
            writeImage(path1 + "ransac", ransacBufferedImagesList.get(0));
            writeImage(path2 + "ransac", ransacBufferedImagesList.get(1));
            writeImage(path1 + "neighbourhoodAnalysis", neighbourAnalysisImagesList.get(0));
            writeImage(path2 + "neighbourhoodAnalysis", neighbourAnalysisImagesList.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void setNearestNeighbour(PictureAttribute[] picture1, PictureAttribute[] picture2) {
        for (int i = 0; i < picture1.length; i++) {
            KNN knn = new KNN(picture2, picture1[i]);
            picture1[i].setNearestNeighbour(knn.getNearestNeighbour());
        }
        for (int i = 0; i < picture2.length; i++) {
            KNN knn = new KNN(picture1, picture2[i]);
            picture2[i].setNearestNeighbour(knn.getNearestNeighbour());
        }
    }

    private static Map<PictureAttribute, PictureAttribute> getKeyPointsPairs(PictureAttribute[] picture1, PictureAttribute[] picture2) {
        Map<PictureAttribute, PictureAttribute> keyPoint = new HashMap<>();
        for (int i = 0; i < picture1.length; i++) {
            if (picture2[picture1[i].getNearestNeighbourLabel()].getNearestNeighbourLabel() == i) {
                keyPoint.put(picture1[i], picture1[i].getNearestNeighbour());
            }
        }
        return keyPoint;
    }

    private static void setPictureNeighbourhood(PictureAttribute[] picture1, PictureAttribute[] picture2, Map<PictureAttribute, PictureAttribute> keyPoint) {
        for (int i = 0; i < picture1.length; i++) {
            Neighbourhood neighbourhood = new neighbourhoodAnalysis.Neighbourhood(keyPoint.keySet().toArray(), picture1[i]);
            picture1[i].setNeighbourHood(neighbourhood.getNearestNeighbours(kNearest));
        }
        for (int i = 0; i < picture2.length; i++) {
            Neighbourhood neighbourhood = new neighbourhoodAnalysis.Neighbourhood(keyPoint.values().toArray(), picture2[i]);
            picture2[i].setNeighbourHood(neighbourhood.getNearestNeighbours(kNearest));
        }
    }

    private static List<BufferedImage> getNeighbourhoodAnalysisImage(Map<PictureAttribute, PictureAttribute> keyPoint) {
        List<BufferedImage> images = new ArrayList<>();
        Map<model.PictureAttribute, model.PictureAttribute> compactKeyPointsPairs = algorithms.Algorithms.getCompactNeighbourhood(keyPoint, 80);

        Set<model.PictureAttribute> keySet = compactKeyPointsPairs.keySet();

        Iterator<model.PictureAttribute> it = keySet.iterator();

        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File("/home/konrad/Pulpit/zdjeciaSI/" + path1 + ".png"));
            img2 = ImageIO.read(new File("/home/konrad/Pulpit/zdjeciaSI/" + path2 + ".png"));
        } catch (IOException ignored) {
        }
        while (it.hasNext()) {
            model.PictureAttribute pictureAttribute1 = it.next();
            model.PictureAttribute pictureAttribute2 = compactKeyPointsPairs.get(pictureAttribute1);
            img1.setRGB((int) pictureAttribute1.getCoordinateX(), (int) pictureAttribute1.getCoordinateY(), Color.RED.getRGB());
            img2.setRGB((int) pictureAttribute2.getCoordinateX(), (int) pictureAttribute2.getCoordinateY(), Color.RED.getRGB());
        }
        images.add(img1);
        images.add(img2);

        return images;
    }


    private static List<BufferedImage> getRansacImages(Map<PictureAttribute, PictureAttribute> keyPoint) {
        List<BufferedImage> images = new ArrayList<>();
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File("/home/konrad/Pulpit/zdjeciaSI/" + path1 + ".png"));
            img2 = ImageIO.read(new File("/home/konrad/Pulpit/zdjeciaSI/" + path2 + ".png"));
        } catch (IOException ignored) {
        }
        Iterator<PictureAttribute> it = keyPoint.keySet().iterator();
        for (int i = 0; i < 1; i++) {
            PictureAttribute pictureAttribute1 = it.next();
            Matrix m = new Matrix(3, 1);
            m.set(0, 0, pictureAttribute1.getCoordinateX());
            m.set(1, 0, pictureAttribute1.getCoordinateY());
            m.set(2, 0, 1);
            Model ransac = null;
            while (ransac == null) {
                try {
                    ransac = Algorithms.ransac(Algorithms.PERSPECTIVE_TRANSFORMATION, keyPoint, 20);
                } catch (RuntimeException ignored) {

                }
            }
            List<PictureAttribute> pointsToMark = Algorithms.pointsToMark;
            for (int j = 0; j < pointsToMark.size(); j++) {
                img1.setRGB((int) pointsToMark.get(j).getCoordinateX(), (int) pointsToMark.get(j).getCoordinateY(), Color.RED.getRGB());
                img2.setRGB((int) keyPoint.get(pointsToMark.get(j)).getCoordinateX(), (int) keyPoint.get(pointsToMark.get(j)).getCoordinateY(), Color.RED.getRGB());
            }
        }
        images.add(img1);
        images.add(img2);
        return images;
    }


    private static void writeImage(String path, BufferedImage img) throws IOException {
        ImageIO.write(img, "png", new File(path + "png"));
    }
}
