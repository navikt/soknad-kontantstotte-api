var OppsummeringsListeElement = React.createClass({
    render: function () {
        return (
            <li>
                <span> {this.props.tekst} </span>
                {this.props.children && <li> {this.props.children}</li>}
            </li>
        )
    }
});