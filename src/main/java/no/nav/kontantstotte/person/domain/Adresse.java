package no.nav.kontantstotte.person.domain;

public class Adresse {

    private final String adresse;
    private final String adressetillegg;
    private final String bydel;
    private final String kommune;
    private final String landkode;
    private final String postnummer;

    private Adresse(Builder builder) {
        this.adresse = builder.adresse;
        this.adressetillegg = builder.adressetillegg;
        this.bydel = builder.bydel;
        this.kommune = builder.kommune;
        this.landkode = builder.landkode;
        this.postnummer = builder.postnummer;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getAdressetillegg() {
        return adressetillegg;
    }

    public String getBydel() {
        return bydel;
    }

    public String getKommune() {
        return kommune;
    }

    public String getLandkode() {
        return landkode;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public static class Builder {

        private String adresse;
        private String adressetillegg;
        private String bydel;
        private String kommune;
        private String landkode;
        private String postnummer;
        
        public Builder adresse(String adresse) {
            this.adresse = adresse;
            return this;
        }

        public Builder adressetillegg(String adressetillegg) {
            this.adressetillegg = adressetillegg;
            return this;
        }

        public Builder bydel(String bydel) {
            this.bydel = bydel;
            return this;
        }

        public Builder kommune(String kommune) {
            this.kommune = kommune;
            return this;
        }

        public Builder landkode(String landkode) {
            this.landkode = landkode;
            return this;
        }

        public Builder postnummer(String postnummer) {
            this.postnummer = postnummer;
            return this;
        }

        public Adresse build() {
            return new Adresse(this);
        }
    }


}
