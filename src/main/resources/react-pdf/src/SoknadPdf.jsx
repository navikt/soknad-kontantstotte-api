const wrapper = {
    alignItems: 'center',
    display: 'flex',
    flexDirection: 'column'
};

const SideTittel = {
    backgroundColor:'#c1b5d0',
    margin:'20px 0 40px 0',
    padding: '20px 0 20px 0',
    textAlign: 'center'
};

const SoknadPdf = (props) => {
    return (
        <div style={wrapper}>
            <div style={SideTittel}>
                <h1>Søknad om kontantstøtte</h1>
            </div>

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
                @page   { margin: 12mm 1cm 2.5cm; } 
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