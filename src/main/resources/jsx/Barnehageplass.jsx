var Barnehageplass = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Opplysninger om barnehageplass</h3>
                <ul>
                    <li>Barnet går i barnehage: {this.props.barnehageplass.harBarnehageplass}</li>
                </ul>
            </div>
        );
    }
});