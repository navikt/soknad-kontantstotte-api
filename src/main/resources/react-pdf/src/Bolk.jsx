const topBorder = {
    borderTop: '1px solid black',
    pageBreakInside: 'avoid'
};

const Bolk = (props) => {
    return (
        <div style={topBorder}>
            {props.children}
        </div>
    );
};