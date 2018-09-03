package no.nav.kontantstotte.oppsummering;

import com.coveo.nashorn_modules.Require;
import com.coveo.nashorn_modules.ResourceFolder;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
            ResourceFolder resourceFolder = ResourceFolder.create(getClass().getClassLoader(), "react-pdf/", "UTF-8");
            Require.enable(nashornScriptEngine, resourceFolder);

            nashornScriptEngine.eval(read("static/polyfills/nashorn-polyfill.js"));
            //nashornScriptEngine.eval(read("dist/react.js"));
            //nashornScriptEngine.eval(read("static/vendor/react-dom-server.js"));
            //nashornScriptEngine.eval(read("static/vendor/react-dom.js"));

            evaluerPdfKomponenter(nashornScriptEngine);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        return nashornScriptEngine;
    }

    private void evaluerPdfKomponenter(NashornScriptEngine nashorn) throws ScriptException {

        /*nashorn.eval(read("jsx-transpilert/SokerKrav.jsx"));
        nashorn.eval(read("jsx-transpilert/Barn.jsx"));
        nashorn.eval(read("jsx-transpilert/Barnehageplass.jsx"));
        nashorn.eval(read("jsx-transpilert/Familieforhold.jsx"));
        nashorn.eval(read("dist/Arbeidsforhold.js"));
        nashorn.eval(read("dist/OppsummeringsListeElement.js"));
        nashorn.eval(read("jsx-transpilert/PersonaliaOgBarnOppsummering.jsx"));
        nashorn.eval(read("dist/SoknadPdf.js"));*/

        nashorn.eval(read("react-pdf/dist/bundle.js"));
    }

    private Reader read(String path) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(in);
    }

    public String renderHTMLForPdf(Soknad soknad) {
        try {
            Object html = engineHolder.invokeFunction("hentHTMLStringForOppsummering", soknad);
            return String.valueOf(html);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new IllegalStateException("Klarte ikke rendre react-komponent", e);
        }
    }
}
