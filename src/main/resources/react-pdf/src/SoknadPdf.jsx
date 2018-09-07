const SoknadPdf = (props) => {
    return (
        <div>
            <h1>Søknad om kontantstøtte</h1>
            <Personalia person={props.soknad.person} />
            <SokerKrav kravTilSoker={props.soknad.kravTilSoker}/>
            <Barn mineBarn={props.soknad.mineBarn}/>
            <Familieforhold familieforhold={props.soknad.familieforhold}/>
            <Barnehageplass barnehageplass={props.soknad.barnehageplass}/>
            <Arbeidsforhold arbeidsforhold={props.soknad.arbeidsforhold}/>
        </div>
    );
};

function hentHTMLStringForOppsummering(soknad) {
    var komponent = ReactDOMServer.renderToStaticMarkup(<SoknadPdf soknad={soknad} />);
    const template = `
        <html>
            <head>
            <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
            <style type="text/css"> 
                @page:first { margin: 12mm 2cm 2.5cm; } 
                *       { box-sizing: border-box; } 
                body    { font-family: ArialSystem, sans-serif; font-size: 10pt; line-height: 1.4em; margin: 0; color: #3e3832; } 
            </style>
            </head>
            <body>
            ${komponent}
            </body>
        </html>
`;

    return template
}