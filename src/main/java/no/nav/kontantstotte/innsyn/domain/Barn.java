package no.nav.kontantstotte.innsyn.domain;

public class Barn {
    private final String fulltnavn;
    private final String fodselsdato;

    public Barn(Builder builder) {

        this.fulltnavn = builder.fulltnavn;
        this.fodselsdato = builder.fodselsdato;
    }

    public String getFulltnavn() {
        return fulltnavn;
    }

    public String getFodselsdato() {
        return fodselsdato;
    }

    public static class Builder {
        private String fulltnavn;
        private String fodselsdato;

        public Builder fulltnavn(String fulltnavn) {
            this.fulltnavn = fulltnavn;
            return this;
        }

        public Builder fodselsdato(String fodselsdato) {
            this.fodselsdato = fodselsdato;
            return this;
        }


        public Barn build() {
            return new Barn(this);
        }
    }
}
