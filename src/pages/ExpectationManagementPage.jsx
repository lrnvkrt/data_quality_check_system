import React, { useState } from 'react'
import { Container, Typography } from '@mui/material'
import DataSourceSelector from '../components/expectations/DataSourceSelector.jsx'
import FieldExpectationTable from '../components/expectations/FieldExpectationTable'

export default function ExpectationManagementPage() {
    const [selectedDataSourceId, setSelectedDataSourceId] = useState(null)

    return (
        <Container sx={{ mt: 4 }}>
            <Typography variant="h5" gutterBottom>
                Управление проверками по полям
            </Typography>

            <DataSourceSelector
                selected={selectedDataSourceId}
                onChange={setSelectedDataSourceId}
            />

            <FieldExpectationTable dataSourceId={selectedDataSourceId} />
        </Container>
    )
}