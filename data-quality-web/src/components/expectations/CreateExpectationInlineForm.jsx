import React, { useEffect, useState } from 'react'
import {Box, Grid, Button, Typography, TextField} from '@mui/material'
import {
    fetchExpectationCatalog,
    createFieldExpectation
} from '../../api/expectationApi'
import { useNotification } from '../../hooks/useNotification'
import ExpectationTypeSelector from './ExpectationTypeSelector'
import SeveritySelector from './SeveritySelector'
import DynamicKwargsForm from './DynamicKwargsForm'
import RowConditionField from './RowConditionField'

export default function CreateExpectationInlineForm({ fieldId, fieldType, onCreated, dataSourceId }) {
    const [catalog, setCatalog] = useState([])
    const [expectationTypeId, setExpectationTypeId] = useState('')
    const [description, setDescription] = useState('')
    const [severity, setSeverity] = useState('INFO')
    const [kwargs, setKwargs] = useState({})
    const [rowCondition, setRowCondition] = useState('')
    const showNotification = useNotification()

    useEffect(() => {
        fetchExpectationCatalog()
            .then(setCatalog)
            .catch(() => showNotification('Ошибка загрузки справочника правил', 'error'))
    }, [showNotification])

    const selectedType = catalog.find((item) => item.id === expectationTypeId)
    const allowedArgsType = selectedType?.allowedKwargTypes || {}
    const supportsRawCondition = selectedType?.supportsRawCondition || false

    const handleSubmit = async () => {
        try {
            const finalKwargs = {}
            for (const [key, type] of Object.entries(allowedArgsType)) {
                const raw = kwargs[key]
                if (raw == null || raw === '') {
                    showNotification(`Поле "${key}" обязательно`, 'error')
                    return
                }

                let val
                switch (type) {
                    case 'int': val = parseInt(raw, 10); break
                    case 'float': val = parseFloat(raw); break
                    case 'boolean': val = raw === 'true' || raw === true; break
                    case 'list':
                        val = raw.split('\n').map((line) => line.trim()).filter(Boolean)
                        break
                    default: val = String(raw)
                }

                if ((type === 'int' || type === 'float') && isNaN(val)) {
                    showNotification(`Аргумент "${key}" должен быть числом`, 'error')
                    return
                }

                finalKwargs[key] = val
            }

            const payload = {
                expectationTypeId,
                description,
                severity,
                kwargs: finalKwargs,
                enabled: true
            }

            if (supportsRawCondition && rowCondition.trim()) {
                payload.rowCondition = rowCondition.trim()
            }

            await createFieldExpectation(fieldId, payload)
            setExpectationTypeId('')
            setDescription('')
            setSeverity('INFO')
            setKwargs({})
            setRowCondition('')
            showNotification('Правило добавлено', 'success')
            onCreated()
        } catch {
            showNotification('Ошибка при создании правила', 'error')
        }
    }

    return (
        <Box sx={{ mt: 2 }}>
            <Typography variant="subtitle1" gutterBottom>Добавить новое правило</Typography>
            <Grid container spacing={2} alignItems="center">
                <ExpectationTypeSelector
                    catalog={catalog}
                    fieldType={fieldType}
                    value={expectationTypeId}
                    onChange={(id) => {
                        setExpectationTypeId(id)
                        setKwargs({})
                        setRowCondition('')
                    }}
                />
                <SeveritySelector value={severity} onChange={setSeverity} />
                <Grid item xs={12} sm={1}>
                    <TextField
                        type="text"
                        placeholder="Описание"
                        style={{ width: '100%', height: '100%'}}
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />
                </Grid>
                <Grid item xs={12} sm={1}>
                    <Button
                        onClick={handleSubmit}
                        variant="contained"
                        disabled={!expectationTypeId}
                    >
                        Создать
                    </Button>
                </Grid>
            </Grid>

            <DynamicKwargsForm
                kwargs={kwargs}
                onChange={setKwargs}
                allowedArgsType={allowedArgsType}
            />

            {supportsRawCondition && (
                <RowConditionField
                    dataSourceId={dataSourceId}
                    value={rowCondition}
                    onChange={setRowCondition}
                />
            )}
        </Box>
    )
}
