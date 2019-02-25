package no.nav.kontantstotte.tekst;

import java.util.Map;

public class TekstService {

    private static TekstProvider teksterProvider;

    private static TekstProvider landProvider;

    private static final String DEFAULT_TEKSTER_BUNDLE = "tekster";

    private static final String DEFAULT_LAND_BUNDLE = "land";

    public static final String[] DEFAULT_VALID_LANGUAGES = { "nb", "nn"};

    public TekstService(){
        this.teksterProvider = new TekstProvider(DEFAULT_TEKSTER_BUNDLE, DEFAULT_VALID_LANGUAGES);
        this.landProvider = new TekstProvider(DEFAULT_LAND_BUNDLE, DEFAULT_VALID_LANGUAGES);
    }

    public TekstService(String teksterBundle, String landBundle, String sprak){
        this.teksterProvider = new TekstProvider(teksterBundle, sprak);
        this.landProvider = new TekstProvider(landBundle, sprak);
    }

    public Map<String, String> hentTekster(String sprak) {
        return teksterProvider.hentTekster(sprak);
    }

    public Map<String, String> hentLand(String sprak) {
        return landProvider.hentTekster(sprak);
    }

}
