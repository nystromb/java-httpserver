package scarvill.httpserver.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ServerTest {
    private ExecutorService threadPool;

    @Before
    public void setUp() throws Exception {
        threadPool = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testServerRuns() throws Exception {
        int port = 0;
        Server server = new Server(new TestServerConfiguration(port, new EchoService()));
        threadPool.submit(server::start);

        assertTrue(server.isRunning());

        server.stopServingNewConnections();
    }

    @Test
    public void testServerStops() throws Exception {
        int port = 0;
        Server server = new Server(new TestServerConfiguration(port, new EchoService()));
        threadPool.submit(server::start);
        server.stopServingNewConnections();

        assertFalse(server.isRunning());
    }

    @Test
    public void testServesConnectionAccordingToServiceInServerConfiguration() throws Exception {
        int port = 0;
        Server server = new Server(new TestServerConfiguration(port, new EchoService()));
        threadPool.submit(server::start);
        String echoMessage = "foo";

        assertEquals(echoMessage, getServerReplyTo(echoMessage, server));

        server.stopServingNewConnections();
    }

    @After
    public void tearDown() throws Exception {
        threadPool.shutdown();
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

    private class TestServerConfiguration implements ServerConfiguration {
        private int port;
        private Serveable service;

        public TestServerConfiguration(int port, Serveable service) {
            this.port = port;
            this.service = service;
        }

        @Override
        public int getPort() {
            return port;
        }

        @Override
        public String getPublicDirectory() {
            return null;
        }

        @Override
        public Serveable getService() {
            return service;
        }
    }

    private class EchoService implements Serveable {

        public Runnable serve(Socket clientSocket) {
            return () -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    out.println(in.readLine());
                } catch (IOException ignored) {
                }
            };
        }
    }
}