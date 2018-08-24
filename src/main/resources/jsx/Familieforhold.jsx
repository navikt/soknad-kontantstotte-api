var Familieforhold = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Opplysning om familieforhold:</h3>
                <ul>
                    <li>Har foreldrene delt bosted: {this.props.familieforhold.erAvklartDeltBosted}</li>
                    {
                        this.props.familieforhold.erAvklartDeltBosted === 'JA' && (
                            <li>Har foreldrene delt bosted: {this.props.familieforhold.erAvklartDeltBosted}</li>
                        )
                    }
                </ul>
            </div>
        );
    }
});