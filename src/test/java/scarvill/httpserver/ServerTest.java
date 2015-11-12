package scarvill.httpserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

public class ServerTest {
    private ExecutorService threadPool;

    @Before
    public void setUp() throws Exception {
        threadPool = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testServerRunsThenStops() throws Exception {
        Server server = new Server(0, echoService());
        threadPool.submit(server::start);

        assertTrue(server.isRunning());

        server.stop();

        assertFalse(server.isRunning());
    }

    @Test
    public void testServesConnectionAccordingToInjectedService() throws Exception {
        Server server = new Server(0, echoService());
        threadPool.submit(server::start);
        String echoMessage = "foo";

        assertEquals(echoMessage, getServerReplyTo(echoMessage, server));

        server.stop();
    }

    @After
    public void tearDown() throws Exception {
        threadPool.shutdown();
    }

    private static BiConsumer<InputStream, OutputStream> echoService() {
        return (inputStream, outputStream) -> {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter out = new PrintWriter(outputStream, true);

            try { out.println(in.readLine()); }
            catch (IOException e) { throw new RuntimeException(e); }
        };
    }

    private String getServerReplyTo(String message, Server server) throws IOException {
        Socket clientSocket =
            new Socket(server.getInetAddress(), server.getLocalPort());
        PrintWriter out =
            new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in =
            new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        out.println(message);
        return in.readLine();
    }
}