import React, { useEffect, useState } from 'react'
import {
    Table, TableHead, TableRow, TableCell,
    TableBody, Paper, Typography
} from '@mui/material'
import FieldExpectationRow from './FieldExpectationRow'
import { fetchFieldsByDataSource } from '../../api/fieldApi'
import { useNotification } from '../../hooks/useNotification'

export default function FieldExpectationTable({ dataSourceId }) {
    const [fields, setFields] = useState([])
    const showNotification = useNotification()

    useEffect(() => {
        if (!dataSourceId) return

        fetchFieldsByDataSource(dataSourceId)
            .then(setFields)
            .catch((e) => {
                console.error(e)
                showNotification('Не удалось загрузить поля', 'error')
            })
    }, [dataSourceId, showNotification])

    if (!dataSourceId) return null

    return (
        <Paper sx={{ mt: 2 }}>
            <Typography variant="h6" sx={{ p: 2 }}>Поля источника</Typography>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell />
                        <TableCell>Имя поля</TableCell>
                        <TableCell>Тип данных</TableCell>
                        <TableCell align="right">Действия</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {fields.map((field) => (
                        <FieldExpectationRow key={field.id} field={field} dataSourceId={dataSourceId} />
                    ))}
                </TableBody>
            </Table>
        </Paper>
    )
}