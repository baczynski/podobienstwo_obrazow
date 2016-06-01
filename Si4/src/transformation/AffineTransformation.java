package transformation;

import Jama.Matrix;
import model.PictureAttribute;

import java.util.*;

/**
 * Created by konrad on 27.05.16.
 */
public class AffineTransformation extends Transformation {
    private static final int numberOfKeyPointsPairs = 3;

    public AffineTransformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int modelSize,int height,int width) {
        super(keyPointsPairs, modelSize,height,width);
    }

    @Override
    protected int getNumberOfKeyPointsPairs() {
        return numberOfKeyPointsPairs;
    }


    @Override
    protected Matrix getFirstPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
        randomlyChosenPairs = getNPairs(keyPointsPairs);
        Matrix matrix = new Matrix(6, 6);
        Iterator<PictureAttribute> it = randomlyChosenPairs.keySet().iterator();
        PictureAttribute xy1 = it.next();
        PictureAttribute xy2 = it.next();
        PictureAttribute xy3 = it.next();

        matrix.set(0, 0, xy1.getCoordinateX());
        matrix.set(0, 1, xy1.getCoordinateY());
        matrix.set(1, 0, xy2.getCoordinateX());
        matrix.set(1, 1, xy2.getCoordinateY());
        matrix.set(2, 0, xy3.getCoordinateX());
        matrix.set(2, 1, xy3.getCoordinateY());

        matrix.set(3, 3, xy1.getCoordinateX());
        matrix.set(3, 4, xy1.getCoordinateY());
        matrix.set(4, 3, xy2.getCoordinateX());
        matrix.set(4, 4, xy2.getCoordinateY());
        matrix.set(5, 3, xy3.getCoordinateX());
        matrix.set(5, 4, xy3.getCoordinateY());

        matrix.set(0, 2, 1);
        matrix.set(1, 2, 1);
        matrix.set(2, 2, 1);
        matrix.set(3, 5, 1);
        matrix.set(4, 5, 1);
        matrix.set(5, 5, 1);
        return matrix;
    }

    @Override
    protected Matrix getSecondPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {

        Iterator<PictureAttribute> it = randomlyChosenPairs.keySet().iterator();
        PictureAttribute xy1 = it.next();
        PictureAttribute xy2 = it.next();
        PictureAttribute xy3 = it.next();

        PictureAttribute uv1 = keyPointsPairs.get(xy1);
        PictureAttribute uv2 = keyPointsPairs.get(xy2);
        PictureAttribute uv3 = keyPointsPairs.get(xy3);


        Matrix vector = new Matrix(6, 1);
        vector.set(0, 0, uv1.getCoordinateX());
        vector.set(1, 0, uv2.getCoordinateX());
        vector.set(2, 0, uv3.getCoordinateX());
        vector.set(3, 0, uv1.getCoordinateY());
        vector.set(4, 0, uv2.getCoordinateY());
        vector.set(5, 0, uv3.getCoordinateY());
        return vector;

    }

        @SuppressWarnings("Duplicates")
        @Override
        protected Map<PictureAttribute, PictureAttribute> getNPairs(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
            Map<PictureAttribute, PictureAttribute> chosenPairs = new HashMap<>();
            ArrayList<Integer> randomlyPickedNumbers = new ArrayList<>();
            int currentIteration = 0;
            while (randomlyPickedNumbers.size() < 3) {
                addRandomValueToList(randomlyPickedNumbers, keyPointsPairs.size());

                if (randomlyPickedNumbers.size() == 1) {
                    addRandomValueToList(randomlyPickedNumbers, keyPointsPairs.size());
                    PictureAttribute first = getPictureAttributeFromMap(keyPointsPairs.keySet(), randomlyPickedNumbers.get(0));
                    chosenPairs.put(first, keyPointsPairs.get(first));
                    currentIteration=0;
                }
                if (randomlyPickedNumbers.size() == 2) {
                    Iterator<PictureAttribute> it = chosenPairs.keySet().iterator();
                    PictureAttribute p1 = it.next();
                    PictureAttribute p1Pair = keyPointsPairs.get(p1);
                    PictureAttribute p2 = getPictureAttributeFromMap(keyPointsPairs.keySet(), randomlyPickedNumbers.get(1));
                    PictureAttribute p2Pair = keyPointsPairs.get(p2);

                    if (distanceSmaller(p1, p2, Math.pow(r, 2)) || distanceLonger(p1, p2, Math.pow(R, 2)) || distanceSmaller(p1Pair, p2Pair, Math.pow(r, 2)) || distanceLonger(p1Pair, p2Pair, Math.pow(R, 2))) {
                        randomlyPickedNumbers.remove(randomlyPickedNumbers.size() - 1);
                        currentIteration++;

                    } else {
                        chosenPairs.put(p2, p2Pair);
                        currentIteration=0;
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

                    if (distanceSmaller(p1, p3, Math.pow(r, 2)) || distanceLonger(p1, p3, Math.pow(R, 2)) || distanceSmaller(p1Pair, p3Pair, Math.pow(r, 2)) || distanceLonger(p1Pair, p3Pair, Math.pow(R, 2))
                            || distanceSmaller(p2, p3, Math.pow(r, 2)) || distanceLonger(p2, p3, Math.pow(R, 2)) || distanceSmaller(p2Pair, p3Pair, Math.pow(r, 2)) || distanceLonger(p2Pair, p3Pair, Math.pow(R, 2))) {
                        randomlyPickedNumbers.remove(randomlyPickedNumbers.size() - 1);
                        currentIteration++;
                    } else {
                        chosenPairs.put(p3, p3Pair);
                        currentIteration=0;
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

    @Override
    protected Matrix getResultMatrix(Matrix vector) {
        Matrix result = new Matrix(3, 3);

        result.set(0, 0, vector.get(0, 0));
        result.set(0, 1, vector.get(1, 0));
        result.set(0, 2, vector.get(2, 0));
        result.set(1, 0, vector.get(3, 0));
        result.set(1, 1, vector.get(4, 0));
        result.set(1, 2, vector.get(5, 0));
        result.set(2, 2, 1);

        return result;
    }


}

