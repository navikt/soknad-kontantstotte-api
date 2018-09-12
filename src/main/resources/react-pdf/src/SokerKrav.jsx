const SokerKrav = props => {
    return (
        <Bolk>
            <h3 style={Uppercase}>{props.tekster['sokerkrav.tittel']}</h3>
            <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.norskStatsborger']}/>
            <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.boddEllerJobbetINorgeSisteFemAar']}/>
            <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.borSammenMedBarnet']}/>
            <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.barnIkkeHjemme']}/>
            <OppsummeringsElement svar={props.tekster['oppsummering.kravtilsoker.ikkeAvtaltDeltBosted']}/>
            <OppsummeringsElement
                svar={props.tekster['oppsummering.kravtilsoker.skalBoMedBarnetINorgeNesteTolvMaaneder']}/>
        </Bolk>
    )
};