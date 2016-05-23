import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by konrad on 23.05.16.
 */
public class Algorithms {

    public static Map<PictureAttribute,PictureAttribute> getCompactNeighbourhood(Map<PictureAttribute,PictureAttribute> keyPointsPairs, int n){
        Iterator<PictureAttribute> it = keyPointsPairs.keySet().iterator();

        while(it.hasNext()){
            int counter =0;
            PictureAttribute p1 = it.next();
            List<PictureAttribute> p1Neighbourhood = p1.getNeighbourhood();
            Iterator<PictureAttribute> neighbourhoodIterator = keyPointsPairs.values().iterator();
            while(neighbourhoodIterator.hasNext()){
                PictureAttribute nextPicture = neighbourhoodIterator.next();
                if(nextPicture != p1 && p1Neighbourhood.contains(nextPicture)){
                    PictureAttribute p1Partner = getKeyForValue(keyPointsPairs,nextPicture);
                    List<PictureAttribute> nextPicturePartnerNeighbourhood = keyPointsPairs.get(p1).getNeighbourhood();
                    if(nextPicturePartnerNeighbourhood.contains(p1Partner)){
                        counter++;
                    }
                }
            }
            if(counter<n){
                it.remove();
            }
        }
        return keyPointsPairs;
    }
    private static PictureAttribute getKeyForValue(Map<PictureAttribute,PictureAttribute> pictureAttributeMap,PictureAttribute pictureAttribute){
        boolean found =false;
        PictureAttribute p=null;
        Iterator<PictureAttribute> it = pictureAttributeMap.keySet().iterator();
        while (it.hasNext() && !found){
            p = it.next();
            if(pictureAttributeMap.get(p).equals(pictureAttribute)){
                found = true;
            }
        }
        if(found){
            return p;
        }
        else
            return null;
    }
}
