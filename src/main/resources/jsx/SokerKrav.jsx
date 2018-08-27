var SokerKrav = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Kravene av elektronisk søknad</h3>
                <ul>
                    <OppsummeringsListeElement
                        tekst={'Jeg har bodd eller vært yrkesaktiv i Norge i minst fem år'}
                    />
                    <OppsummeringsListeElement
                        tekst={'Jeg bor sammen med barnet'}
                    />
                    <OppsummeringsListeElement
                        tekst={'Jeg og barnet skal bo i Norge de neste 12 månedene'}
                    />
                </ul>
            </div>
        );
    }
});