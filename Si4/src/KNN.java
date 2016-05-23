import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by konrad on 22.05.16.
 */
public class KNN {

    private PictureAttribute[] trainingPictures;
    private List<Result> resultsList = new ArrayList<>();
    private PictureAttribute picture;

    public KNN(PictureAttribute[] trainingPictures, PictureAttribute picture){
        this.trainingPictures = trainingPictures;
        this.picture = picture;
        sortNeighbours();
    }

    public void sortNeighbours(){
        int current=0;
        for(PictureAttribute train: trainingPictures){
            int dist=0;
            for(int i=0;i<train.getPictureAttributes().length;i++){
                dist += Math.abs(train.getPictureAttributes()[i] - picture.getPictureAttributes()[i]);
            }
            resultsList.add(new Result(current++,dist));
        }
        Collections.sort(resultsList,new DistanceComparator());
    }


    public List<PictureAttribute> getNearestNeighbours(int k){
        List<PictureAttribute> nearestsNeighbours = new ArrayList<>();
        for(int i=0;i<k;i++){
            nearestsNeighbours.add(trainingPictures[resultsList.get(i).getLabel()]);
        }
        return nearestsNeighbours;
    }

    private class DistanceComparator implements Comparator<Result>{

        @Override
        public int compare(Result o1, Result o2) {
            return o1.getDistance() < o2.getDistance() ? -1 : o1.getDistance() ==o2.getDistance() ? 0 : 1;
        }
    }
    private class Result{
        private int distance;
        private int label;
        public Result(int label,int distance){
            this.distance=distance;
            this.label=label;
        }
        public int getDistance(){
            return distance;
        }
        public int getLabel(){
            return label;
        }
    }
}
