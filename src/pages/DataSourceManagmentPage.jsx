import React, { useCallback, useEffect, useState } from 'react'
import {
    Container,
    Typography,
    Box,
    Button
} from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import {
    fetchDataSources,
    deleteDataSource,
    createDataSource
} from '../api/dataSourceApi'
import DataSourceTable from '../components/datasources/DataSourceTable'
import CreateDataSourceDialog from '../components/datasources/CreateDataSourceDialog.jsx'
import { useNotification } from '../hooks/useNotification'

export default function DataSourceManagementPage() {
    const [dataSources, setDataSources] = useState([])
    const [dialogOpen, setDialogOpen] = useState(false)
    const showNotification = useNotification()

    const loadDataSources = useCallback(async () => {
        try {
            const data = await fetchDataSources()
            setDataSources(data)
        } catch (e) {
            console.error(e)
            showNotification('Ошибка загрузки источников', 'error')
        }
    }, [showNotification])

    useEffect(() => {
        loadDataSources()
    }, [loadDataSources])

    const handleDelete = async (id) => {
        if (!window.confirm('Удалить источник?')) return
        try {
            await deleteDataSource(id)
            await loadDataSources()
            showNotification('Источник удалён', 'success')
        } catch (e) {
            console.error(e)
            showNotification('Ошибка при удалении', 'error')
        }
    }

    const handleCreate = async (data) => {
        try {
            await createDataSource(data)
            await loadDataSources()
            showNotification('Источник создан', 'success')
        } catch (e) {
            console.error(e)
            showNotification('Ошибка при создании', 'error')
        }
    }

    return (
        <Container sx={{ mt: 4 }}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
                <Typography variant="h5">Управление источниками данных</Typography>
                <Button
                    variant="contained"
                    startIcon={<AddIcon />}
                    onClick={() => setDialogOpen(true)}
                >
                    Добавить источник
                </Button>
            </Box>

            <DataSourceTable dataSources={dataSources} onDelete={handleDelete} />
            <CreateDataSourceDialog
                open={dialogOpen}
                onClose={() => setDialogOpen(false)}
                onCreate={handleCreate}
            />
        </Container>
    )
}