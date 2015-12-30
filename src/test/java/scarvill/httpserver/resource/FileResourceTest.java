package scarvill.httpserver.resource;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FileResourceTest {

    @Test
    public void testReturnsFileData() throws IOException {
        byte[] fileContents = "foo".getBytes();

        FileResource resource = new FileResource(createTempFileWithContent(fileContents));

        assertThat(fileContents, equalTo(resource.getData()));
    }

    @Test
    public void testModifiesFileData() throws IOException {
        byte[] fileContents = "foo".getBytes();
        FileResource resource = new FileResource(createTempFileWithContent(fileContents));

        resource.setData(fileContents);

        assertThat(fileContents, equalTo(resource.getData()));
    }

    @Test
    public void testOverwritesExistingData() throws IOException {
        byte[] newFileContents = "baz".getBytes();
        FileResource resource = new FileResource(createTempFileWithContent("foo".getBytes()));

        resource.setData(newFileContents);

        assertThat(newFileContents, equalTo(resource.getData()));
    }

    @Test(expected = RuntimeException.class)
    public void testRaisesRuntimeExceptionIfEncountersIOExceptionWhenGettingData()
        throws IOException {
        Path file = createTempFileWithContent("foo".getBytes());
        FileResource resource = new FileResource(file);
        file.toFile().delete();

        resource.getData();
    }

    @Test(expected = RuntimeException.class)
    public void testRaisesRuntimeExceptionIfEncountersIOExceptionWhenSettingData()
        throws IOException {
        Path file = createTempFileWithContent("foo".getBytes());
        FileResource resource = new FileResource(file);
        file.toFile().setReadOnly();

        resource.setData("bar".getBytes());
    }

    private Path createTempFileWithContent(byte[] content) throws IOException {
        File file = File.createTempFile("temp", "");
        Files.write(file.toPath(), content);
        file.deleteOnExit();

        return file.toPath();
    }
}