package nl.han.dea.http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlPageReader {
    public String readFile(String filename) throws IOException {
        var path = getPath(filename);

        var fileAsString = new String(Files.readAllBytes(path));

        return fileAsString;

    }

    public String getLength(String filename) {
        if (filename.isEmpty()) {
            return "0";
        }

        var path = getPath(filename);
        var length = path.toFile().length();

        return Long.toString(length);
    }

    private Path getPath(String filename) {
        var fullFileName = "pages/".concat(filename);
        var classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fullFileName).getFile()).toPath();
    }
}
