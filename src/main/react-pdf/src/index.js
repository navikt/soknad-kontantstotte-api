import ReactDOMServer from 'react-dom/server';
import SoknadPdf from './SoknadPdf';

const renderApp = (soknad) => {
    ReactDOMServer.renderToStaticMarkup(
        React.render(<SoknadPdf soknad={soknad} />, document.getElementById('app'))
    )
};
