package no.nav.kontantstotte.innsyn.domain;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person {
    private final String fornavn;
    private final String mellomnavn;
    private final String slektsnavn;
    private final String statsborgerskap;

    public Person(Builder builder) {

        this.fornavn = builder.fornavn;
        this.mellomnavn = builder.mellomnavn;
        this.slektsnavn = builder.slektsnavn;
        this.statsborgerskap = builder.statsborgerskap;

    }

    public String getFornavn() {
        return fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public String getSlektsnavn() {
        return slektsnavn;
    }

    public String getFulltnavn() {

        return Stream.of(fornavn, mellomnavn, slektsnavn)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public String getStatsborgerskap() { return statsborgerskap; }

    public static class Builder {
        private String fornavn;
        private String mellomnavn;
        private String slektsnavn;
        private String statsborgerskap;

        public Builder fornavn(String fornavn) {
            this.fornavn = fornavn;
            return this;
        }

        public Builder mellomnavn(String mellomnavn) {
            this.mellomnavn = mellomnavn;
            return this;
        }

        public Builder slektsnavn(String slektsnavn) {
            this.slektsnavn = slektsnavn;
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
