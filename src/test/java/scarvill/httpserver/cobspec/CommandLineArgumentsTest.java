package scarvill.httpserver.cobspec;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandLineArgumentsTest {
    @Test
    public void testParsesServerPort() throws Exception {
        String[] args = {"-p", "9999"};
        CommandLineArguments commandLineArguments = new CommandLineArguments(args);

        assertEquals(9999, commandLineArguments.getPort());
    }

    @Test
    public void testSetsDefaultPortWhenNotSpecified() throws Exception {
        String[] args = {};
        CommandLineArguments commandLineArguments = new CommandLineArguments(args);

        assertEquals(CommandLineArguments.DEFAULT_PORT, commandLineArguments.getPort());
    }

    @Test
    public void testParsesPublicDirectory() throws Exception {
        String[] args = {"-d", "/foo/bar"};
        CommandLineArguments commandLineArguments = new CommandLineArguments(args);

        assertEquals("/foo/bar", commandLineArguments.getPublicDirectory());
    }

    @Test
    public void testSetsDefaultPublicDirectoryWhenNotSpecified() throws Exception {
        String[] args = {};
        CommandLineArguments commandLineArguments = new CommandLineArguments(args);

        assertEquals(CommandLineArguments.DEFAULT_PUBLIC_DIRECTORY, commandLineArguments.getPublicDirectory());
    }

    @Test
    public void testParsesPortAndDirectoryTogether() throws Exception {
        String[] args = {"-p", "9999", "-d", "/foo/bar"};
        CommandLineArguments commandLineArguments = new CommandLineArguments(args);

        assertEquals(9999, commandLineArguments.getPort());
        assertEquals("/foo/bar", commandLineArguments.getPublicDirectory());
    }
}