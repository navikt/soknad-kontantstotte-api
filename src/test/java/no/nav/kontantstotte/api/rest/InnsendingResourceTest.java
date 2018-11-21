package no.nav.kontantstotte.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.api.rest.dto.InnsendingsResponsDto;
import no.nav.kontantstotte.api.rest.dto.SoknadDto;
import no.nav.kontantstotte.innsending.InnsendingService;
import no.nav.kontantstotte.innsending.Soknad;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.StaticApplicationContext;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InnsendingResourceTest extends JerseyTest {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private InnsendingService innsendingService = mock(InnsendingService.class);

    @Test
    public void at_innsending_av_soknad_er_ok_med_bekreftelse() throws JsonProcessingException {
        SoknadDto soknadMedBekreftelse = new SoknadDto();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";

        when(innsendingService.sendInnSoknad(any(Soknad.class)))
                .then(returnsFirstArg());

        Response response = target()
                .path("sendinn")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .post(Entity.entity(multipart(soknadMedBekreftelse), MULTIPART_FORM_DATA_TYPE));

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    }

    @Test
    public void at_soknad_markeres_med_innsendingstidspunkt() throws JsonProcessingException {
        SoknadDto soknadMedBekreftelse = new SoknadDto();
        soknadMedBekreftelse.oppsummering.bekreftelse = "JA";

        when(innsendingService.sendInnSoknad(any(Soknad.class)))
                .thenAnswer(returnsFirstArg());

        target().path("sendinn")
                .request()
                .post(Entity.entity(multipart(soknadMedBekreftelse), MULTIPART_FORM_DATA_TYPE));

        ArgumentCaptor<Soknad> captor = ArgumentCaptor.forClass(Soknad.class);
        verify(innsendingService).sendInnSoknad(captor.capture());

        assertThat(captor.getValue().getInnsendingsTidspunkt()).isBefore(now());
        assertThat(captor.getValue().getInnsendingsTidspunkt()).isAfter(now().minus(5, MINUTES));


    }

    @Test
    public void at_innsending_av_soknad_er_gir_400_ved_manglende_bekreftelse() throws JsonProcessingException {

        Response response = target()
                .path("sendinn")
                .request()
                .post(Entity.entity(multipart(new SoknadDto()), MULTIPART_FORM_DATA_TYPE));

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());

        verifyNoMoreInteractions(innsendingService);
    }

    private MultiPart multipart(SoknadDto soknad) throws JsonProcessingException {
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
        enable(TestProperties.DUMP_ENTITY);
        enable(TestProperties.LOG_TRAFFIC);

        return new ResourceConfig()
                .register(InnsendingResource.class)
                .register(MultiPartFeature.class)
                .register(LoggingFeature.class)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, "INFO")
                .property("contextConfig", staticApplicationContext); // Since spring/jersey integration is on CP, we need a dummy appctx
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, "INFO")
                .register(LoggingFeature.class);

    }
}
