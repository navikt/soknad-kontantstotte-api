var OppsummeringsListeElement = React.createClass({
    displayName: "OppsummeringsListeElement",

    render: function () {
        return React.createElement(
            "li",
            null,
            React.createElement(
                "span",
                null,
                " ",
                this.props.tekst,
                " "
            ),
            this.props.children && React.createElement(
                "li",
                null,
                " ",
                this.props.children
            )
        );
    }
});