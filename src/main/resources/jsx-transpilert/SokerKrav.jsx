var SokerKrav = React.createClass({
    displayName: 'SokerKrav',

    render: function () {
        return React.createElement(
            'div',
            null,
            React.createElement(
                'h3',
                null,
                'Kravene av elektronisk s\xF8knad'
            ),
            React.createElement(
                'ul',
                null,
                React.createElement(OppsummeringsListeElement, {
                    tekst: 'Jeg har bodd eller vært yrkesaktiv i Norge i minst fem år'
                }),
                React.createElement(OppsummeringsListeElement, {
                    tekst: 'Jeg bor sammen med barnet'
                }),
                React.createElement(OppsummeringsListeElement, {
                    tekst: 'Jeg og barnet skal bo i Norge de neste 12 månedene'
                })
            )
        );
    }
});