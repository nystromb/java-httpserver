package scarvill.httpserver.routes;

import org.junit.Test;
import scarvill.httpserver.routes.Resource;

import static org.junit.Assert.assertEquals;

public class ResourceTest {

    @Test
    public void testResourceHasData() throws Exception {
        Resource resource = new Resource("foo");

        assertEquals("foo", resource.getData());
    }
}