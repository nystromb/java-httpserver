package scarvill.httpserver.resource;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class StringResourceTest {

    @Test
    public void testResourceHasData() throws Exception {
        Resource resource = new StringResource("foo");

        assertThat("foo".getBytes(), equalTo(resource.getData()));
    }

    @Test
    public void testSetResourceData() {
        Resource resource = new StringResource("foo");
        resource.setData("bar".getBytes());

        assertThat("bar".getBytes(), equalTo(resource.getData()));
    }
}