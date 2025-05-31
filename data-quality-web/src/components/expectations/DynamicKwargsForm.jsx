import { Grid, TextField, MenuItem } from '@mui/material'

export default function DynamicKwargsForm({ kwargs, onChange, allowedArgsType }) {
    return (
        <Grid container spacing={2} alignItems="center" sx={{ mt: 3 }}>
            {Object.entries(allowedArgsType).map(([arg, type]) => {
                const value = kwargs[arg] ?? ''

                if (type === 'bool') {
                    return (
                        <Grid item xs={12} sm={4} key={arg}>
                            <TextField
                                select
                                label={`Аргумент: ${arg}`}
                                fullWidth
                                sx={{ minWidth: 200 }}
                                value={value}
                                onChange={(e) => onChange((prev) => ({ ...prev, [arg]: e.target.value }))}
                            >
                                <MenuItem value="true">true</MenuItem>
                                <MenuItem value="false">false</MenuItem>
                            </TextField>
                        </Grid>
                    )
                }

                if (type === 'list') {
                    return (
                        <Grid item xs={12} sm={6} key={arg}>
                            <TextField
                                label={`Аргумент: ${arg}`}
                                fullWidth
                                multiline
                                minRows={3}
                                value={value}
                                onChange={(e) => onChange((prev) => ({ ...prev, [arg]: e.target.value }))}
                                placeholder="Каждое значение — с новой строки"
                            />
                        </Grid>
                    )
                }

                return (
                    <Grid item xs={12} sm={4} key={arg}>
                        <TextField
                            label={`Аргумент: ${arg}`}
                            fullWidth
                            type={type === 'int' || type === 'float' ? 'number' : 'text'}
                            value={value}
                            onChange={(e) => onChange((prev) => ({ ...prev, [arg]: e.target.value }))}
                        />
                    </Grid>
                )
            })}
        </Grid>
    )
}