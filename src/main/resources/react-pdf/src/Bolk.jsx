const topBorder = {
    borderTop: '1px solid black'
};

const Bolk = (props) => {
    return (
        <div style={topBorder}>
            {props.children}
        </div>
    );
};