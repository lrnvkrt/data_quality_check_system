import React, { useState } from 'react'
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button
} from '@mui/material'

export default function CreateDataSourceDialog({ open, onClose, onCreate }) {
    const [name, setName] = useState('')
    const [description, setDescription] = useState('')

    const handleSubmit = () => {
        onCreate({ name, description })
        setName('')
        setDescription('')
        onClose()
    }

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>Добавить источник данных</DialogTitle>
            <DialogContent>
                <TextField
                    margin="dense"
                    label="Имя топика"
                    fullWidth
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <TextField
                    margin="dense"
                    label="Описание"
                    fullWidth
                    multiline
                    rows={3}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Отмена</Button>
                <Button onClick={handleSubmit} variant="contained">
                    Создать
                </Button>
            </DialogActions>
        </Dialog>
    )
}