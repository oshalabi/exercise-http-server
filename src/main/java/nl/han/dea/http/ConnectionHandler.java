package nl.han.dea.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class ConnectionHandler implements Runnable {

    private static final String KEY_CONTENT_LENGTH = "{{CONTENT_LENGTH}}";
    private static final String KEY_DATE = "{{DATE}}";

    private static final String HTTP_HEADER = "HTTP/1.1 200 OK\n" +
            "Date: " + KEY_DATE + "\n" +
            "HttpServer: Simple DEA Webserver\n" +
            "Content-Length: " + KEY_CONTENT_LENGTH + "\n" +
            "Content-Type: text/html\n";

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        handle();
    }

    public void handle() {
        try {
            var inputStreamReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
            var outputStreamWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));

            parseRequest(inputStreamReader);
            writeResponse(outputStreamWriter);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseRequest(BufferedReader inputStreamReader) throws IOException {
        var request = inputStreamReader.readLine();

        while (request != null && !request.isEmpty()) {
            System.out.println(request);
            request = inputStreamReader.readLine();
        }
    }

    private void writeResponse(BufferedWriter outputStreamWriter) {
        try {
            outputStreamWriter.write(generateHeader());
            outputStreamWriter.newLine();
            outputStreamWriter.write(new HtmlPageReader().readFile("index.html"));
            outputStreamWriter.newLine();
            outputStreamWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateHeader() {
        return HTTP_HEADER
                .replace(KEY_CONTENT_LENGTH, new HtmlPageReader().getLength("index.html"))
                .replace(KEY_DATE, OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }

    @Override
    public void run() {
        handle();
    }
}
