import { useEffect, useState } from 'react';
import { Grid, Button, Typography, Box } from '@mui/material';
import { AddCircle } from '@mui/icons-material';
import { fetchFieldsByDataSource } from '../../api/fieldApi.js';
import ConditionGroup from './ConditionGroup';

const STRING_OPERATORS = [
    { label: '=', value: '==' },
    { label: '≠', value: '!=' },
];

const NUMBER_OPERATORS = [
    ...STRING_OPERATORS,
    { label: '<', value: '<' },
    { label: '≤', value: '<=' },
    { label: '>', value: '>' },
    { label: '≥', value: '>=' },
];

const LOGICAL_OPERATORS = [
    { label: 'AND', value: 'and' },
    { label: 'OR', value: 'or' },
];

export default function RowConditionBuilder({ dataSourceId, value, onChange }) {
    const [fields, setFields] = useState([]);
    const [groups, setGroups] = useState([]);

    useEffect(() => {
        fetchFieldsByDataSource(dataSourceId).then(setFields).catch(console.error);
    }, [dataSourceId]);

    useEffect(() => {
        const conditionStr = groups
            .filter(g => g.conditions.length)
            .map((group, i) => {
                const groupStr = group.conditions.map((c, j) => {
                    const fieldMeta = fields.find(f => f.name === c.field);
                    const val = fieldMeta?.dataType === 'string' ? `"${c.value}"` : c.value;
                    const expr = `${c.field} ${c.operator} ${val}`;
                    return j > 0 ? `${c.logical || 'and'} ${expr}` : expr;
                }).join(' ');
                return i > 0 ? `${group.logical || 'and'} (${groupStr})` : `(${groupStr})`;
            }).join(' ');

        onChange(conditionStr);
    }, [groups, fields]);

    const updateGroupCondition = (groupIndex, condIndex, key, val) => {
        const updated = [...groups];
        updated[groupIndex].conditions[condIndex] = {
            ...updated[groupIndex].conditions[condIndex],
            [key]: val,
        };
        setGroups(updated);
    };

    const addGroup = () => {
        setGroups([...groups, { logical: 'and', conditions: [{ field: '', operator: '==', value: '', logical: 'and' }] }]);
    };

    const addConditionToGroup = (groupIndex) => {
        const updated = [...groups];
        updated[groupIndex].conditions.push({ field: '', operator: '==', value: '', logical: 'and' });
        setGroups(updated);
    };

    const removeCondition = (groupIndex, condIndex) => {
        const updated = [...groups];
        updated[groupIndex].conditions.splice(condIndex, 1);
        setGroups(updated);
    };

    const updateGroupLogic = (groupIndex, val) => {
        const updated = [...groups];
        updated[groupIndex].logical = val;
        setGroups(updated);
    };

    const getOperatorsForField = (fieldName) => {
        const field = fields.find(f => f.name === fieldName);
        if (!field) return STRING_OPERATORS;
        return field.dataType === 'string' ? STRING_OPERATORS : NUMBER_OPERATORS;
    };

    return (
        <Box sx={{ mt: 2 }}>
            {groups.map((group, gi) => (
                <Box key={gi} sx={{ mb: 4 }}>
                    <ConditionGroup
                        index={gi}
                        group={group}
                        fields={fields}
                        onUpdateGroupLogic={updateGroupLogic}
                        onUpdateCondition={updateGroupCondition}
                        onAddCondition={addConditionToGroup}
                        onRemoveCondition={removeCondition}
                        getOperatorsForField={getOperatorsForField}
                    />
                </Box>
            ))}
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Button variant="outlined" startIcon={<AddCircle />} onClick={addGroup}>
                        Добавить группу условий
                    </Button>
                </Grid>
                {value && (
                    <Grid item xs={12}>
                        <Typography variant="body2" color="text.secondary">Сформированное выражение:</Typography>
                        <Typography variant="body1" sx={{ mt: 1, whiteSpace: 'pre-wrap' }}>{value}</Typography>
                    </Grid>
                )}
            </Grid>
        </Box>
    );
}