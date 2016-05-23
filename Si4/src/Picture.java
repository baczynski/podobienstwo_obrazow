/**
 * Created by konrad on 22.05.16.
 */
public class Picture {
    private static int LABEL_SEQUENCE=0;
    private int [] pictureAttributes;
    private int label;

    public Picture(int [] pictureAttributes){
        label = LABEL_SEQUENCE++;
        this.pictureAttributes=pictureAttributes;
    }
    public int getLabel(){
        return label;
    }
    public int [] getPictureAttributes(){
        return pictureAttributes;
    }
}
