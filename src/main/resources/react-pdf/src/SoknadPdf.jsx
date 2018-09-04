class SoknadPdf extends React.Component {
    render () {
        return (
            <div>
                <h1>Søknad om kontantstøtte</h1>
                <PersonaliaOgBarnOppsummering person={this.props.soknad.person} barn={this.props.soknad.mineBarn}/>
                <SokerKrav kravTilSoker={this.props.soknad.kravTilSoker}/>
                <Barn mineBarn={this.props.soknad.mineBarn}/>
                <Familieforhold familieforhold={this.props.soknad.familieforhold}/>
                <Barnehageplass barnehageplass={this.props.soknad.barnehageplass}/>
                <Arbeidsforhold arbeidsforhold={this.props.soknad.arbeidsforhold}/>
            </div>
        );
    }
}

function hentHTMLStringForOppsummering(soknad) {
    var komponent = ReactDOMServer.renderToStaticMarkup(<SoknadPdf soknad={soknad} />);
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