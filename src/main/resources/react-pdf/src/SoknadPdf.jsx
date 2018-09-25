const wrapper = {
    padding: '0 5%',
};

const SideTittel = {
    container: {
        backgroundColor: '#c1b5d0',
        height: '60px',
    },
    tittel: {
        textTransform: 'uppercase',
        textAlign: 'center',
    },
    ikon: {
        height: '32.25px',
        width: '50px',
        display: 'inline-block',
        float: 'left',
        paddingTop: '13.875px',
        paddingLeft: '20px',
    },
};

const Uppercase = {
    textTransform: 'uppercase'
};

const SoknadPdf = (props) => {
    return (
        <div style={wrapper}>
            <div style={SideTittel.container} >
                <div style={SideTittel.ikon}>
                    <NavIkon />
                </div>
                <h1 style={SideTittel.tittel}>{props.tekster['kontantstotte.tittel'].toUpperCase()}</h1>
            </div>

            <Personalia person={props.soknad.person} tekster={props.tekster}/>
            <SokerKrav kravTilSoker={props.soknad.kravTilSoker} tekster={props.tekster}/>
            <Barn mineBarn={props.soknad.mineBarn} tekster={props.tekster}/>
            <Familieforhold familieforhold={props.soknad.familieforhold} tekster={props.tekster}/>
            <Barnehageplass barnehageplass={props.soknad.barnehageplass} tekster={props.tekster}/>
            <ArbeidIUtlandet arbeidIUtlandet={props.soknad.arbeidIUtlandet} familieforhold={props.soknad.familieforhold} tekster={props.tekster}/>
            <UtenlandskeYtelser familieforhold={props.soknad.familieforhold} utenlandskeYtelser={props.soknad.utenlandskeYtelser} tekster={props.tekster}/>
            <UtenlandskKontantstotte utenlandskKontantstotte={props.soknad.utenlandskKontantstotte} tekster={props.tekster}/>
            <Oppsummering tekster={props.tekster} />
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
                body    { font-family: Source Sans Pro, sans-serif; font-size: 10pt; line-height: 1.4em; margin: 0; color: #3e3832; } 
            </style>
            </head>
            <body>
            ${komponent}
            </body>
        </html>
`;

    return template
}