package no.nav.kontantstotte.service;

public class PersonService extends ProxyService {

    public String hentPerson() {

        return proxyTarget()
                .path("person")
                .request()
                .header(key, proxyApiKey)
                .get(String.class);
    }
}
