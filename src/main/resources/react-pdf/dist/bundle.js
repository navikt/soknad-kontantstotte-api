"use strict";

var ArbeidIUtlandet = function ArbeidIUtlandet(props) {
  return React.createElement(Bolk, null, React.createElement("h3", {
    style: Uppercase
  }, props.tekster['arbeidIUtlandet.tittel']), React.createElement("div", null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel.sporsmal'],
    svar: props.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel
  }), props.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring !== '' && React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['arbeidIUtlandet.forklaring.hjelpetekst'],
    svar: props.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring
  })), props.familieforhold.borForeldreneSammenMedBarnet === 'JA' && React.createElement("div", null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['arbeidIUtlandet.arbeiderAnnenForelderIUtlandet.sporsmal'],
    svar: props.arbeidIUtlandet.arbeiderAnnenForelderIUtlandet
  }), props.arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring !== '' && React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['arbeidIUtlandet.forklaring.hjelpetekst'],
    svar: props.arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring
  })));
};
"use strict";

var Barn = function Barn(props) {
  return React.createElement(Bolk, null, React.createElement("h3", {
    style: Uppercase
  }, props.tekster['barn.tittel']), React.createElement("h3", null, props.tekster['oppsummering.barn.subtittel']), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barn.navn'],
    svar: props.mineBarn.navn
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barn.fodselsdato'],
    svar: props.mineBarn.fodselsdato
  }));
};
"use strict";

var Barnehageplass = function Barnehageplass(props) {
  var date = new Date(props.barnehageplass.dato);
  var dateString = date.getDay() + "." + date.getMonth() + "." + date.getFullYear();
  var barnBarnehageplassStatusKey = 'Ubesvart';
  var BarnehageplassStatus = {
    GAR_IKKE_I_BARNEHAGE: 'garIkkeIBarnehage',
    HAR_BARNEHAGEPLASS: 'harBarnehageplass',
    HAR_SLUTTET_I_BARNEHAGE: 'harSluttetIBarnehage',
    SKAL_BEGYNNE_I_BARNEHAGE: 'skalBegynneIBarnehage',
    SKAL_SLUTTE_I_BARNEHAGE: 'skalSlutteIBarnehage'
  };

  switch (props.barnehageplass.barnBarnehageplassStatus) {
    case BarnehageplassStatus.GAR_IKKE_I_BARNEHAGE:
      barnBarnehageplassStatusKey = 'barnehageplass.garIkkeIBarnehage';
      break;

    case BarnehageplassStatus.HAR_BARNEHAGEPLASS:
      barnBarnehageplassStatusKey = 'barnehageplass.harBarnehageplass';
      break;

    case BarnehageplassStatus.HAR_SLUTTET_I_BARNEHAGE:
      barnBarnehageplassStatusKey = 'barnehageplass.harSluttetIBarnehage';
      break;

    case BarnehageplassStatus.SKAL_BEGYNNE_I_BARNEHAGE:
      barnBarnehageplassStatusKey = 'barnehageplass.skalBegynneIBarnehage';
      break;

    case BarnehageplassStatus.SKAL_SLUTTE_I_BARNEHAGE:
      barnBarnehageplassStatusKey = 'barnehageplass.skalSlutteIBarnehage';
      break;
  }

  return React.createElement(Bolk, null, React.createElement("h3", {
    style: Uppercase
  }, props.tekster['oppsummering.barnehageplass.tittel']), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['oppsummering.barnehageplass.harBarnehageplass'],
    svar: props.barnehageplass.harBarnehageplass
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.barnBarnehageplassStatus'],
    svar: props.tekster[barnBarnehageplassStatusKey]
  }), props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.HAR_SLUTTET_I_BARNEHAGE && React.createElement(React.Fragment, null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.harSluttetIBarnehage.dato.sporsmal'],
    svar: props.barnehageplass.harSluttetIBarnehageDato
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.harSluttetIBarnehage.kommune.sporsmal'],
    svar: props.barnehageplass.harSluttetIBarnehageKommune
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.harSluttetIBarnehage.antallTimer.sporsmal'],
    svar: props.barnehageplass.harSluttetIBarnehageAntallTimer
  })), props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.SKAL_SLUTTE_I_BARNEHAGE && React.createElement(React.Fragment, null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.skalSlutteIBarnehage.dato.sporsmal'],
    svar: props.barnehageplass.skalSlutteIBarnehageDato
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.skalSlutteIBarnehage.kommune.sporsmal'],
    svar: props.barnehageplass.skalSlutteIBarnehageKommune
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.skalSlutteIBarnehage.antallTimer.sporsmal'],
    svar: props.barnehageplass.skalSlutteIBarnehageAntallTimer
  })), props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.SKAL_BEGYNNE_I_BARNEHAGE && React.createElement(React.Fragment, null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.skalBegynneIBarnehage.dato.sporsmal'],
    svar: props.barnehageplass.skalBegynneIBarnehageDato
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.skalBegynneIBarnehage.kommune.sporsmal'],
    svar: props.barnehageplass.skalBegynneIBarnehageKommune
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.skalBegynneIBarnehage.antallTimer.sporsmal'],
    svar: props.barnehageplass.skalBegynneIBarnehageAntallTimer
  })), props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.HAR_BARNEHAGEPLASS && React.createElement(React.Fragment, null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.harBarnehageplass.antallTimer.sporsmal'],
    svar: props.barnehageplass.harBarnehageplassAntallTimer
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.harBarnehageplass.dato.sporsmal'],
    svar: props.barnehageplass.harBarnehageplassDato
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['barnehageplass.harBarnehageplass.kommune.sporsmal'],
    svar: props.barnehageplass.harBarnehageplassKommune
  })));
};
"use strict";

var topBorder = {
  borderTop: '1px solid black',
  pageBreakInside: 'avoid'
};

var Bolk = function Bolk(props) {
  return React.createElement("div", {
    style: topBorder
  }, props.children);
};
"use strict";

var Familieforhold = function Familieforhold(props) {
  return React.createElement(Bolk, null, React.createElement("h3", {
    style: Uppercase
  }, props.tekster['familieforhold.tittel']), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['familieforhold.borForeldreneSammenMedBarnet.sporsmal'],
    svar: props.familieforhold.borForeldreneSammenMedBarnet
  }), props.familieforhold.borForeldreneSammenMedBarnet === 'JA' && React.createElement(React.Fragment, null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['oppsummering.familieforhold.annenForelderNavn.label'],
    svar: props.familieforhold.annenForelderNavn
  }), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['oppsummering.familieforhold.annenForelderFodselsnummer.label'],
    svar: props.familieforhold.annenForelderFodselsnummer
  })));
};
"use strict";

var Oppsummering = function Oppsummering(props) {
  return React.createElement(Bolk, null, React.createElement("h4", null, props.tekster['oppsummering.bekreftelse.label']));
};
"use strict";

var style = {
  tekst: {
    whiteSpace: 'pre-wrap',
    wordBreak: 'break-word'
  }
};

var OppsummeringsElement = function OppsummeringsElement(props) {
  return React.createElement("div", null, props.sporsmal && React.createElement("h4", null, props.sporsmal), React.createElement("p", {
    style: style.tekst
  }, props.svar));
};
"use strict";

var spacing = {
  margin: '0 0 10px 0',
  padding: '0 0 10px 0',
  textAlign: 'center'
};

var Personalia = function Personalia(props) {
  return React.createElement("div", {
    style: spacing
  }, React.createElement("h2", null, props.person.fnr));
};
"use strict";

var SokerKrav = function SokerKrav(props) {
  return React.createElement(Bolk, null, React.createElement("h3", {
    style: Uppercase
  }, props.tekster['sokerkrav.tittel']), React.createElement(OppsummeringsElement, {
    svar: props.tekster['oppsummering.kravtilsoker.norskStatsborger']
  }), React.createElement(OppsummeringsElement, {
    svar: props.tekster['oppsummering.kravtilsoker.boddEllerJobbetINorgeSisteFemAar']
  }), React.createElement(OppsummeringsElement, {
    svar: props.tekster['oppsummering.kravtilsoker.borSammenMedBarnet']
  }), React.createElement(OppsummeringsElement, {
    svar: props.tekster['oppsummering.kravtilsoker.barnIkkeHjemme']
  }), React.createElement(OppsummeringsElement, {
    svar: props.tekster['oppsummering.kravtilsoker.ikkeAvtaltDeltBosted']
  }), React.createElement(OppsummeringsElement, {
    svar: props.tekster['oppsummering.kravtilsoker.skalBoMedBarnetINorgeNesteTolvMaaneder']
  }));
};
"use strict";

var wrapper = {
  alignItems: 'center',
  display: 'flex',
  flexDirection: 'column'
};
var SideTittel = {
  backgroundColor: '#c1b5d0',
  margin: '20px 0 20px 0',
  padding: '20px 0 20px 0',
  textAlign: 'center'
};
var Uppercase = {
  textTransform: 'uppercase'
};

var SoknadPdf = function SoknadPdf(props) {
  return React.createElement("div", {
    style: wrapper
  }, React.createElement("div", {
    style: SideTittel
  }, React.createElement("h1", {
    style: Uppercase
  }, props.tekster['kontantstotte.tittel'].toUpperCase())), React.createElement(Personalia, {
    person: props.soknad.person,
    tekster: props.tekster
  }), React.createElement(SokerKrav, {
    kravTilSoker: props.soknad.kravTilSoker,
    tekster: props.tekster
  }), React.createElement(Barn, {
    mineBarn: props.soknad.mineBarn,
    tekster: props.tekster
  }), React.createElement(Familieforhold, {
    familieforhold: props.soknad.familieforhold,
    tekster: props.tekster
  }), React.createElement(Barnehageplass, {
    barnehageplass: props.soknad.barnehageplass,
    tekster: props.tekster
  }), React.createElement(ArbeidIUtlandet, {
    arbeidIUtlandet: props.soknad.arbeidIUtlandet,
    familieforhold: props.soknad.familieforhold,
    tekster: props.tekster
  }), React.createElement(UtenlandskeYtelser, {
    familieforhold: props.soknad.familieforhold,
    utenlandskeYtelser: props.soknad.utenlandskeYtelser,
    tekster: props.tekster
  }), React.createElement(UtenlandskKontantstotte, {
    utenlandskKontantstotte: props.soknad.utenlandskKontantstotte,
    tekster: props.tekster
  }), React.createElement(Oppsummering, {
    tekster: props.tekster
  }));
};

function hentHTMLStringForOppsummering(soknad, tekster) {
  var komponent = ReactDOMServer.renderToStaticMarkup(React.createElement(SoknadPdf, {
    soknad: soknad,
    tekster: tekster
  }));
  var template = "\n        <html>\n            <head>\n            <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n            <style type=\"text/css\"> \n                @page   { margin: 12mm 1cm 2.5cm; } \n                *       { box-sizing: border-box; } \n                body    { font-family: ArialSystem, sans-serif; font-size: 10pt; line-height: 1.4em; margin: 0; color: #3e3832; } \n            </style>\n            </head>\n            <body>\n            ".concat(komponent, "\n            </body>\n        </html>\n");
  return template;
}
"use strict";

var UtenlandskKontantstotte = function UtenlandskKontantstotte(props) {
  return React.createElement(Bolk, null, React.createElement("h3", {
    style: Uppercase
  }, props.tekster['utenlandskKontantstotte.tittel']), React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['utenlandskKontantstotte.mottarKontantstotteFraUtlandet.sporsmal'],
    svar: props.utenlandskKontantstotte.mottarKontantstotteFraUtlandet
  }), props.utenlandskKontantstotte.mottarKontantstotteFraUtlandet === 'JA' && React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['utenlandskKontantstotte.mottarKontantstotteFraUtlandet.tilleggsinfo.sporsmal'],
    svar: props.utenlandskKontantstotte.mottarKontantstotteFraUtlandetTilleggsinfo
  }));
};
"use strict";

var UtenlandskeYtelser = function UtenlandskeYtelser(props) {
  return React.createElement(Bolk, null, React.createElement("h3", {
    style: Uppercase
  }, props.tekster['utenlandskeYtelser.tittel']), React.createElement("div", null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['utenlandskeYtelser.mottarYtelserFraUtland.sporsmal'],
    svar: props.utenlandskeYtelser.mottarYtelserFraUtland
  }), props.utenlandskeYtelser.mottarYtelserFraUtlandForklaring !== '' && React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['oppsummering.utenlandskeYtelser.forklaring.label'],
    svar: props.utenlandskeYtelser.mottarYtelserFraUtlandForklaring
  })), props.familieforhold.borForeldreneSammenMedBarnet === 'JA' && React.createElement("div", null, React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['oppsummering.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland'],
    svar: props.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland
  }), props.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring !== '' && React.createElement(OppsummeringsElement, {
    sporsmal: props.tekster['oppsummering.utenlandskeYtelser.forklaring.label'],
    svar: props.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring
  })));
};
