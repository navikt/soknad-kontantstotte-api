package no.nav.kontantstotte.innsyn.domain;

public class Barn {
    private final String fødselsnummer;
    private final String fulltnavn;
    private final String fødselsdato;

    public Barn(Builder builder) {
        this.fødselsnummer = builder.fødselsnummer;
        this.fulltnavn = builder.fulltnavn;
        this.fødselsdato = builder.fødselsdato;
    }

    public String getFødselsnummer() {
        return fødselsnummer;
    }

    public String getFulltnavn() {
        return fulltnavn;
    }

    public String getFødselsdato() {
        return fødselsdato;
    }


    public static class Builder {
        private String fødselsnummer;
        private String fulltnavn;
        private String fødselsdato;

        public Builder fødselsnummer(String fødselsnummer) {
            this.fødselsnummer = fødselsnummer;
            return this;
        }

        public Builder fulltnavn(String fulltnavn) {
            this.fulltnavn = fulltnavn;
            return this;
        }

        public Builder fødselsdato(String fødselsdato) {
            this.fødselsdato = fødselsdato;
            return this;
        }

        public Barn build() {
            return new Barn(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("class Barn {\n");
        sb.append("    fødselsnummer: ").append(toIndentedString(fødselsnummer)).append("\n");
        sb.append("    fulltnavn: ").append(toIndentedString(fulltnavn)).append("\n");
        sb.append("    fødselsdato: ").append(toIndentedString(fødselsdato)).append("\n");
        sb.append("}");

        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
