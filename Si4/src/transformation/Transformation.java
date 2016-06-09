package transformation;

import Jama.Matrix;
import model.Model;
import model.PictureAttribute;

import java.util.*;

/**
 * Created by konrad on 27.05.16.
 */
public abstract class Transformation {

    protected Model model;
    protected Map<PictureAttribute, PictureAttribute> randomlyChosenPairs;
    protected int pictureSize;
    public static double r = 0.01;
    public static double R = 0.3;
    private static boolean isRrSet = false;

    public Transformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int modelSize, int height, int width) {
        model = new Model();
        pictureSize = height > width ? height : width;
        if (!isRrSet) {
            r *= pictureSize;
            R *= pictureSize;
            isRrSet = true;
        }
        for (int i = 0; i < modelSize; i++) {
            addTransformation(keyPointsPairs);
        }

    }

    protected abstract int getNumberOfKeyPointsPairs();

    protected abstract Matrix getFirstPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs);

    protected abstract Matrix getSecondPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs);

    protected abstract Map<PictureAttribute, PictureAttribute> getNPairs(Map<PictureAttribute, PictureAttribute> keyPointsPairs);


    private void addTransformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
        Matrix m = getFirstPictureCoordinatesMatrix(keyPointsPairs);
        Matrix vector = getSecondPictureCoordinatesMatrix(keyPointsPairs);


        Matrix matrix = m.inverse();
        Matrix vector1 = matrix.times(vector);

        Matrix result = getResultMatrix(vector1);

        model.addElementToModel(m, vector, result);
    }

    protected abstract Matrix getResultMatrix(Matrix vector);

    public final Model getModel() {
        return model;
    }

    protected boolean distanceLonger(PictureAttribute p1, PictureAttribute p2, double x) {
        //System.out.println(Math.pow(p1.getCoordinateX() - p2.getCoordinateX(),2)+ Math.pow(p1.getCoordinateX() - p2.getCoordinateX(),2));
        return Math.pow(p1.getCoordinateX() - p2.getCoordinateX(), 2) + Math.pow(p1.getCoordinateX() - p2.getCoordinateX(), 2) > x;

    }

    protected boolean distanceSmaller(PictureAttribute p1, PictureAttribute p2, double x) {
        //System.out.println(Math.pow(p1.getCoordinateX() - p2.getCoordinateX(),2)+ Math.pow(p1.getCoordinateX() - p2.getCoordinateX(),2));
        return Math.pow(p1.getCoordinateX() - p2.getCoordinateX(), 2) + Math.pow(p1.getCoordinateX() - p2.getCoordinateX(), 2) < x;
    }

    protected void addRandomValueToList(List<Integer> randomlyPickedNumbers, int keyPointsPairsSize) {
        Random random = new Random();
        int drewNumber = random.nextInt(keyPointsPairsSize);
        while (randomlyPickedNumbers.contains(drewNumber)) {
            drewNumber = random.nextInt(keyPointsPairsSize);
        }

        randomlyPickedNumbers.add(drewNumber);
    }

    protected PictureAttribute getPictureAttributeFromMap(Set<PictureAttribute> keyset, int numberOfKeyPointsPairs) {

        int current = 0;
        Iterator<PictureAttribute> iterator = keyset.iterator();
        while (current != numberOfKeyPointsPairs) {
            iterator.next();
            current++;
        }
        return iterator.next();

    }
}
