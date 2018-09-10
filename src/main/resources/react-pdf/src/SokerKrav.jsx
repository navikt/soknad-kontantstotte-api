const SokerKrav = (props) => {
        return (
            <div>
                <h3>{props.tekster['sokerkrav.tittel']}</h3>
                <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.harBoddINorgeSisteFemAar']} />
                <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.borMedBarnet']} />
                <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.skalBoINorgeNesteAaret']} />
            </div>
        );
};