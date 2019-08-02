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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("class Barn {\n");
        sb.append("    fulltnavn: ").append(toIndentedString(fulltnavn)).append("\n");
        sb.append("    fodselsdato: ").append(toIndentedString(fodselsdato)).append("\n");
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
