import com.sun.org.apache.regexp.internal.RE;

import java.util.Comparator;

/**
 * Created by konrad on 22.05.16.
 */
public class KNN {

    private Picture [] trainingPictures;

    public KNN(Picture [] trainingPictures){
        this.trainingPictures = trainingPictures;
    }

    public int getNearestNeighbour(Picture p){
        int nearest=0;
        int distantOfNearest= Integer.MAX_VALUE;
        int current=0;
        for(Picture train: trainingPictures){
            int dist=0;
            for(int i=0;i<train.getPictureAttributes().length;i++){
                dist += Math.abs(train.getPictureAttributes()[i] - p.getPictureAttributes()[i]);
            }
            if(distantOfNearest>dist){
                distantOfNearest = dist;
                nearest = current;
            }
            current++;
        }
        return nearest;
    }
}
