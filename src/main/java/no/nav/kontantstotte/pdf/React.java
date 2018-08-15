package no.nav.kontantstotte.pdf;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class React {

    private ThreadLocal<NashornScriptEngine> engineHolder = new ThreadLocal<NashornScriptEngine>() {
        @Override
        protected NashornScriptEngine initialValue () {
            NashornScriptEngine nashornScriptEngine = (NashornScriptEngine)
                    new ScriptEngineManager().getEngineByName("nashorn");
            try {
                nashornScriptEngine.eval(read("nashorn-polyfill.js"));
                nashornScriptEngine.eval(read("SoknadPdf.jsx"));
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
            return nashornScriptEngine;
        }
    };

    public String renderHTMLForPdf() {
        try {
            Object html = engineHolder.get().invokeFunction("hentHTMLStringForOppsummering");
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("Klarte ikke rendre react-komponent", e);
        }
    }

    private Reader read(String path) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(in);
    }
}
