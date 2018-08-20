package no.nav.kontantstotte.pdf;

import jdk.nashorn.api.scripting.NashornScriptEngine;

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
            nashornScriptEngine.eval(read("static/nashorn-polyfill.js"));
            nashornScriptEngine.eval(read("static/babel.js"), bindings);
            nashornScriptEngine.eval(read("static/react.js"));
            nashornScriptEngine.eval(read("static/react-dom-server.js"));
            nashornScriptEngine.eval(read("static/react-dom.js"));

            String utranspilert = readFromFile("jsx/SoknadPdf.js");
            bindings.put("inputFil", utranspilert);
            String transpilert = (String) nashornScriptEngine.eval("Babel.transform(inputFil, { presets: ['react'] }).code", bindings);
            nashornScriptEngine.eval(transpilert);
        } catch (ScriptException | IOException e) {
            throw new RuntimeException(e);
        }
        return nashornScriptEngine;
    });

    public String renderHTMLForPdf() {
        try {
            Object html = engineHolder.get().invokeFunction("hentHTMLStringForOppsummering");
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
