package no.nav.kontantstotte.service;

public class PersonService extends ProxyService {

    public String hentPerson() {

        String response = proxyTarget()
                .path("person")
                .request()
                .header(key, proxyApiKey)
                .get(String.class);
        return response;
    }
}
