package transformation;

import Jama.Matrix;
import model.Model;
import model.PictureAttribute;

import java.util.*;

/**
 * Created by konrad on 27.05.16.
 */
public abstract class Transformation {

    protected Model model;
    protected Map <PictureAttribute, PictureAttribute> randomlyChosenPairs;

    public Transformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs, int modelSize){
        model = new Model();
        for(int i=0;i<modelSize;i++){
            addTransformation(keyPointsPairs);
        }
    }

    protected abstract int getNumberOfKeyPointsPairs();
    protected abstract Matrix getFirstPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs);
    protected abstract Matrix getSecondPictureCoordinatesMatrix(Map<PictureAttribute, PictureAttribute> keyPointsPairs);

    protected Map<PictureAttribute, PictureAttribute> getNPairs(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
        Map<PictureAttribute, PictureAttribute> chosenPairs = new HashMap<>();
        ArrayList<Integer> randomlyPickedNumbers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < getNumberOfKeyPointsPairs(); i++) {
            int drewNumber = random.nextInt(keyPointsPairs.size());
            while(randomlyPickedNumbers.contains(drewNumber)){
                drewNumber = random.nextInt(keyPointsPairs.size());
            }

            randomlyPickedNumbers.add(drewNumber);
        }
        Collections.sort(randomlyPickedNumbers);

        Iterator<PictureAttribute> iterator = keyPointsPairs.keySet().iterator();

        int current = 0;
        int elementNumber = 0;
        while (iterator.hasNext() && elementNumber < randomlyPickedNumbers.size()) {
            PictureAttribute currentPictureAttribute = iterator.next();
            if (randomlyPickedNumbers.get(elementNumber) == current) {
                chosenPairs.put(currentPictureAttribute, keyPointsPairs.get(currentPictureAttribute));
                elementNumber++;
            }
            current++;
        }
        return chosenPairs;
    }


    private void addTransformation(Map<PictureAttribute, PictureAttribute> keyPointsPairs) {
        Matrix m = getFirstPictureCoordinatesMatrix(keyPointsPairs);
        Matrix vector = getSecondPictureCoordinatesMatrix(keyPointsPairs);


        Matrix matrix = m.inverse();
        Matrix vector1 = matrix.times(vector);

        Matrix result = getResultMatrix(vector1);

        model.addElementToModel(m, vector, result);
    }

    protected abstract Matrix getResultMatrix(Matrix vector);

    public final Model getModel(){
        return model;
    }
}
