import React from 'react'
import ReactDOM from 'react-dom/client'
import {BrowserRouter} from 'react-router-dom'
import {CssBaseline, ThemeProvider} from '@mui/material'
import App from './App'
import theme from "./theme.jsx";
import {NotificationProvider} from "./components/ui/NotificationProvider.jsx";

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <NotificationProvider>
            <ThemeProvider theme={theme}>
                <BrowserRouter>
                    <CssBaseline/>
                    <App/>
                </BrowserRouter>
            </ThemeProvider>
        </NotificationProvider>
    </React.StrictMode>
)
