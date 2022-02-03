package no.nav.kontantstotte.innsyn.domain;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person {
    private final String fornavn;
    private final String mellomnavn;
    private final String etternavn;
    private final String statsborgerskap;

    public Person(Builder builder) {

        this.fornavn = builder.fornavn;
        this.mellomnavn = builder.mellomnavn;
        this.etternavn = builder.etternavn;
        this.statsborgerskap = builder.statsborgerskap;

    }

    public String getFornavn() {
        return fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getFulltnavn() {

        return Stream.of(fornavn, mellomnavn, etternavn)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public String getStatsborgerskap() { return statsborgerskap; }

    public static class Builder {
        private String fornavn;
        private String mellomnavn;
        private String etternavn;
        private String statsborgerskap;

        public Builder fornavn(String fornavn) {
            this.fornavn = fornavn;
            return this;
        }

        public Builder mellomnavn(String mellomnavn) {
            this.mellomnavn = mellomnavn;
            return this;
        }

        public Builder etternavn(String etternavn) {
            this.etternavn = etternavn;
            return this;
        }

        public  Builder statsborgerskap(String statsborgerskap) {
            this.statsborgerskap = statsborgerskap;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }
}
