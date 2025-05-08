import React, { createContext, useContext, useState, useCallback } from 'react'
import { Snackbar, Alert } from '@mui/material'

const NotificationContext = createContext()

export function NotificationProvider({ children }) {
    const [open, setOpen] = useState(false)
    const [message, setMessage] = useState('')
    const [severity, setSeverity] = useState('info')

    const showNotification = useCallback((msg, type = 'info') => {
        setMessage(msg)
        setSeverity(type)
        setOpen(true)
    }, [])

    const handleClose = () => setOpen(false)

    return (
        <NotificationContext.Provider value={{ showNotification }}>
            {children}
            <Snackbar open={open} autoHideDuration={3000} onClose={handleClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
                <Alert onClose={handleClose} severity={severity} variant="filled" sx={{ width: '100%' }}>
                    {message}
                </Alert>
            </Snackbar>
        </NotificationContext.Provider>
    )
}

export function useNotificationContext() {
    return useContext(NotificationContext)
}