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

    private static final String path = "/home/konrad/Pulpit/zdjeciaSi/";
    private static final String path1 = "castle1";
    private static final String path2 = "castle2";

    private static final int kNearest = 50;
    private static final String path3 = path1+path2;
    private static final int iterations = 1000;
    private static final int neighbourhood = 25;

    public static void main(String[] args) {
        Parser parser = new Parser();
        PictureAttribute[] picture1 = parser.getValues(path + path1 + ".png.haraff.sift");
        PictureAttribute[] picture2 = parser.getValues(path + path2 + ".png.haraff.sift");

        setNearestNeighbour(picture1, picture2);

        Map<PictureAttribute, PictureAttribute> keyPoint = getKeyPointsPairs(picture1, picture2);
        Map<PictureAttribute, PictureAttribute> keyPointRansac = getKeyPointsPairs(picture1, picture2);

        setPictureNeighbourhood(picture1, picture2, keyPoint);

        long startTimenNeighbours = System.currentTimeMillis();

        List<BufferedImage> neighbourAnalysisImagesList = getNeighbourhoodAnalysisImage(keyPoint);

        long endTimeNeighbours  = System.currentTimeMillis();

        long startTimeRansacAffine = System.currentTimeMillis();

        List<BufferedImage> ransacBufferedImagesListAffine = getRansacImages(keyPointRansac,Algorithms.AFFINE_TRANSFORMATION);

        long endTimeRansacAffine  = System.currentTimeMillis();

        long startTimeRansacPerspective = System.currentTimeMillis();

        List<BufferedImage> ransacBufferedImagesListPerspective = getRansacImages(keyPointRansac,Algorithms.PERSPECTIVE_TRANSFORMATION);

        long endTimeRansacPerspective = System.currentTimeMillis();

        try {
            writeImage(path1 + "ransacAffine", ransacBufferedImagesListAffine.get(0));
            writeImage(path2 + "ransacAffine", ransacBufferedImagesListAffine.get(1));
            writeImage(path3 + "ransacAffine", ransacBufferedImagesListAffine.get(2));
            writeImage(path3 + "ransacPerspective", ransacBufferedImagesListPerspective.get(0));
            writeImage(path3 + "ransacPerspective", ransacBufferedImagesListPerspective.get(1));
            writeImage(path3 + "ransacPerspective", ransacBufferedImagesListPerspective.get(2));
            writeImage(path1 + "neighbourhoodAnalysis", neighbourAnalysisImagesList.get(0));
            writeImage(path2 + "neighbourhoodAnalysis", neighbourAnalysisImagesList.get(1));
            writeImage(path3 + "neighbourhoodAnalysis", neighbourAnalysisImagesList.get(2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Analiza sąsiedztwa: " + (endTimeNeighbours - startTimenNeighbours));
        System.out.println("Ransac afiniczna: " + (endTimeRansacAffine - startTimeRansacAffine));
        System.out.println("Ransac perspektywiczna: " + (endTimeRansacPerspective - startTimeRansacPerspective));

    }


    public static int ileWystapien(String napis,char c){
        return (int) napis.chars().filter(n -> n==c).count();
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
        Map<model.PictureAttribute, model.PictureAttribute> compactKeyPointsPairs = algorithms.Algorithms.getCompactNeighbourhood(keyPoint, neighbourhood);

        Set<model.PictureAttribute> keySet = compactKeyPointsPairs.keySet();

        Iterator<model.PictureAttribute> it = keySet.iterator();

        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File(path + path1 + ".png"));
            img2 = ImageIO.read(new File(path+ path2 + ".png"));
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
        images.add(drawLines(img1,img2,new ArrayList<>(keySet),keyPoint));
        return images;
    }


    private static List<BufferedImage> getRansacImages(Map<PictureAttribute, PictureAttribute> keyPoint,int transformation) {
        List<BufferedImage> images = new ArrayList<>();
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File(path+ path1 + ".png"));
            img2 = ImageIO.read(new File(path+ path2 + ".png"));
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
                    ransac = Algorithms.ransac(transformation, keyPoint, iterations,img1.getHeight(),img1.getWidth());
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
        images.add(drawLines(img1,img2,Algorithms.pointsToMark,keyPoint));
        return images;
    }

    private static BufferedImage drawLines(BufferedImage img1,BufferedImage img2,List<PictureAttribute> pointsToMark,Map<PictureAttribute,PictureAttribute> keyPointsPairs){
        BufferedImage bi = new BufferedImage(img1.getWidth()+img2.getWidth(),img1.getHeight()>img2.getHeight()?img1.getHeight():img2.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img1.getWidth(); i++) {
            for (int j = 0; j < img1.getHeight(); j++) {
                bi.setRGB(i,j,img1.getRGB(i,j));
            }
        }
        for (int i = 0; i < img2.getWidth(); i++) {
            for (int j = 0; j < img2.getHeight(); j++) {
                bi.setRGB(img1.getWidth()+ i,j,img2.getRGB(i,j));
            }
        }
        Graphics g = bi.createGraphics();

        for (int i = 0; i < pointsToMark.size(); i++) {
                int xf =(int) pointsToMark.get(i).getCoordinateX();
                int yf =(int) pointsToMark.get(i).getCoordinateY();
                int xs=(int) keyPointsPairs.get(pointsToMark.get(i)).getCoordinateX();
                int ys=(int) keyPointsPairs.get(pointsToMark.get(i)).getCoordinateY();
                img2.setRGB(xs, ys,Color.RED.getRGB());
                img1.setRGB(xf,yf,Color.RED.getRGB());
                g.drawLine(xf,yf,img1.getWidth()+xs, ys);


        }
        return bi;
    }


    private static void writeImage(String path, BufferedImage img) throws IOException {
        ImageIO.write(img, "png", new File(path + ".png"));
    }
}
