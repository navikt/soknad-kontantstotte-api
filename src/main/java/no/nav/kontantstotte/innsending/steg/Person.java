package no.nav.kontantstotte.innsending.steg;

public class Person {
    public String fnr;
    public String navn;
    public String statsborgerskap;

    public Person() {
    }

    public Person(String fnr, String navn, String statsborgerskap) {
        this.fnr = fnr;
        this.navn = navn;
        this.statsborgerskap = statsborgerskap;
    }
}
