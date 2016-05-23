import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by konrad on 22.05.16.
 */
public class KNN {

    private PictureAttribute[] anotherPicture;
    private PictureAttribute picture;

    public KNN(PictureAttribute[] anotherPicture, PictureAttribute picture) {
        this.picture = picture;
        this.anotherPicture = anotherPicture;
    }


    public PictureAttribute getNearestNeighbour() {
        int minDist = Integer.MAX_VALUE;
        PictureAttribute minDistAttribute = null;
        for (PictureAttribute train : anotherPicture) {
            if (!train.equals(picture)) {
                int dist = 0;
                for (int i = 0; i < train.getPictureAttributes().length; i++) {
                    dist += Math.abs(train.getPictureAttributes()[i] - picture.getPictureAttributes()[i]);
                }
                if (minDist > dist) {
                    minDist = dist;
                    minDistAttribute = train;
                }
            }
        }
        return minDistAttribute;
    }

}
