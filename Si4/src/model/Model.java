package model;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konrad on 29.05.16.
 */
public class Model {
    private List<Matrix> firstPictureCoordinatesMatrixesList= new ArrayList<>();
    private List<Matrix> secondPictureCoordinatesMatrixesList = new ArrayList<>();
    private List<Matrix> transformationsList = new ArrayList<>();

    public void addElementToModel(Matrix firstPictureCoordinatesMatrix,Matrix secondPictureCoordinatesMatrix,Matrix transformation){
        firstPictureCoordinatesMatrixesList.add(firstPictureCoordinatesMatrix);
        secondPictureCoordinatesMatrixesList.add(secondPictureCoordinatesMatrix);
        transformationsList.add(transformation);
    }

    public Matrix getFirstPictureCoordinatesMatrix(int n){
        return firstPictureCoordinatesMatrixesList.get(n);
    }

    public Matrix getSecondPictureCoordinatesMatrix(int n){
        return secondPictureCoordinatesMatrixesList.get(n);
    }

    public Matrix getTransformation(int n){
        return transformationsList.get(n);
    }
    public int getSize(){
        return transformationsList.size();
    }
}
