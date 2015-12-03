package scarvill.httpserver.routes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileResource {
    private Path filepath;

    public FileResource(Path filepath) {
        this.filepath = filepath;
    }

    public byte[] getData() {
        try {
            return Files.readAllBytes(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData(byte[] data) {
        try {
            Files.write(filepath, data, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
