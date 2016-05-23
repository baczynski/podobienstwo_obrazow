import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by konrad on 23.05.16.
 */
public class Algorithms {

    public static Map<PictureAttribute, PictureAttribute> getCompactNeighbourhood(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int n) {
        Iterator<PictureAttribute> it = keyPointsPairs.keySet().iterator();
        List<Integer> toRemove = new ArrayList<>();
        int index = 0;
        while (it.hasNext()) {
            int counter = 0;
            PictureAttribute p1 = it.next();
            Iterator<PictureAttribute> neighbourhoodIterator = p1.getNeighbourhood().iterator();
            while (neighbourhoodIterator.hasNext()) {
                PictureAttribute nextPicture = neighbourhoodIterator.next();
                PictureAttribute p1Partner = keyPointsPairs.get(p1);
                List<PictureAttribute> p1PartnerNeighbourhood = p1Partner.getNeighbourhood();
                if (p1PartnerNeighbourhood.contains(keyPointsPairs.get(nextPicture))) {
                    counter++;
                }
            }
            if (counter < n) {
                toRemove.add(index);
            }
            index++;
        }
        it = keyPointsPairs.keySet().iterator();
        index=0;
        while(it.hasNext()){
            it.next();
            if(toRemove.contains(index++)){
                it.remove();
            }
        }
        return keyPointsPairs;
    }

    private static PictureAttribute getKeyForValue(Map<PictureAttribute, PictureAttribute> pictureAttributeMap, PictureAttribute pictureAttribute) {
        boolean found = false;
        PictureAttribute p = null;
        Iterator<PictureAttribute> it = pictureAttributeMap.keySet().iterator();
        while (it.hasNext() && !found) {
            p = it.next();
            if (pictureAttributeMap.get(p).equals(pictureAttribute)) {
                found = true;
            }
        }
        if (found) {
            return p;
        } else
            return null;
    }
}
