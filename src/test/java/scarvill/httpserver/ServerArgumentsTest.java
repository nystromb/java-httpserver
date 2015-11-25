package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerArgumentsTest {
    @Test
    public void testParsesServerPort() throws Exception {
        String[] args = {"-p", "9999"};
        ServerArguments serverArguments = new ServerArguments(args);

        assertEquals(9999, serverArguments.port);
    }

    @Test
    public void testSetsDefaultPortWhenNotSpecified() throws Exception {
        String[] args = {};
        ServerArguments serverArguments = new ServerArguments(args);

        assertEquals(ServerArguments.DEFAULT_PORT, serverArguments.port);
    }

    @Test
    public void testParsesPublicDirectory() throws Exception {
        String[] args = {"-d", "/foo/bar"};
        ServerArguments serverArguments = new ServerArguments(args);

        assertEquals("/foo/bar", serverArguments.publicDirectory);
    }

    @Test
    public void testSetsDefaultPublicDirectoryWhenNotSpecified() throws Exception {
        String[] args = {};
        ServerArguments serverArguments = new ServerArguments(args);

        assertEquals(ServerArguments.DEFAULT_PUBLIC_DIRECTORY, serverArguments.publicDirectory);
    }

    @Test
    public void testParsesPortAndDirectoryTogether() throws Exception {
        String[] args = {"-p", "9999", "-d", "/foo/bar"};
        ServerArguments serverArguments = new ServerArguments(args);

        assertEquals(9999, serverArguments.port);
        assertEquals("/foo/bar", serverArguments.publicDirectory);
    }
}