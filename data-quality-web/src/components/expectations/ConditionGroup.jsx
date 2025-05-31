import {
    Grid,
    Paper,
    TextField,
    MenuItem,
    IconButton,
    Button,
    Typography,
} from '@mui/material';
import { Delete } from '@mui/icons-material';

const LOGICAL_OPERATORS = [
    { label: 'AND', value: 'and' },
    { label: 'OR', value: 'or' },
];

export default function ConditionGroup({
                                           index,
                                           group,
                                           fields,
                                           onUpdateGroupLogic,
                                           onUpdateCondition,
                                           onAddCondition,
                                           onRemoveCondition,
                                           getOperatorsForField,
                                       }) {
    return (
        <Grid item xs={12}>
            {index > 0 && (
                <TextField
                    select
                    label="Логика между группами"
                    value={group.logical}
                    onChange={(e) => onUpdateGroupLogic(index, e.target.value)}
                    sx={{ mb: 1 }}
                    size="small"
                >
                    {LOGICAL_OPERATORS.map((op) => (
                        <MenuItem key={op.value} value={op.value}>
                            {op.label}
                        </MenuItem>
                    ))}
                </TextField>
            )}

            <Paper variant="outlined" sx={{ p: 2 }}>
                <Typography variant="subtitle2" gutterBottom>
                    Группа {index + 1}
                </Typography>

                {group.conditions.map((cond, ci) => (
                    <Grid
                        container
                        spacing={2}
                        alignItems="center"
                        key={ci}
                        sx={{ mb: 1 }}
                    >
                        {ci > 0 && (
                            <Grid item xs={12} sm={2}>
                                <TextField
                                    select
                                    label="Логика"
                                    fullWidth
                                    value={cond.logical || 'and'}
                                    onChange={(e) =>
                                        onUpdateCondition(index, ci, 'logical', e.target.value)
                                    }
                                    size="small"
                                >
                                    {LOGICAL_OPERATORS.map((op) => (
                                        <MenuItem key={op.value} value={op.value}>
                                            {op.label}
                                        </MenuItem>
                                    ))}
                                </TextField>
                            </Grid>
                        )}

                        <Grid item xs={12} sm={3}>
                            <TextField
                                select
                                label="Поле"
                                fullWidth
                                value={cond.field}
                                onChange={(e) =>
                                    onUpdateCondition(index, ci, 'field', e.target.value)
                                }
                                size="small"
                            >
                                {fields.map((f) => (
                                    <MenuItem key={f.id} value={f.name}>
                                        {f.name}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </Grid>

                        <Grid item xs={12} sm={2}>
                            <TextField
                                select
                                label="Оператор"
                                fullWidth
                                value={cond.operator}
                                onChange={(e) =>
                                    onUpdateCondition(index, ci, 'operator', e.target.value)
                                }
                                size="small"
                            >
                                {getOperatorsForField(cond.field).map((op) => (
                                    <MenuItem key={op.value} value={op.value}>
                                        {op.label}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </Grid>

                        <Grid item xs={12} sm={4}>
                            <TextField
                                label="Значение"
                                fullWidth
                                value={cond.value}
                                onChange={(e) =>
                                    onUpdateCondition(index, ci, 'value', e.target.value)
                                }
                                size="small"
                            />
                        </Grid>

                        <Grid item xs={12} sm={1}>
                            <IconButton onClick={() => onRemoveCondition(index, ci)}>
                                <Delete />
                            </IconButton>
                        </Grid>
                    </Grid>
                ))}

                <Button size="small" onClick={() => onAddCondition(index)}>
                    Добавить условие
                </Button>
            </Paper>
        </Grid>
    );
}