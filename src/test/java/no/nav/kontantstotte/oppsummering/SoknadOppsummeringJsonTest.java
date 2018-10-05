package no.nav.kontantstotte.oppsummering.innsending.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.innsending.v2.mapping.SoknadOppsummering;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.StaticApplicationContext;

import javax.ws.rs.core.Application;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoknadOppsummeringJsonTest extends JerseyTest {

    public static final Logger LOG = LoggerFactory.getLogger(SoknadOppsummeringJsonTest.class);

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private HtmlOppsummeringService htmlOppsummeringService;
    private NodeOppsummeringGenerator nodeOppsummeringGenerator;

    @Test
    public void enkelt_soknadsjson_gir_forventet_oppsummeringsobjekt() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Soknad enkelSoknad = mapper.readValue(new File(getClass().getClassLoader().getResource("soknad.json").getFile()), Soknad.class);
        enkelSoknad.innsendingsTidspunkt = Instant.now();
        SoknadOppsummering oppsummering = mapper.readValue(new File(getClass().getClassLoader().getResource("soknad-oppsummering.json").getFile()), SoknadOppsummering.class);

        ArgumentCaptor<SoknadOppsummering> captor = ArgumentCaptor.forClass(SoknadOppsummering.class);
        when(htmlOppsummeringService.genererHtml(captor.capture())).thenReturn(new byte[1]);
        nodeOppsummeringGenerator.genererOppsummering(enkelSoknad);

         assertThat(captor.getValue()).isEqualToComparingFieldByFieldRecursively(oppsummering);

    }

    @Override
    protected Application configure() {
        htmlOppsummeringService = mock(HtmlOppsummeringService.class);
        nodeOppsummeringGenerator = new NodeOppsummeringGenerator(
                htmlOppsummeringService,
                mock(PdfGenService.class));

        StaticApplicationContext staticApplicationContext = new StaticApplicationContext();
        staticApplicationContext.registerBean(NodeOppsummeringGenerator.class, () -> nodeOppsummeringGenerator );

        forceSet(TestProperties.CONTAINER_PORT, "0"); // random port
        enable(TestProperties.DUMP_ENTITY);
        enable(TestProperties.LOG_TRAFFIC);

        return new ResourceConfig()
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