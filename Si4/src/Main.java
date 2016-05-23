import java.util.*;

/**
 * Created by konrad on 22.05.16.
 */
public class Main {

    public static void main(String [] args){
        Parser p = new Parser();
        int [][] picure1Value= p.getValues("/home/konrad/Pulpit/zdjeciaSI/komputer1.png.haraff.sift");
        int [][] picure2Value= p.getValues("/home/konrad/Pulpit/zdjeciaSI/komputer2.png.haraff.sift");


        Picture [] picture1 = new Picture[picure1Value.length];
        Picture [] picture2 = new Picture[picure2Value.length];

        for(int i=0;i<picure1Value.length;i++){
            picture1[i] = new Picture(picure1Value[i]);
        }

        for(int i=0;i<picure2Value.length;i++){
            picture2[i] = new Picture(picure2Value[i]);
        }

        KNN knn = new KNN(picture2);

        List<Integer> firstPictureValuesNearestNeighbours= new ArrayList<>();
        List<Integer> secondPictureValuesNearestNeighbours= new ArrayList<>();

        for(Picture pic: picture1){
            firstPictureValuesNearestNeighbours.add(knn.getNearestNeighbour(pic));
        }
        for(Picture pic: picture2){
            secondPictureValuesNearestNeighbours.add(knn.getNearestNeighbour(pic));
        }

        Map<Integer,Integer> keyPoint= new HashMap<>();

        for(int i=0;i<picture1.length;i++){
            if(secondPictureValuesNearestNeighbours.get(firstPictureValuesNearestNeighbours.get(i))==i){
                keyPoint.put(i,firstPictureValuesNearestNeighbours.get(i));
            }
        }

        Set<Integer> keySet = keyPoint.keySet();

        Iterator<Integer> it = keySet.iterator();

        while(it.hasNext()){
            int key = it.next();
            System.out.println(key + "   " + keyPoint.get(key));
        }
    }
}
