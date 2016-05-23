import java.util.*;

/**
 * Created by konrad on 22.05.16.
 */
public class Main {

    public static void main(String [] args){
        Parser p = new Parser();
        PictureAttribute [] picture1= p.getValues("/home/konrad/Pulpit/zdjeciaSI/ksiazka1.png.haraff.sift");
        PictureAttribute [] picture2= p.getValues("/home/konrad/Pulpit/zdjeciaSI/ksiazka2.png.haraff.sift");

        int kNearest =60;


        for(int i=0;i<picture1.length;i++){
            KNN knn = new KNN(picture2,picture1[i]);
            picture1[i].setNeighbourHood(knn.getNearestNeighbours(kNearest));
        }
        for(int i=0;i<picture2.length;i++){
            KNN knn = new KNN(picture1,picture2[i]);
            picture2[i].setNeighbourHood(knn.getNearestNeighbours(kNearest));
        }

        Map<PictureAttribute,PictureAttribute> keyPoint= new HashMap<>();

        for(int i=0;i<picture1.length;i++){
            if(picture2[picture1[i].getNearestNeighbourLabel()].getNearestNeighbourLabel()==i){
                keyPoint.put(picture1[i],picture1[i].getNearestNeighbour());
            }
        }
//        Set<PictureAttribute> keySet = keyPoint.keySet();
//
//        Iterator<PictureAttribute> it = keySet.iterator();
//
//        while(it.hasNext()){
//            PictureAttribute key = it.next();
//            System.out.println(key + "   " + keyPoint.get(key));
//        }

        Map<PictureAttribute,PictureAttribute> compactKeyPointsPairs = Algorithms.getCompactNeighbourhood(keyPoint,20);

        Set<PictureAttribute> keySet = compactKeyPointsPairs.keySet();

        Iterator<PictureAttribute> it = keySet.iterator();

        while (it.hasNext()) {
            PictureAttribute key = it.next();
            System.out.println(key + "   " + keyPoint.get(key));
        }
    }
}
