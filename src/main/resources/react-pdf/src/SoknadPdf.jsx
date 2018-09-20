const wrapper = {
    alignItems: 'center',
    display: 'flex',
    flexDirection: 'column'
};

const SideTittel = {
    backgroundColor:'#c1b5d0',
    margin:'20px 0 20px 0',
    padding: '20px 0 20px 0',
    textAlign: 'center'
};

const Uppercase = {
    textTransform: 'uppercase'
};

const SoknadPdf = (props) => {
    return (
        <div style={wrapper}>
            <div style={SideTittel}>
                <h1 style={Uppercase}>{props.tekster['kontantstotte.tittel'].toUpperCase()}</h1>
            </div>

            <Personalia person={props.soknad.person} tekster={props.tekster}/>
            <SokerKrav kravTilSoker={props.soknad.kravTilSoker} tekster={props.tekster}/>
            <Barn mineBarn={props.soknad.mineBarn} tekster={props.tekster}/>
            <Familieforhold familieforhold={props.soknad.familieforhold} tekster={props.tekster}/>
            <Barnehageplass barnehageplass={props.soknad.barnehageplass} tekster={props.tekster}/>
            <Arbeidsforhold arbeidsforhold={props.soknad.arbeidsforhold} tekster={props.tekster}/>
            <UtenlandskeYtelser familieforhold={props.soknad.familieforhold} utenlandskeYtelser={props.soknad.utenlandskeYtelser} tekster={props.tekster}/>
            <Bekreftelse tekster={props.tekster} />
        </div>
    );
};

function hentHTMLStringForOppsummering(soknad, tekster) {
    var komponent = ReactDOMServer.renderToStaticMarkup(<SoknadPdf soknad={soknad} tekster={tekster}/>);
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