package no.nav.kontantstotte.oppsummering.innsending.v1;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.tekst.Utf8Control;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.*;
import java.util.function.Function;

import static java.util.ResourceBundle.getBundle;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

class OppsummeringTransformer {

    private NashornScriptEngine engineHolder;

    OppsummeringTransformer() {
        this.engineHolder = getandInitializeEngineHolder();
    }

    private NashornScriptEngine getandInitializeEngineHolder() {
        NashornScriptEngine nashornScriptEngine = (NashornScriptEngine)
                new ScriptEngineManager().getEngineByName("nashorn");
        try {
            nashornScriptEngine.eval(read("static/polyfills/nashorn-polyfill.js"));
            nashornScriptEngine.eval(read("react-pdf/node_modules/react/umd/react.production.min.js"));
            nashornScriptEngine.eval(read("react-pdf/node_modules/react-dom/umd/react-dom-server.browser.production.min.js"));

            evaluerPdfKomponenter(nashornScriptEngine);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        return nashornScriptEngine;
    }

    private void evaluerPdfKomponenter(NashornScriptEngine nashorn) throws ScriptException {
        nashorn.eval(read("react-pdf/dist/bundle.js"));
    }

    private Reader read(String path) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(in);
    }

    String renderHTMLForPdf(Soknad soknad) {
        try {
            Function<ResourceBundle, Map<String, String>> bundleToMap = bundle -> bundle.keySet().stream()
                    .collect(toMap(identity(), bundle::getString));

            ResourceBundle teksterBundle = getBundle("tekster", new Locale(soknad.sprak), new Utf8Control());
            Map<String,String> teksterMap = bundleToMap.apply(teksterBundle);

            Object html = engineHolder.invokeFunction("hentHTMLStringForOppsummering", soknad, teksterMap);
            return String.valueOf(html);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new IllegalStateException("Klarte ikke rendre react-komponent", e);
        }
    }
}
