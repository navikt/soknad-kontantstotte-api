import * as React from 'react';
import { renderToStaticMarkup } from 'react-dom/server';

const SoknadPdf = ({}) => {
    return (
        <div>
            <h1>Søknad om kontantstøtte</h1>
        </div>
    );
};

function hentHtmlStringForOppsummering () {
    return `<!doctype html>` +
        `<html>` +
        `<head><meta http-equiv="content-type" content="text/html; charset=utf-8"/></head>` +
        `<body>${renderToStaticMarkup(<SoknadPdf/>)}</body>` +
        `</html>`;
}

export default hentHtmlStringForOppsummering;