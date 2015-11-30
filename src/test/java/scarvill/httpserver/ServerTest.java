package scarvill.httpserver;

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
    public void testServerRunsThenStops() throws Exception {
        Server server = new Server(0, new EchoService());
        threadPool.submit(server::start);

        assertTrue(server.isRunning());

        server.stopServingNewConnections();

        assertFalse(server.isRunning());
    }

    @Test
    public void testServesConnectionAccordingToInjectedService() throws Exception {
        Server server = new Server(0, new EchoService());
        threadPool.submit(server::start);
        String echoMessage = "foo";

        assertEquals(echoMessage, getServerReplyTo(echoMessage, server));

        server.stopServingNewConnections();
    }

    @After
    public void tearDown() throws Exception {
        threadPool.shutdown();
    }

    private class EchoService implements Serveable {
        private Socket clientSocket;

        public EchoService serve(Socket clientSocket) {
            this.clientSocket = clientSocket;
            return this;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.println(in.readLine());
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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