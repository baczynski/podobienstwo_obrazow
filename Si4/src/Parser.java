/**
 * Created by konrad on 22.05.16.
 */
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Parser {

    private Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.US_ASCII;
    private int [][] values;

    public int [][] getValues(String aFileName){
        fFilePath = Paths.get(aFileName);
        try {
            processLineByLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }


    public final void processLineByLine() throws IOException {
        try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
            int columns = scanner.nextInt();
            int rows = scanner.nextInt();
            values = new int[rows][columns];
            int lineNumber=0;
            while (scanner.hasNext()){
                scanner.next();
                scanner.next();
                scanner.next();
                scanner.next();
                scanner.next();
                for(int i=0;i<columns;i++){
                    values[lineNumber][i] = scanner.nextInt();
                }
                lineNumber++;
            }
        }
    }

}