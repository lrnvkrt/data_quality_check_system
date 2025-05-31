import React, { useEffect, useState, useCallback } from 'react'
import {
    TableRow,
    TableCell,
    Collapse,
    IconButton,
    Box,
    Typography,
    CircularProgress
} from '@mui/material'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import ExpandLessIcon from '@mui/icons-material/ExpandLess'
import { fetchFieldExpectations } from '../../api/expectationApi'
import { useNotification } from '../../hooks/useNotification'
import ExpectationList from './ExpectationList'
import CreateExpectationInlineForm from './CreateExpectationInlineForm'

export default function FieldExpectationRow({ field, dataSourceId }) {
    const [expanded, setExpanded] = useState(false)
    const [expectations, setExpectations] = useState([])
    const [loading, setLoading] = useState(false)
    const showNotification = useNotification()

    const loadExpectations = useCallback(async () => {
        setLoading(true)
        try {
            const list = await fetchFieldExpectations(field.id)
            setExpectations(list)
        } catch (e) {
            console.error(e)
            showNotification('Ошибка при загрузке правил поля', 'error')
        } finally {
            setLoading(false)
        }
    }, [field.id, showNotification])

    useEffect(() => {
        if (expanded) {
            loadExpectations()
        }
    }, [expanded, loadExpectations])

    const handleCreated = () => {
        loadExpectations()
    }

    return (
        <>
            <TableRow hover>
                <TableCell>
                    <IconButton onClick={() => setExpanded(!expanded)}>
                        {expanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                    </IconButton>
                </TableCell>
                <TableCell>{field.name}</TableCell>
                <TableCell>{field.dataType}</TableCell>
                <TableCell align="right" />
            </TableRow>

            <TableRow>
                <TableCell colSpan={4} sx={{ p: 0, border: 0 }}>
                    <Collapse in={expanded} timeout="auto" unmountOnExit>
                        <Box sx={{ p: 2 }}>
                            <Typography variant="subtitle1" gutterBottom>
                                Правила
                            </Typography>

                            {loading ? (
                                <Box sx={{ py: 2, textAlign: 'center' }}>
                                    <CircularProgress size={24} />
                                </Box>
                            ) : expectations.length > 0 ? (
                                <ExpectationList
                                    expectations={expectations}
                                    onChanged={loadExpectations}
                                />
                            ) : (
                                <Typography variant="body2" color="text.secondary">
                                    Нет правил для этого поля
                                </Typography>
                            )}

                            <CreateExpectationInlineForm
                                dataSourceId={dataSourceId}
                                fieldId={field.id}
                                fieldType={field.dataType}
                                onCreated={handleCreated}
                            />
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </>
    )
}