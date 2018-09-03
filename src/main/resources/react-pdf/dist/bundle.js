"use strict";

var Arbeidsforhold = function Arbeidsforhold(props) {
  return React.createElement("div", null, React.createElement("h3", null, "Opplysninger om arbeidsforhold og andre ytelser"), React.createElement("ul", null, React.createElement(OppsummeringsListeElement, {
    tekst: props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' ? 'Jeg eller barnets andre forelder mottar ytelser fra utlandet' : 'Ingen av foreldrene mottar andre ytelser fra utlandet',
    children: props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' && React.createElement("div", null, React.createElement("h4", null, "Tilleggsinformasjon:"), React.createElement("p", null, props.arbeidsforhold.mottarYtelserFraUtlandetForklaring))
  }), React.createElement(OppsummeringsListeElement, {
    tekst: props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' ? 'Jeg eller barnets andre forelder jobber på utenlandsk kontinentalsokkel' : 'Ingen av foreldrene jobber på utenlandsk kontinentalsokkel',
    children: props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' && React.createElement("div", null, React.createElement("h4", null, "Tilleggsinformasjon:"), React.createElement("p", null, props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkelForklaring))
  }), React.createElement(OppsummeringsListeElement, {
    tekst: props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' ? 'Jeg eller barnets andre forelder mottar kontantstøtte fra et EØS-land' : 'Ingen av foreldrene mottar kontantstøtte fra et EØS-land',
    children: props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' && React.createElement("div", null, React.createElement("h4", null, "Tilleggsinformasjon:"), React.createElement("p", null, props.arbeidsforhold.mottarKontantstotteFraAnnetEOSForklaring))
  })));
};
"use strict";

var Barn = function Barn(props) {
  return React.createElement("div", null, React.createElement("h3", null, "Opplysninger om barnet eller barna"), React.createElement("ul", null, React.createElement(OppsummeringsListeElement, {
    tekst: props.mineBarn.navn + ', ' + props.mineBarn.fodselsdato
  })));
};
"use strict";

var Barnehageplass = function Barnehageplass(props) {
  var BarnehageplassVerdier = {
    Nei: 'Nei',
    NeiHarFaatt: 'NeiHarFaatt',
    Ja: 'Ja',
    JaSkalSlutte: 'JaSkalSlutte',
    Ubesvart: 'Ubesvart'
  };
  var header = React.createElement("h3", null, "Opplysning om barnehage:");

  switch (props.barnehageplass.harBarnehageplass) {
    case BarnehageplassVerdier.Ja:
      return React.createElement("div", null, header, React.createElement("ul", null, React.createElement(OppsummeringsListeElement, {
        tekst: 'Barnet har barnehageplass'
      }, React.createElement("h4", null, "Dato:"), React.createElement("p", null, props.barnehageplass.dato), React.createElement("h4", null, "Barnehageplass i kommunen:"), React.createElement("p", null, props.barnehageplass.kommune), React.createElement("h4", null, "Antall timer:"), React.createElement("p", null, props.barnehageplass.antallTimer))));

    case BarnehageplassVerdier.JaSkalSlutte:
      return React.createElement("div", null, header, React.createElement("ul", null, React.createElement(OppsummeringsListeElement, {
        tekst: 'Barnet går i barnehagen, men skal slutte'
      }, React.createElement("h4", null, "Dato:"), React.createElement("p", null, props.barnehageplass.dato), React.createElement("h4", null, "Barnehageplass i kommunen:"), React.createElement("p", null, props.barnehageplass.kommune), React.createElement("h4", null, "Antall timer:"), React.createElement("p", null, props.barnehageplass.antallTimer))));

    case BarnehageplassVerdier.NeiHarFaatt:
      return React.createElement("div", null, header, React.createElement("ul", null, React.createElement(OppsummeringsListeElement, {
        tekst: 'Barnet har fått barnehageplass, men ikke begynt enda'
      }, React.createElement("h4", null, "Dato:"), React.createElement("p", null, props.barnehageplass.dato), React.createElement("h4", null, "Barnehageplass i kommunen:"), React.createElement("p", null, props.barnehageplass.kommune))));

    case BarnehageplassVerdier.Nei:
      return React.createElement("div", null, header, React.createElement("ul", null, React.createElement(OppsummeringsListeElement, {
        tekst: 'Barnet har ikke barnehageplass'
      })));

    default:
      return React.createElement("div", null);
  }
};
"use strict";

var Familieforhold = function Familieforhold(props) {
  return React.createElement("div", null, React.createElement("h3", null, "Opplysning om familieforhold:"), React.createElement("ul", null, React.createElement("li", null, "Har foreldrene delt bosted: ", props.familieforhold.borForeldreneSammenMedBarnet), props.familieforhold.borForeldreneSammenMedBarnet === 'JA' && React.createElement(OppsummeringsListeElement, {
    tekst: 'Jeg bor sammen med den andre forelderen'
  }, React.createElement("h4", null, "Navnet til den andre forelderen:"), React.createElement("p", null, props.familieforhold.annenForelderNavn), React.createElement("h4", null, "Fodselsnummeret til den andre forelderen:"), React.createElement("p", null, props.familieforhold.annenForelderFodselsnummer))));
};
"use strict";

var OppsummeringsListeElement = function OppsummeringsListeElement(props) {
  return React.createElement("li", null, React.createElement("span", null, " ", props.tekst, " "), props.children && React.createElement("li", null, " ", props.children));
};
"use strict";

var PersonaliaOgBarnOppsummering = function PersonaliaOgBarnOppsummering(props) {
  return React.createElement("div", null, React.createElement("h4", null, "Det s\xF8kes kontantst\xF8tte for:"), React.createElement("p", null, props.barn.navn), React.createElement("p", null, 'Fødselsnummer: ' + props.barn.fodselsdato), React.createElement("h4", null, "Av forelder:"), React.createElement("p", null, 'Fødselsnummer: ' + props.person.fnr));
};
"use strict";

var SokerKrav = function SokerKrav(props) {
  return React.createElement("div", null, React.createElement("h3", null, "Kravene av elektronisk s\xF8knad"), React.createElement("ul", null, React.createElement(OppsummeringsListeElement, {
    tekst: 'Jeg har bodd eller vært yrkesaktiv i Norge i minst fem år'
  }), React.createElement(OppsummeringsListeElement, {
    tekst: 'Jeg bor sammen med barnet'
  }), React.createElement(OppsummeringsListeElement, {
    tekst: 'Jeg og barnet skal bo i Norge de neste 12 månedene'
  })));
};
"use strict";

var _react = _interopRequireDefault(require("react"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

var SoknadPdf =
/*#__PURE__*/
function (_React$Component) {
  _inherits(SoknadPdf, _React$Component);

  function SoknadPdf() {
    _classCallCheck(this, SoknadPdf);

    return _possibleConstructorReturn(this, _getPrototypeOf(SoknadPdf).apply(this, arguments));
  }

  _createClass(SoknadPdf, [{
    key: "render",
    value: function render() {
      return _react.default.createElement("div", null, _react.default.createElement("h1", null, "S\xF8knad om kontantst\xF8tte"), _react.default.createElement(PersonaliaOgBarnOppsummering, {
        person: this.props.soknad.person,
        barn: this.props.soknad.mineBarn
      }), _react.default.createElement(SokerKrav, {
        kravTilSoker: this.props.soknad.kravTilSoker
      }), _react.default.createElement(Barn, {
        mineBarn: this.props.soknad.mineBarn
      }), _react.default.createElement(Familieforhold, {
        familieforhold: this.props.soknad.familieforhold
      }), _react.default.createElement(Barnehageplass, {
        barnehageplass: this.props.soknad.barnehageplass
      }), _react.default.createElement(Arbeidsforhold, {
        arbeidsforhold: this.props.soknad.arbeidsforhold
      }));
    }
  }]);

  return SoknadPdf;
}(_react.default.Component);

function hentHTMLStringForOppsummering(soknad) {
  var komponent = ReactDOMServer.renderToStaticMarkup(_react.default.createElement(SoknadPdf, {
    soknad: soknad
  }));
  return '<html><head><meta http-equiv="content-type" content="text/html; charset=utf-8"/>' + hentStyleHeader() + '</head><body>' + komponent + '</body></html>';
}

function hentStyleHeader() {
  return '<style type="text/css"> \
        @page:first { margin: 12mm 2cm 2.5cm; } \
        *       { box-sizing: border-box; } \
        body    { font-family: ArialSystem, sans-serif; font-size: 10pt; line-height: 1.4em; margin: 0; color: #3e3832; } \
    </style>';
}
"use strict";

var _server = _interopRequireDefault(require("react-dom/server"));

var _SoknadPdf = _interopRequireDefault(require("./SoknadPdf"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var renderApp = function renderApp(soknad) {
  _server.default.renderToStaticMarkup(React.render(React.createElement(_SoknadPdf.default, {
    soknad: soknad
  }), document.getElementById('app')));
};
