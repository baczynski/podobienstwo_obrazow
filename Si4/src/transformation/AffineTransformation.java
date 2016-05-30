package transformation;

import Jama.Matrix;
import model.PictureAttribute;

import java.util.*;

/**
 * Created by konrad on 27.05.16.
 */
public class AffineTransformation extends Transformation {
    private static final int numberOfKeyPointsPairs = 3;

    public AffineTransformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int modelSize) {
        super(keyPointsPairs, modelSize);
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
