import { Grid, TextField, MenuItem } from '@mui/material'

export default function SeveritySelector({ value, onChange }) {
    return (
        <Grid item xs={6} sm={2}>
            <TextField
                select
                label="Severity"
                fullWidth
                value={value}
                onChange={(e) => onChange(e.target.value)}
            >
                <MenuItem value="INFO">INFO</MenuItem>
                <MenuItem value="WARNING">WARNING</MenuItem>
                <MenuItem value="CRITICAL">CRITICAL</MenuItem>
            </TextField>
        </Grid>
    )
}