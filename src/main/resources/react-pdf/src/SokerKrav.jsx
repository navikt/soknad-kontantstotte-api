const SokerKrav = (props) => {
        return (
            <Bolk>
                <h3>{props.tekster['sokerkrav.tittel'].toUpperCase()}</h3>
                <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.harBoddINorgeSisteFemAar']} />
                <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.borMedBarnet']} />
                <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.skalBoINorgeNesteAaret']} />
            </Bolk>
        );
};