package no.nav.kontantstotte.pdf;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import no.nav.kontantstotte.innsending.Soknad;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.*;

class React {

    private ThreadLocal<NashornScriptEngine> engineHolder = ThreadLocal.withInitial(() -> {
        NashornScriptEngine nashornScriptEngine = (NashornScriptEngine)
                new ScriptEngineManager().getEngineByName("nashorn");
        try {
            SimpleBindings bindings = new SimpleBindings();
            nashornScriptEngine.eval(read("static/polyfills/nashorn-polyfill.js"));
            nashornScriptEngine.eval(read("static/polyfills/babel.js"), bindings);
            nashornScriptEngine.eval(read("static/vendor-react/react.js"));
            nashornScriptEngine.eval(read("static/vendor-react/react-dom-server.js"));
            nashornScriptEngine.eval(read("static/vendor-react/react-dom.js"));

            evaluateReactComponents(nashornScriptEngine, bindings);
        } catch (ScriptException | IOException e) {
            throw new RuntimeException(e);
        }
        return nashornScriptEngine;
    });

    private void evaluateReactComponents(NashornScriptEngine nashorn, SimpleBindings bindings) throws ScriptException, IOException {
        nashorn = evaluerReactKomponent(nashorn, bindings, "jsx/SokerKrav.jsx");
        nashorn = evaluerReactKomponent(nashorn, bindings, "jsx/Barn.jsx");
        nashorn = evaluerReactKomponent(nashorn, bindings, "jsx/Barnehageplass.jsx");
        nashorn = evaluerReactKomponent(nashorn, bindings, "jsx/Familieforhold.jsx");
        nashorn = evaluerReactKomponent(nashorn, bindings, "jsx/Arbeidsforhold.jsx");
        nashorn = evaluerReactKomponent(nashorn, bindings, "jsx/OppsummeringsListeElement.jsx");
        nashorn = evaluerReactKomponent(nashorn, bindings, "jsx/PersonaliaOgBarnOppsummering.jsx");
        evaluerReactKomponent(nashorn, bindings, "jsx/SoknadPdf.jsx");
    }

    private NashornScriptEngine evaluerReactKomponent(NashornScriptEngine nashorn, SimpleBindings bindings, String filnavn) throws ScriptException, IOException {
        bindings.put("fil", readFromFile(filnavn));

        String transpilert = (String) nashorn
                .eval("Babel.transform(fil, { presets: ['react'] }).code", bindings);
        nashorn.eval(transpilert);
        return nashorn;
    }

    public String renderHTMLForPdf(Soknad soknad) {
        try {
            Object html = engineHolder.get().invokeFunction("hentHTMLStringForOppsummering", soknad);
            return String.valueOf(html);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new IllegalStateException("Klarte ikke rendre react-komponent", e);
        }
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
}
