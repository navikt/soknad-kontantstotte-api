package no.nav.kontantstotte.person.domain;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person {
    private final String fornavn;
    private final String mellomnavn;
    private final String slektsnavn;

    public Person(Builder builder) {

        this.fornavn = builder.fornavn;
        this.mellomnavn = builder.mellomnavn;
        this.slektsnavn = builder.slektsnavn;

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

    public String getNavn() {

        return Stream.of(fornavn, mellomnavn, slektsnavn)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public static class Builder {
        private String fornavn;
        private String mellomnavn;
        private String slektsnavn;

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

        public Person build() {
            return new Person(this);
        }
    }
}