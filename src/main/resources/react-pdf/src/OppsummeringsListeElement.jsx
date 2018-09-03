const OppsummeringsListeElement = (props) => {
        return (
            <li>
                <span> {props.tekst} </span>
                {props.children && <li> {props.children}</li>}
            </li>
        );
};
