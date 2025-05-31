import React from "react";
import { Box, Autocomplete, TextField } from "@mui/material";

export default function TopicSelector({ topics, selected, onChange }) {
    return (
        <Box mb={3}>
            <Autocomplete
                options={topics}
                value={selected || null}
                onChange={(event, newValue) => onChange(newValue)}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        label="Выберите топик"
                        variant="filled"
                        fullWidth
                    />
                )}
                fullWidth
                disableClearable
                autoHighlight
                isOptionEqualToValue={(option, value) => option === value}
                noOptionsText="Нет совпадений"
            />
        </Box>
    );
}