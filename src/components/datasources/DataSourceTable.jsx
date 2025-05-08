import React from 'react'
import {
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Paper,
    Box
} from '@mui/material'
import DataSourceTableRow from './DataSourceTableRow'

export default function DataSourceTable({ dataSources, onDelete }) {
    return (
        <Paper sx={{ mt: 2 }}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Имя</TableCell>
                        <TableCell>Описание</TableCell>
                        <TableCell align="right">Действия</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {dataSources.map((ds) => (
                        <DataSourceTableRow key={ds.id} dataSource={ds} onDelete={onDelete} />
                    ))}
                    {dataSources.length === 0 && (
                        <TableRow>
                            <TableCell colSpan={3}>
                                <Box textAlign="center" p={2} color="text.secondary">
                                    Нет зарегистрированных источников данных
                                </Box>
                            </TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </Paper>
    )
}