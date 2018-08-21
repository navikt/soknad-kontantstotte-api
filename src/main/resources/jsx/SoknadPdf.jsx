var SoknadPdf = function () {
    return (
        <div>
            <h1>Søknad om kontantstøtte</h1>
            <p>Dette er en React-komponent!</p>
        </div>
    );
};


function hentHTMLStringForOppsummering() {
    var komponent = ReactDOMServer.renderToStaticMarkup(SoknadPdf());
    return '<html><head><meta http-equiv="content-type" content="text/html; charset=utf-8"/>' +
            hentStyleHeader() +
        '</head><body>' + komponent + '</body></html>';
}

function hentStyleHeader() {
    return '<style type="text/css"> \
        @page:first { margin: 12mm 2cm 2.5cm; } \
        *       { box-sizing: border-box; } \
        body    { font-family: ArialSystem, sans-serif; font-size: 10pt; line-height: 1.4em; margin: 0; color: #3e3832; } \
    </style>';
}