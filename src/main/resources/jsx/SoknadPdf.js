var SoknadPdf2 = function() {
    return (
        <div>
            <h1>Søknad om kontantstøtte</h1>
        </div>
    );
};


function hentHTMLStringForOppsummering() {
    var komponent = ReactDOMServer.renderToStaticMarkup(SoknadPdf2());
    return '<html>' +
        '<head><meta http-equiv="content-type" content="text/html; charset=utf-8"/></head>' +
        '<body>' + komponent + '</body>' +
        '</html>';
}
