package transformation;

import Jama.Matrix;
import model.PictureAttribute;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by konrad on 30.05.16.
 */
public class PerspectiveTransformation extends Transformation {
    private static final int numberOfKeyPointsPairs = 4;

    public PerspectiveTransformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int modelSize) {
        super(keyPointsPairs, modelSize);
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


        matrix.set(0,6, (-(xy1.getCoordinateX() * keyPointsPairs.get(xy1).getCoordinateX())));
        matrix.set(0,7, (-(xy1.getCoordinateY() * keyPointsPairs.get(xy1).getCoordinateX())));
        matrix.set(1,6, (-(xy2.getCoordinateX() * keyPointsPairs.get(xy2).getCoordinateX())));
        matrix.set(1,7, (-(xy2.getCoordinateY() * keyPointsPairs.get(xy2).getCoordinateX())));
        matrix.set(2,6, (-(xy3.getCoordinateX() * keyPointsPairs.get(xy3).getCoordinateX())));
        matrix.set(2,7, (-(xy3.getCoordinateY() * keyPointsPairs.get(xy3).getCoordinateX())));
        matrix.set(3,6, (-(xy4.getCoordinateX() * keyPointsPairs.get(xy4).getCoordinateX())));
        matrix.set(3,7, (-(xy4.getCoordinateY() * keyPointsPairs.get(xy4).getCoordinateX())));
        matrix.set(4,6, (-(xy1.getCoordinateX() * keyPointsPairs.get(xy1).getCoordinateY())));
        matrix.set(4,7, (-(xy1.getCoordinateY() * keyPointsPairs.get(xy1).getCoordinateY())));
        matrix.set(5,6, (-(xy2.getCoordinateX() * keyPointsPairs.get(xy2).getCoordinateY())));
        matrix.set(5,7, (-(xy2.getCoordinateY() * keyPointsPairs.get(xy2).getCoordinateY())));
        matrix.set(6,6, (-(xy3.getCoordinateX() * keyPointsPairs.get(xy3).getCoordinateY())));
        matrix.set(6,7, (-(xy3.getCoordinateY() * keyPointsPairs.get(xy3).getCoordinateY())));
        matrix.set(7,6, (-(xy4.getCoordinateX() * keyPointsPairs.get(xy4).getCoordinateY())));
        matrix.set(7,7, (-(xy4.getCoordinateY() * keyPointsPairs.get(xy4).getCoordinateY())));
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
        vector.set(2, 0, uv4.getCoordinateX());
        vector.set(3, 0, uv1.getCoordinateY());
        vector.set(4, 0, uv2.getCoordinateY());
        vector.set(5, 0, uv3.getCoordinateY());
        vector.set(6, 0, uv4.getCoordinateY());
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
}
