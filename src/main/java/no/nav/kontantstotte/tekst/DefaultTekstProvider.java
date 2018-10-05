package no.nav.kontantstotte.tekst;

public class DefaultTekstProvider extends TekstProvider {

    private static final String DEFAULT_BUNDLE_NAME = "tekster";

    private static final String[] DEFAULT_VALID_LANGUAGES = { "nb" };


    public DefaultTekstProvider(){
        super(DEFAULT_BUNDLE_NAME, DEFAULT_VALID_LANGUAGES);
    }

}
