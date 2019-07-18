package nl.han.dea.http;

import nl.han.dea.http.exceptions.ResourceNotAvailableException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class ConnectionHandler implements Runnable {

    private static final String KEY_CONTENT_LENGTH = "{{CONTENT_LENGTH}}";
    private static final String KEY_DATE = "{{DATE}}";
    private static final String KEY_HTTP_STATUS = "{{HTTP_STATUS}}";

    private static final String HTTP_STATUS_200 = "200 OK";
    private static final String HTTP_STATUS_404 = "404 NOT FOUND";

    private static final String HTTP_HEADER = "HTTP/1.1 " + KEY_HTTP_STATUS + "\n" +
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

            var startLine = parseRequest(inputStreamReader);

            if (startLine == null) { // Prevent a null value from creating a NullPointerException
                return;
            }

            var resource = startLine.split(" ")[1];
            writeResponse(outputStreamWriter, resource);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseRequest(BufferedReader inputStreamReader) throws IOException {
        var request = inputStreamReader.readLine();
        var startLine = request;

        while (request != null && !request.isEmpty()) {
            System.out.println(request);
            request = inputStreamReader.readLine();
        }

        return startLine;
    }

    private void writeResponse(BufferedWriter outputStreamWriter, String resource) {
        System.out.println("Writing response for resource: " + resource);
        try {
            writeHeader(outputStreamWriter, resource);
            writeBody(outputStreamWriter, resource);
            outputStreamWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHeader(BufferedWriter outputStreamWriter, String resource) throws IOException {
        outputStreamWriter.write(generateHeader(resource));
        outputStreamWriter.newLine();
    }

    private void writeBody(BufferedWriter outputStreamWriter, String resource) throws IOException {
        var file = "\n";

        try {
            file = new HtmlPageReader().readFile(resource);
        } catch (ResourceNotAvailableException e) {
            System.out.println(e.getMessage());
        }

        outputStreamWriter.write(file);
        outputStreamWriter.newLine();
    }

    private String generateHeader(String resource) {
        var length = "0";
        var status = HTTP_STATUS_200;

        try {
            length = new HtmlPageReader().getLength(resource);
        } catch (ResourceNotAvailableException e) {
            status = HTTP_STATUS_404;
        }

        return HTTP_HEADER
                .replace(KEY_CONTENT_LENGTH, length)
                .replace(KEY_DATE, OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .replace(KEY_HTTP_STATUS, status);
    }

    @Override
    public void run() {
        handle();
    }
}
