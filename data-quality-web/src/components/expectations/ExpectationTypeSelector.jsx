import { Grid, TextField, MenuItem } from '@mui/material'

export default function ExpectationTypeSelector({ catalog, value, onChange, fieldType }) {
    const isNumericField = fieldType === 'int' || fieldType === 'float'

    const filteredCatalog = catalog.filter((item) => {
        return isNumericField || !item.requiresNumeric
    })

    return (
        <Grid item xs={12} sm={5}>
            <TextField
                select
                label="Тип правила"
                fullWidth
                value={value}
                onChange={(e) => onChange(e.target.value)}
                sx={{ minWidth: 360 }}
            >
                {filteredCatalog.map((item) => (
                    <MenuItem key={item.id} value={item.id}>
                        {item.name}
                    </MenuItem>
                ))}
            </TextField>
        </Grid>
    )
}