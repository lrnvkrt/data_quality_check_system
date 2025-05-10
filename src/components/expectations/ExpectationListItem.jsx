import React from 'react'
import {
    Paper,
    Box,
    Typography,
    Stack,
    IconButton,
    Switch,
    Chip,
    Tooltip
} from '@mui/material'
import DeleteIcon from '@mui/icons-material/Delete'
import { deleteExpectation, toggleExpectation } from '../../api/expectationApi'
import { useNotification } from '../../hooks/useNotification'

export default function ExpectationListItem({ expectation, onChanged }) {
    const showNotification = useNotification()

    const handleDelete = async () => {
        try {
            await deleteExpectation(expectation.expectationId)
            showNotification('Правило удалено', 'success')
            onChanged()
        } catch (e) {
            console.error(e)
            showNotification('Ошибка при удалении правила', 'error')
        }
    }

    const handleToggle = async () => {
        try {
            await toggleExpectation(expectation.expectationId)
            onChanged()
        } catch (e) {
            console.error(e)
            showNotification('Ошибка при переключении состояния', 'error')
        }
    }

    const displaySeverity =
        expectation.severity === 'ERROR' ? 'CRITICAL' : expectation.severity

    const severityColor =
        displaySeverity === 'CRITICAL'
            ? 'error'
            : displaySeverity === 'WARNING'
                ? 'warning'
                : 'default'

    return (
        <Paper
            elevation={2}
            sx={{
                p: 2,
                mb: 1.5,
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                borderRadius: 2
            }}
        >
            <Box>
                <Typography variant="subtitle1" fontWeight={600}>
                    {expectation.description || `Тип: ${expectation.expectationCode}`}
                </Typography>

                {expectation.rowCondition && (
                    <Typography
                        variant="body2"
                        sx={{ mt: 1 }}
                        color="text.secondary"
                    >
                        <strong>Условие выборки:</strong> {expectation.rowCondition}
                    </Typography>
                )}
                <Stack direction="row" spacing={1} alignItems="center" mt={0.5} flexWrap="wrap">
                    <Chip size="small" label={displaySeverity} color={severityColor} />
                    <Chip
                        size="small"
                        label={`Тип: ${expectation.expectationCode}`}
                        variant="outlined"
                    />
                    {expectation.kwargs &&
                        Object.entries(expectation.kwargs).map(([key, value]) => (
                            <Chip
                                key={key}
                                size="small"
                                label={`${key}: ${value}`}
                                variant="outlined"
                            />
                        ))}
                </Stack>
            </Box>

            <Stack direction="row" spacing={1} alignItems="center">
                <Tooltip title={expectation.enabled ? 'Отключить' : 'Включить'}>
                    <Switch
                        checked={expectation.enabled}
                        onChange={handleToggle}
                        inputProps={{ 'aria-label': 'Переключатель правила' }}
                    />
                </Tooltip>
                <Tooltip title="Удалить правило">
                    <IconButton onClick={handleDelete}>
                        <DeleteIcon />
                    </IconButton>
                </Tooltip>
            </Stack>
        </Paper>
    )
}