package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.config.RestConfiguration;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Application;

public class StatusResourceTest extends JerseyTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();

            //        @Override
//        protected void configureClient(ClientConfig config) {
//
//            super.configureClient(config);
//        }
    }

    @Override
    protected Application configure() {
        return new RestConfiguration();
    }

    @Test
    public void testStatus() throws Exception {
        setUp();

        target("/status/ping");
    }

}
