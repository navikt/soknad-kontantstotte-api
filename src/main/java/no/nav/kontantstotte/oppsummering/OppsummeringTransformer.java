package no.nav.kontantstotte.oppsummering;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.*;

public class OppsummeringTransformer {

    private NashornScriptEngine engineHolder;

    public OppsummeringTransformer() {
        this.engineHolder = getandInitializeEngineHolder();
    }

    private NashornScriptEngine getandInitializeEngineHolder() {
        NashornScriptEngine nashornScriptEngine = (NashornScriptEngine)
                new ScriptEngineManager().getEngineByName("nashorn");
        try {
            SimpleBindings bindings = new SimpleBindings();
            nashornScriptEngine.eval(read("static/polyfills/nashorn-polyfill.js"));
            //nashornScriptEngine.eval(read("static/vendor/babel.js"), bindings);
            nashornScriptEngine.eval(read("static/vendor/react.js"));
            nashornScriptEngine.eval(read("static/vendor/react-dom-server.js"));
            nashornScriptEngine.eval(read("static/vendor/react-dom.js"));

            evaluerPdfKomponenter(nashornScriptEngine, bindings);
        } catch (ScriptException | IOException e) {
            throw new RuntimeException(e);
        }
        return nashornScriptEngine;
    }

    private void evaluerPdfKomponenter(NashornScriptEngine nashorn, SimpleBindings bindings) throws ScriptException, IOException {

        nashorn.eval(read("jsx-transpilert/SokerKrav.jsx"));
        nashorn.eval(read("jsx-transpilert/Barn.jsx"));
        nashorn.eval(read("jsx-transpilert/Barnehageplass.jsx"));
        nashorn.eval(read("jsx-transpilert/Familieforhold.jsx"));
        nashorn.eval(read("jsx-transpilert/Arbeidsforhold.jsx"));
        nashorn.eval(read("jsx-transpilert/OppsummeringsListeElement.jsx"));
        nashorn.eval(read("jsx-transpilert/PersonaliaOgBarnOppsummering.jsx"));
        nashorn.eval(read("jsx-transpilert/SoknadPdf.jsx"));

        /*
        evaluerReactKomponent(nashorn, bindings, "jsx/SokerKrav.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/Barn.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/Barnehageplass.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/Familieforhold.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/Arbeidsforhold.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/OppsummeringsListeElement.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/PersonaliaOgBarnOppsummering.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/SoknadPdf.jsx");
        */
    }

    private void evaluerReactKomponent(NashornScriptEngine nashorn, SimpleBindings bindings, String filnavn) throws ScriptException, IOException {
        bindings.put("fil", readFromFile(filnavn));

        String transpilert = (String) nashorn
                .eval("Babel.transform(fil, { presets: ['react'] }).code", bindings);
        nashorn.eval(transpilert);
    }

    private Reader read(String path) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(in);
    }

    private String readFromFile(String path) throws IOException {
        Reader reader = read(path);
        int intValueOfChar;
        String targetString = "";
        while ((intValueOfChar = reader.read()) != -1) {
            targetString += (char) intValueOfChar;
        }
        reader.close();
        return targetString;
    }

    public String renderHTMLForPdf(Soknad soknad) {
        try {
            Object html = engineHolder.invokeFunction("hentHTMLStringForOppsummering", soknad);
            return String.valueOf(html);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new IllegalStateException("Klarte ikke rendre react-komponent", e);
        }
    }

    private void skrivTilFil(String transpilert, String filnavn) {
        try {
            PrintWriter out = new PrintWriter("/Users/martineenger/nav/soknad-kontantstotte-api/src/main/resources/jsx-transpilert" + filnavn);
            out.write(transpilert);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
