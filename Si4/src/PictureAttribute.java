import java.util.List;
import java.util.Objects;

/**
 * Created by konrad on 22.05.16.
 */
public class PictureAttribute {
    private int [] pictureAttributes;
    private int label;
    private double coordinateX;
    private double coordinateY;
    private List<PictureAttribute> neighbourhood;

    public PictureAttribute(int [] pictureAttributes,double coordinateX,double coordinateY,int label){
        this.label = label;
        this.pictureAttributes=pictureAttributes;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }
    public int getLabel(){
        return label;
    }
    public int [] getPictureAttributes(){
        return pictureAttributes;
    }
    public void setNeighbourHood(List<PictureAttribute> neighbourhood){
        this.neighbourhood = neighbourhood;
    }
    public List<PictureAttribute> getNeighbourhood(){
        return neighbourhood;
    }
    public double getCoordinateX(){
        return coordinateX;
    }
    public double getCoordinateY(){
        return coordinateY;
    }
    public String toString(){
        return Integer.toString(label);
    }

    public PictureAttribute getNearestNeighbour(){
        return neighbourhood.get(0);
    }
    public int getNearestNeighbourLabel(){
        return getNearestNeighbour().getLabel();
    }
    @Override
    public boolean equals(Object p){
        PictureAttribute pictureAttribute = (PictureAttribute) p;
        return label == pictureAttribute.getLabel() && coordinateX == pictureAttribute.getCoordinateX() && coordinateY == pictureAttribute.getCoordinateY();
    }
}
