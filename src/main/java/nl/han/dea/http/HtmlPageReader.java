package nl.han.dea.http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HtmlPageReader {

    public String readFile(String filename) {
        var fullFileName = "pages/".concat(filename);
        try {
            var fileAsString = "";
            ClassLoader classLoader = getClass().getClassLoader();
                System.out.println(fullFileName);
                try {
                    var file = new File(classLoader.getResource(fullFileName).getFile()).toPath();

                     fileAsString = new String(Files.readAllBytes(file));
                    //TODO
                } catch (NullPointerException e){
                    return  "";
                }



            return fileAsString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}