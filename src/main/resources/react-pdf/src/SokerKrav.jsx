const SokerKrav = (props) => {
        return (
            <div>
                <h3>Kravene av elektronisk søknad</h3>
                <OppsummeringsElement svar={'Jeg har bodd eller vært yrkesaktiv i Norge i minst fem år'} />
                <OppsummeringsElement svar={'Jeg bor sammen med barnet'} />
                <OppsummeringsElement svar={'Jeg og barnet skal bo i Norge de neste 12 månedene'} />
            </div>
        );
};