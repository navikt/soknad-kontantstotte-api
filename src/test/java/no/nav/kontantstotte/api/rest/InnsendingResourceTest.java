package no.nav.kontantstotte.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.oppsummering.InnsendingService;
import no.nav.kontantstotte.oppsummering.Soknad;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class InnsendingResourceTest extends JerseyTest {

    private InnsendingService innsendingService = mock(InnsendingService.class);

    @Test
    public void at_innsending_av_soknad_er_ok_med_bekreftelse() throws JsonProcessingException {
        when(innsendingService.sendInnSoknad(any(Soknad.class)))
                .thenReturn(Response.ok().build());

        Soknad soknadMedBekreftelse = new Soknad();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";

        Response response = target()
                .path("sendinn")
                .request()
                .post(Entity.entity(multipart(soknadMedBekreftelse), MULTIPART_FORM_DATA_TYPE));

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    }

    @Test
    public void at_innsending_av_soknad_er_gir_400_uten_bekreftelse() throws JsonProcessingException {

        Response response = target()
                .path("sendinn")
                .request()
                .post(Entity.entity(multipart(new Soknad()), MULTIPART_FORM_DATA_TYPE));

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());

        verifyNoMoreInteractions(innsendingService);
    }

    private MultiPart multipart(Soknad soknad) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return new FormDataMultiPart()
                .field("soknad", objectMapper.writeValueAsString(soknad), APPLICATION_JSON_TYPE)
                .bodyPart(Entity.json(objectMapper.writeValueAsString(soknad)), APPLICATION_JSON_TYPE);
    }

    @Override
    protected Application configure() {

        StaticApplicationContext staticApplicationContext = new StaticApplicationContext();
        staticApplicationContext.registerBean(InnsendingResource.class, () -> new InnsendingResource(innsendingService));

        forceSet(TestProperties.CONTAINER_PORT, "0"); // random port

        return new ResourceConfig()
                .register(InnsendingResource.class)
                .register(MultiPartFeature.class)
                .property("contextConfig", staticApplicationContext); // Since spring/jersey integration is on CP, we need a dummy appctx
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);

    }
}
