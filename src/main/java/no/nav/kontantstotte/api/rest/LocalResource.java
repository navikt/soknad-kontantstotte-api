package no.nav.kontantstotte.api.rest;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.SignedJWT;
import no.nav.security.oidc.api.Unprotected;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
@Path("local")
public class LocalResource {


    @Unprotected
    @GET
    public TokenEndpoint[] endpoints(HttpServletRequest request) {
        String base = request.getRequestURL().toString();
        return new TokenEndpoint[]{new TokenEndpoint("Get JWT as serialized string", base + "/jwt", "subject"),
                new TokenEndpoint("Get JWT as SignedJWT object with claims", base + "/claims", "subject"),
                new TokenEndpoint("Add JWT as a cookie, (optional) redirect to secured uri", base + "/cookie", "subject", "redirect", "cookiename"),
                new TokenEndpoint("Get JWKS used to sign token", base + "/jwks"),
                new TokenEndpoint("Get JWKS used to sign token as JWKSet object", base + "/jwkset"),
                new TokenEndpoint("Get token issuer metadata (ref oidc .well-known)", base + "/metadata")};
    }



    @Unprotected
    @Path("cookie")
    @GET
    public Cookie addCookie(@QueryParam("subject")@DefaultValue("12345678910") String subject,
                            @QueryParam("cookiename") @DefaultValue("localhost-idtoken") String cookieName,
                            @QueryParam("redirect") String redirect,
                            @Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {

        SignedJWT token = JwtTokenGenerator.createSignedJWT(subject);
        Cookie cookie = new Cookie(cookieName, token.serialize());
        cookie.setDomain("localhost");
        cookie.setPath("/");
        response.addCookie(cookie);
        if (redirect != null) {
            response.sendRedirect(redirect);
            return null;
        }
        return cookie;
    }

    @Unprotected
    @GET
    @Path("/jwks")
    public String jwks() throws IOException {
        return IOUtils.readInputStreamToString(getClass().getResourceAsStream(JwkGenerator.DEFAULT_JWKSET_FILE),
                Charset.defaultCharset());
    }

    @Unprotected
    @GET
    @Path("jwkset")
    public JWKSet jwkSet() {
        return JwkGenerator.getJWKSet();
    }

    @Unprotected
    @GET
    @Path("/metadata")
    public String metadata() throws IOException {
        return IOUtils.readInputStreamToString(getClass().getResourceAsStream("/metadata.json"),
                Charset.defaultCharset());
    }



    class TokenEndpoint {
        String desc;
        String uri;
        String[] params;

        public TokenEndpoint(String desc, String uri, String... params) {
            this.desc = desc;
            this.uri = uri;
            this.params = params;

        }

        public String getDesc() {
            return desc;
        }

        public String getUri() {
            return uri;
        }

        public String[] getParams() {
            return params;
        }
    }
}
