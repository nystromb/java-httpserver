package scarvill.httpserver.routing.resource;

import org.junit.Test;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.resource.StringResource;

import static org.junit.Assert.assertEquals;

public class StringResourceTest {

    @Test
    public void testResourceHasData() throws Exception {
        Resource resource = new StringResource("foo");

        assertEquals("foo", new String(resource.getData()));
    }

    @Test
    public void testSetResourceData() {
        Resource resource = new StringResource("foo");
        resource.setData("bar".getBytes());

        assertEquals("bar", new String(resource.getData()));
    }
}