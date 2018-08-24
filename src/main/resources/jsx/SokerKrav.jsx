var SokerKrav = React.createClass({

    render: function () {
        return (
            <div>
                <h3>Kravene av elektronisk søknad</h3>
                <ul>
                    <li>Søker har bodd eller vært yrkesaktiv i Norge siste fem år: {this.props.kravTilSoker.boddINorgeSisteFemAar}</li>
                    <li>Søker bor i Norge sammen med barnet: {this.props.kravTilSoker.borSammenMedBarnet}</li>
                    <li>Søker skal bo i Norge sammen med barnet de neste tolv måneder: {this.props.kravTilSoker.skalBoMedBarnetINorgeNesteTolvMaaneder}</li>
                </ul>
            </div>
        );
    }
});