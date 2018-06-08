package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.api.config.RestConfiguration;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.Application;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationContext.class)
public class StatusResourceTest {


    JerseyTest test = new JerseyTest() {

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
        protected void configureClient(ClientConfig config) {
            super.configureClient(config);
        }

        @Override
        protected Application configure() {
            return new RestConfiguration();
        }
    };


    @Test
    public void testStatus() throws Exception {

        test.target("/status/ping");
    }

}
