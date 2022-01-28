package no.nav.kontantstotte.innsyn.pdl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
public class PDLConfig {

    private static final Logger logger = LoggerFactory.getLogger(PDLConfig.class);

    private URI pdlUrl;

    public static final String PATH_GRAPHQL = "graphql";



    @Autowired
    public PDLConfig(@Value("${PDL_URL}") URI pdlUrl) {
        pdlUrl = UriComponentsBuilder.fromUri(pdlUrl).path(PATH_GRAPHQL).build().toUri();
    }



}
