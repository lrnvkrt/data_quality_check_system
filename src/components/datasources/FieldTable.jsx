import React, { useState } from 'react'
import {
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    IconButton,
    TextField,
    MenuItem,
    Button
} from '@mui/material'
import DeleteIcon from '@mui/icons-material/Delete'

const dataTypes = ['int', 'string', 'boolean', 'float']

export default function FieldTable({ fields, onCreate, onDelete }) {
    const [name, setName] = useState('')
    const [dataType, setDataType] = useState('int')

    const handleAdd = () => {
        if (!name.trim()) return
        onCreate({ name, dataType })
        setName('')
        setDataType('int')
    }

    return (
        <>
            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>Имя</TableCell>
                        <TableCell>Тип данных</TableCell>
                        <TableCell align="right">Действия</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {fields.map((f) => (
                        <TableRow key={f.id}>
                            <TableCell>{f.name}</TableCell>
                            <TableCell>{f.dataType}</TableCell>
                            <TableCell align="right">
                                <IconButton color="error" onClick={() => onDelete(f.id)}>
                                    <DeleteIcon />
                                </IconButton>
                            </TableCell>
                        </TableRow>
                    ))}
                    <TableRow>
                        <TableCell>
                            <TextField
                                size="small"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                placeholder="Название поля"
                            />
                        </TableCell>
                        <TableCell>
                            <TextField
                                select
                                size="small"
                                value={dataType}
                                onChange={(e) => setDataType(e.target.value)}
                            >
                                {dataTypes.map((type) => (
                                    <MenuItem key={type} value={type}>
                                        {type}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </TableCell>
                        <TableCell align="right">
                            <Button onClick={handleAdd} variant="outlined" size="small">
                                Добавить
                            </Button>
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </>
    )
}