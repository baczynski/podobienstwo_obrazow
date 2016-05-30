package neighbourhoodAnalysis;

import model.PictureAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by konrad on 23.05.16.
 */
public class Neighbourhood {

    private List<Result> resultsList = new ArrayList<>();
    private PictureAttribute[] currentPicture;
    private PictureAttribute picture;

    public Neighbourhood(Object [] currentPicture, PictureAttribute picture){
        this.currentPicture = new PictureAttribute[currentPicture.length];
        for(int i=0;i<currentPicture.length;i++){
            this.currentPicture[i] = (PictureAttribute) currentPicture[i];
        }
        this.picture =picture;
        sortNeighbours();
    }

    public void sortNeighbours() {
        int current = 0;
        for (PictureAttribute train : currentPicture) {
            if(!train.equals(picture)) {
                int dist = 0;
                for (int i = 0; i < train.getPictureAttributes().length; i++) {
                    dist += Math.abs(train.getPictureAttributes()[i] - picture.getPictureAttributes()[i]);
                }
                resultsList.add(new Result(current++, dist));
            }
        }
        Collections.sort(resultsList, new DistanceComparator());
    }

    public List<PictureAttribute> getNearestNeighbours(int k) {
        List<PictureAttribute> nearestsNeighbours = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            nearestsNeighbours.add(currentPicture[resultsList.get(i).getLabel()]);
        }
        return nearestsNeighbours;
    }

    private class DistanceComparator implements Comparator<Result> {

        @Override
        public int compare(Result o1, Result o2) {
            return o1.getDistance() < o2.getDistance() ? -1 : o1.getDistance() == o2.getDistance() ? 0 : 1;
        }
    }

    private class Result {
        private int distance;
        private int label;

        public Result(int label, int distance) {
            this.distance = distance;
            this.label = label;
        }

        public int getDistance() {
            return distance;
        }

        public int getLabel() {
            return label;
        }
    }
}
