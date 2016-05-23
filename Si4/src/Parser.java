/**
 * Created by konrad on 22.05.16.
 */
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Parser {

    private Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.US_ASCII;
    private PictureAttribute[] pictureAttributes;

    public PictureAttribute [] getValues(String aFileName){
        fFilePath = Paths.get(aFileName);
        try {
            processLineByLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pictureAttributes;
    }


    public void processLineByLine() throws IOException {
        try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
            int columns = scanner.nextInt();
            int rows = scanner.nextInt();
            pictureAttributes = new PictureAttribute[rows];
            int lineNumber=0;
            while (scanner.hasNext()){
                int [] values = new int[columns];
                double xCoordinate = Double.parseDouble(scanner.next());
                double yCoordinate = Double.parseDouble(scanner.next());
                scanner.next();
                scanner.next();
                scanner.next();
                for(int i=0;i<columns;i++){
                    values[i] = scanner.nextInt();
                }
                pictureAttributes[lineNumber] = new PictureAttribute(values,xCoordinate,yCoordinate,lineNumber);
                lineNumber++;
            }
        }
    }

}