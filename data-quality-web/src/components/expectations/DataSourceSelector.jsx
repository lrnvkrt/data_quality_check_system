import React, { useEffect, useState } from 'react'
import { FormControl, InputLabel, Select, MenuItem } from '@mui/material'
import { fetchDataSources } from '../../api/dataSourceApi'

export default function DataSourceSelector({ selected, onChange }) {
    const [dataSources, setDataSources] = useState([])

    useEffect(() => {
        fetchDataSources()
            .then(setDataSources)
            .catch((e) => console.error('Ошибка загрузки источников', e))
    }, [])

    return (
        <FormControl fullWidth sx={{ mb: 3 }}>
            <InputLabel id="datasource-select-label">Источник данных</InputLabel>
            <Select
                labelId="datasource-select-label"
                value={selected || ''}
                label="Источник данных"
                onChange={(e) => onChange(e.target.value)}
             variant={"filled"}>
                {dataSources.map((ds) => (
                    <MenuItem key={ds.id} value={ds.id}>
                        {ds.name}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    )
}