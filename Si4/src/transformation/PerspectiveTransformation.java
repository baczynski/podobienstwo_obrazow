package transformation;

import Jama.Matrix;
import model.PictureAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by konrad on 30.05.16.
 */
public class PerspectiveTransformation extends Transformation {
    private static final int numberOfKeyPointsPairs = 4;

    public PerspectiveTransformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int modelSize, int height, int width) {
        super(keyPointsPairs, modelSize, height, width);
    }

    @Override
    protected int getNumberOfKeyPointsPairs() {
        return numberOfKeyPointsPairs;
    }

    @Override
    protected Matrix getFirstPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
        randomlyChosenPairs = getNPairs(keyPointsPairs);
        Matrix matrix = new Matrix(8, 8);
        Iterator<PictureAttribute> it = randomlyChosenPairs.keySet().iterator();
        PictureAttribute xy1 = it.next();
        PictureAttribute xy2 = it.next();
        PictureAttribute xy3 = it.next();
        PictureAttribute xy4 = it.next();

        matrix.set(0, 0, xy1.getCoordinateX());
        matrix.set(0, 1, xy1.getCoordinateY());
        matrix.set(1, 0, xy2.getCoordinateX());
        matrix.set(1, 1, xy2.getCoordinateY());
        matrix.set(2, 0, xy3.getCoordinateX());
        matrix.set(2, 1, xy3.getCoordinateY());
        matrix.set(3, 0, xy4.getCoordinateX());
        matrix.set(3, 1, xy4.getCoordinateY());

        matrix.set(4, 3, xy1.getCoordinateX());
        matrix.set(4, 4, xy1.getCoordinateY());
        matrix.set(5, 3, xy2.getCoordinateX());
        matrix.set(5, 4, xy2.getCoordinateY());
        matrix.set(6, 3, xy3.getCoordinateX());
        matrix.set(6, 4, xy3.getCoordinateY());
        matrix.set(7, 3, xy4.getCoordinateY());
        matrix.set(7, 4, xy4.getCoordinateY());

        matrix.set(0, 2, 1);
        matrix.set(1, 2, 1);
        matrix.set(2, 2, 1);
        matrix.set(3, 2, 1);
        matrix.set(4, 5, 1);
        matrix.set(5, 5, 1);
        matrix.set(6, 5, 1);
        matrix.set(7, 5, 1);


        matrix.set(0, 6, (-(xy1.getCoordinateX() * keyPointsPairs.get(xy1).getCoordinateX())));
        matrix.set(0, 7, (-(xy1.getCoordinateY() * keyPointsPairs.get(xy1).getCoordinateX())));
        matrix.set(1, 6, (-(xy2.getCoordinateX() * keyPointsPairs.get(xy2).getCoordinateX())));
        matrix.set(1, 7, (-(xy2.getCoordinateY() * keyPointsPairs.get(xy2).getCoordinateX())));
        matrix.set(2, 6, (-(xy3.getCoordinateX() * keyPointsPairs.get(xy3).getCoordinateX())));
        matrix.set(2, 7, (-(xy3.getCoordinateY() * keyPointsPairs.get(xy3).getCoordinateX())));
        matrix.set(3, 6, (-(xy4.getCoordinateX() * keyPointsPairs.get(xy4).getCoordinateX())));
        matrix.set(3, 7, (-(xy4.getCoordinateY() * keyPointsPairs.get(xy4).getCoordinateX())));
        matrix.set(4, 6, (-(xy1.getCoordinateX() * keyPointsPairs.get(xy1).getCoordinateY())));
        matrix.set(4, 7, (-(xy1.getCoordinateY() * keyPointsPairs.get(xy1).getCoordinateY())));
        matrix.set(5, 6, (-(xy2.getCoordinateX() * keyPointsPairs.get(xy2).getCoordinateY())));
        matrix.set(5, 7, (-(xy2.getCoordinateY() * keyPointsPairs.get(xy2).getCoordinateY())));
        matrix.set(6, 6, (-(xy3.getCoordinateX() * keyPointsPairs.get(xy3).getCoordinateY())));
        matrix.set(6, 7, (-(xy3.getCoordinateY() * keyPointsPairs.get(xy3).getCoordinateY())));
        matrix.set(7, 6, (-(xy4.getCoordinateX() * keyPointsPairs.get(xy4).getCoordinateY())));
        matrix.set(7, 7, (-(xy4.getCoordinateY() * keyPointsPairs.get(xy4).getCoordinateY())));
        return matrix;
    }

    @Override
    protected Matrix getSecondPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
        Iterator<PictureAttribute> it = randomlyChosenPairs.keySet().iterator();
        PictureAttribute xy1 = it.next();
        PictureAttribute xy2 = it.next();
        PictureAttribute xy3 = it.next();
        PictureAttribute xy4 = it.next();

        PictureAttribute uv1 = keyPointsPairs.get(xy1);
        PictureAttribute uv2 = keyPointsPairs.get(xy2);
        PictureAttribute uv3 = keyPointsPairs.get(xy3);
        PictureAttribute uv4 = keyPointsPairs.get(xy4);


        Matrix vector = new Matrix(8, 1);
        vector.set(0, 0, uv1.getCoordinateX());
        vector.set(1, 0, uv2.getCoordinateX());
        vector.set(2, 0, uv3.getCoordinateX());
        vector.set(3, 0, uv4.getCoordinateX());
        vector.set(4, 0, uv1.getCoordinateY());
        vector.set(5, 0, uv2.getCoordinateY());
        vector.set(6, 0, uv3.getCoordinateY());
        vector.set(7, 0, uv4.getCoordinateY());
        return vector;
    }

    @Override
    protected Matrix getResultMatrix(Matrix vector) {
        Matrix result = new Matrix(3, 3);

        result.set(0, 0, vector.get(0, 0));
        result.set(0, 1, vector.get(1, 0));
        result.set(0, 2, vector.get(2, 0));
        result.set(1, 0, vector.get(3, 0));
        result.set(1, 1, vector.get(4, 0));
        result.set(1, 2, vector.get(5, 0));
        result.set(2, 0, vector.get(6, 0));
        result.set(2, 1, vector.get(7, 0));

        result.set(2, 2, 1);

        return result;
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected Map<PictureAttribute, PictureAttribute> getNPairs(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
        Map<PictureAttribute, PictureAttribute> chosenPairs = new HashMap<>();
        ArrayList<Integer> randomlyPickedNumbers = new ArrayList<>();
        int currentIteration =0;
        while (randomlyPickedNumbers.size() < 4) {
            addRandomValueToList(randomlyPickedNumbers, keyPointsPairs.size());

            if (randomlyPickedNumbers.size() == 1) {
                addRandomValueToList(randomlyPickedNumbers, keyPointsPairs.size());
                PictureAttribute first = getPictureAttributeFromMap(keyPointsPairs.keySet(), randomlyPickedNumbers.get(0));
                chosenPairs.put(first, keyPointsPairs.get(first));
                currentIteration =0;
            }
            if (randomlyPickedNumbers.size() == 2) {
                Iterator<PictureAttribute> it = chosenPairs.keySet().iterator();
                PictureAttribute p1 = it.next();
                PictureAttribute p1Pair = keyPointsPairs.get(p1);
                PictureAttribute p2 = getPictureAttributeFromMap(keyPointsPairs.keySet(), randomlyPickedNumbers.get(1));
                PictureAttribute p2Pair = keyPointsPairs.get(p2);

                if (distanceSmaller(p1, p2, Math.pow(r,2)) || distanceLonger(p1, p2, Math.pow(R,2)) || distanceSmaller(p1Pair, p2Pair, Math.pow(r,2)) || distanceLonger(p1Pair, p2Pair, Math.pow(R,2))) {
                    randomlyPickedNumbers.remove(randomlyPickedNumbers.size() - 1);
                    currentIteration++;
                } else {
                    chosenPairs.put(p2, p2Pair);
                    currentIteration =0;
                }
            }
            if (randomlyPickedNumbers.size() == 3) {
                Iterator<PictureAttribute> it = chosenPairs.keySet().iterator();
                PictureAttribute p1 = it.next();
                PictureAttribute p1Pair = keyPointsPairs.get(p1);
                PictureAttribute p2 = it.next();
                PictureAttribute p2Pair = keyPointsPairs.get(p2);
                PictureAttribute p3 = getPictureAttributeFromMap(keyPointsPairs.keySet(), randomlyPickedNumbers.get(2));
                PictureAttribute p3Pair = keyPointsPairs.get(p3);

                if (distanceSmaller(p1, p3, Math.pow(r,2)) || distanceLonger(p1, p3, Math.pow(R,2)) || distanceSmaller(p1Pair, p3Pair, Math.pow(r,2)) || distanceLonger(p1Pair, p3Pair, Math.pow(R,2))
                        || distanceSmaller(p2, p3, Math.pow(r,2)) || distanceLonger(p2, p3, Math.pow(R,2)) || distanceSmaller(p2Pair, p3Pair, Math.pow(r,2)) || distanceLonger(p2Pair, p3Pair,Math.pow(R,2))) {
                    randomlyPickedNumbers.remove(randomlyPickedNumbers.size() - 1);
                    currentIteration++;
                } else {
                    chosenPairs.put(p3, p3Pair);
                    currentIteration =0;
                }
            }
            if(randomlyPickedNumbers.size()==4){
                Iterator<PictureAttribute> it = chosenPairs.keySet().iterator();
                PictureAttribute p1 = it.next();
                PictureAttribute p1Pair = keyPointsPairs.get(p1);
                PictureAttribute p2 = it.next();
                PictureAttribute p2Pair = keyPointsPairs.get(p2);
                PictureAttribute p3 = it.next();
                PictureAttribute p3Pair = keyPointsPairs.get(p3);
                PictureAttribute p4 = getPictureAttributeFromMap(keyPointsPairs.keySet(), randomlyPickedNumbers.get(3));
                PictureAttribute p4Pair = keyPointsPairs.get(p4);

                if (distanceSmaller(p1, p4, Math.pow(r,2)) || distanceLonger(p1, p4, Math.pow(R,2)) || distanceSmaller(p1Pair, p4Pair,Math.pow(r,2)) || distanceLonger(p1Pair, p4Pair, Math.pow(R,2))
                        || distanceSmaller(p2, p4, Math.pow(r,2)) || distanceLonger(p2, p4, Math.pow(R,2)) || distanceSmaller(p2Pair, p4Pair, Math.pow(r,2)) || distanceLonger(p2Pair, p4Pair, Math.pow(R,2))
                        || distanceSmaller(p3, p4, Math.pow(r,2)) || distanceLonger(p3, p4, Math.pow(R,2)) || distanceSmaller(p3Pair, p4Pair, Math.pow(r,2)) || distanceLonger(p3Pair, p4Pair, Math.pow(R,2))) {

                    randomlyPickedNumbers.remove(randomlyPickedNumbers.size() - 1);
                    currentIteration++;
                } else {
                    chosenPairs.put(p4, p4Pair);
                    currentIteration =0;
                }
            }
            if(currentIteration >50){
                Iterator<PictureAttribute> it = chosenPairs.keySet().iterator();
                for(int i=0;i<randomlyPickedNumbers.size()-1;i++){
                    it.next();
                }
                chosenPairs.remove(it.next());
                randomlyPickedNumbers.remove(randomlyPickedNumbers.size() - 1);
            }
        }
        return chosenPairs;
    }
}