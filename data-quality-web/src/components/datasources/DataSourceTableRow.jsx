import React, { useState, useEffect } from 'react'
import {
    TableRow,
    TableCell,
    IconButton,
    Collapse,
    Box,
    Typography
} from '@mui/material'
import DeleteIcon from '@mui/icons-material/Delete'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import ExpandLessIcon from '@mui/icons-material/ExpandLess'
import FieldTable from './FieldTable'
import { getFields, createField, deleteField } from '../../api/fieldApi'
import { useNotification } from '../../hooks/useNotification'

export default function DataSourceTableRow({ dataSource, onDelete }) {
    const [expanded, setExpanded] = useState(false)
    const [fields, setFields] = useState([])
    const showNotification = useNotification()

    useEffect(() => {
        if (expanded) {
            loadFields()
        }
    }, [expanded])

    const loadFields = async () => {
        try {
            const response = await getFields(dataSource.id)
            setFields(response)
        } catch (e) {
            showNotification('Ошибка загрузки полей', 'error')
        }
    }

    const handleCreateField = async (field) => {
        try {
            await createField(dataSource.id, field)
            await loadFields()
            showNotification('Поле добавлено', 'success')
        } catch (e) {
            showNotification('Ошибка при создании поля', 'error')
        }
    }

    const handleDeleteField = async (fieldId) => {
        try {
            await deleteField(dataSource.id, fieldId)
            await loadFields()
            showNotification('Поле удалено', 'success')
        } catch (e) {
            showNotification('Ошибка при удалении поля', 'error')
        }
    }

    return (
        <>
            <TableRow>
                <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                            <IconButton onClick={() => setExpanded(!expanded)} size="small">
                                {expanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                            </IconButton>
                            <Typography variant="body1" sx={{ ml: 1 }}>
                                {dataSource.name}
                            </Typography>
                        </Box>
                </TableCell>
                <TableCell>{dataSource.description}</TableCell>
                <TableCell align="right">
                    <IconButton color="error" onClick={() => onDelete(dataSource.id)}>
                        <DeleteIcon />
                    </IconButton>
                </TableCell>
            </TableRow>
            <TableRow>
                <TableCell colSpan={3} sx={{ p: 0, borderBottom: 0 }}>
                    <Collapse in={expanded} timeout="auto" unmountOnExit>
                        <Box sx={{ m: 2 }}>
                            <Typography variant="subtitle1" gutterBottom>
                                Поля топика
                            </Typography>
                            <FieldTable
                                fields={fields}
                                onCreate={handleCreateField}
                                onDelete={handleDeleteField}
                            />
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </>
    )
}