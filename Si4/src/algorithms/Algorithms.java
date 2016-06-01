package algorithms;

import Jama.Matrix;
import model.Model;
import model.PictureAttribute;
import transformation.AffineTransformation;
import transformation.PerspectiveTransformation;
import transformation.Transformation;

import java.util.*;

/**
 * Created by konrad on 23.05.16.
 */
public class Algorithms {

    public final static int AFFINE_TRANSFORMATION = 0;
    public final static int PERSPECTIVE_TRANSFORMATION = 1;
    private final static double MAX_ERROR = 5;
    public static List<PictureAttribute> pointsToMark;

    public static Map<PictureAttribute, PictureAttribute> getCompactNeighbourhood(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int n) {
        Map<PictureAttribute,PictureAttribute> compactedKeyPairs = new HashMap<>();
        Iterator<PictureAttribute> it = keyPointsPairs.keySet().iterator();
        List<Integer> toAdd = new ArrayList<>();
        int index = 0;
        while (it.hasNext()) {
            int counter = 0;
            PictureAttribute p1 = it.next();
            Iterator<PictureAttribute> neighbourhoodIterator = p1.getNeighbourhood().iterator();
            while (neighbourhoodIterator.hasNext()) {
                PictureAttribute nextPicture = neighbourhoodIterator.next();
                if(p1.getNeighbourhood().contains(nextPicture)) {
                    PictureAttribute p1Partner = keyPointsPairs.get(p1);
                    List<PictureAttribute> p1PartnerNeighbourhood = p1Partner.getNeighbourhood();
                    if (p1PartnerNeighbourhood.contains(keyPointsPairs.get(nextPicture))) {
                        counter++;
                    }
                }
            }
            if (counter > n) {
                toAdd.add(index);
            }
            index++;
        }
        it = keyPointsPairs.keySet().iterator();
        index = 0;
        while (it.hasNext()) {
            PictureAttribute pictureAttribute = it.next();
            if (toAdd.contains(index++)) {
                compactedKeyPairs.put(pictureAttribute,keyPointsPairs.get(pictureAttribute));
            }
        }
        return compactedKeyPairs;
    }

    public static Model ransac(int transformation, Map<PictureAttribute, PictureAttribute> keyPointsPairs, int iterations,int height,int width) {
        Model bestModel = null;
        int bestScore = 0;

        for (int i = 0; i < iterations; i++) {
            List<PictureAttribute> points = new ArrayList<>();
            Transformation t = getTransformation(transformation, keyPointsPairs, 1,height,width);
            Model model = t.getModel();
            Matrix transformationMatrix = model.getTransformation(0);
            int score = 0;

            Iterator<PictureAttribute> dataIterator = keyPointsPairs.keySet().iterator();
            while (dataIterator.hasNext()) {
                PictureAttribute nextData = dataIterator.next();
                PictureAttribute nextDataPair = keyPointsPairs.get(nextData);

                Matrix pointMatrix = new Matrix(3, 1);
                pointMatrix.set(0, 0, nextData.getCoordinateX());
                pointMatrix.set(1, 0, nextData.getCoordinateY());
                pointMatrix.set(2, 0, 1);
                Matrix pointAfterTransformation = transformationMatrix.times(pointMatrix);

                double transformationX1 = pointAfterTransformation.get(0, 0) / pointAfterTransformation.get(2, 0);
                double transformationY1 = pointAfterTransformation.get(1, 0) / pointAfterTransformation.get(2, 0);

                double transformationX2 = nextDataPair.getCoordinateX();
                double transformationY2 = nextDataPair.getCoordinateY();

                double error = modelError(transformationX1, transformationY1, transformationX2, transformationY2);
                if (error < MAX_ERROR) {
                    points.add(nextData);
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestModel = model;
                pointsToMark = points;
            }

        }
        return bestModel;
    }

    private static double modelError(double transformationX1, double transformationY1, double transformationX2, double transformationY2) {
        return Math.sqrt(Math.pow(transformationX1 - transformationX2, 2) + Math.pow(transformationY1 - transformationY2, 2));
    }

    private static Transformation getTransformation(int tranformation, Map<PictureAttribute, PictureAttribute> keyPointsPairs, int modelSize,int height,int width) {
        Transformation t;
        switch (tranformation) {
            case AFFINE_TRANSFORMATION:
                t = new AffineTransformation(keyPointsPairs, modelSize,height,width);
                break;
            default:
                t = new PerspectiveTransformation(keyPointsPairs, modelSize,height,width);
        }
        return t;
    }
}
