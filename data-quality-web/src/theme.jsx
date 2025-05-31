import { createTheme } from '@mui/material/styles'

const theme = createTheme({
    palette: {
        mode: 'light',
        primary: {
            main: '#155447'
        }
    },
    typography: {
        fontFamily: 'Roboto, sans-serif',
        fontWeightMedium: 500
    }
})

export default theme