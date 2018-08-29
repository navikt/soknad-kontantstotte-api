var SoknadPdf = function (soknad) {
    return React.createElement(
        'div',
        null,
        React.createElement(
            'h1',
            null,
            'S\xF8knad om kontantst\xF8tte'
        ),
        React.createElement(PersonaliaOgBarnOppsummering, { person: soknad.person, barn: soknad.mineBarn }),
        React.createElement(SokerKrav, { kravTilSoker: soknad.kravTilSoker }),
        React.createElement(Barn, { mineBarn: soknad.mineBarn }),
        React.createElement(Familieforhold, { familieforhold: soknad.familieforhold }),
        React.createElement(Barnehageplass, { barnehageplass: soknad.barnehageplass }),
        React.createElement(Arbeidsforhold, { arbeidsforhold: soknad.arbeidsforhold })
    );
};

function hentHTMLStringForOppsummering(soknad) {
    var komponent = ReactDOMServer.renderToStaticMarkup(SoknadPdf(soknad));
    return '<html><head><meta http-equiv="content-type" content="text/html; charset=utf-8"/>' + hentStyleHeader() + '</head><body>' + komponent + '</body></html>';
}

function hentStyleHeader() {
    return '<style type="text/css"> \
        @page:first { margin: 12mm 2cm 2.5cm; } \
        *       { box-sizing: border-box; } \
        body    { font-family: ArialSystem, sans-serif; font-size: 10pt; line-height: 1.4em; margin: 0; color: #3e3832; } \
    </style>';
}