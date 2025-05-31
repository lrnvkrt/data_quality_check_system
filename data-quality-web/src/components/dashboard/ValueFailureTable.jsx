import React from "react";
import {
    Paper,
    Typography,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Box,
    Chip
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";

export default function ValueFailureTable({ data }) {
    const grouped = data.reduce((acc, item) => {
        if (!acc[item.field]) acc[item.field] = [];
        acc[item.field].push(item);
        return acc;
    }, {});

    return (
        <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
                Частые ошибочные значения по полям
            </Typography>

            {Object.entries(grouped).map(([field, entries], i) => (
                <Accordion key={i} defaultExpanded={i === 0}>
                    <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                        <Box display="flex" alignItems="center" gap={1}>
                            <ErrorOutlineIcon color="error" fontSize="small" />
                            <Typography variant="subtitle1">
                                Поле: <strong>{field}</strong>
                            </Typography>
                            <Chip label={`${entries.length} значений`} size="small" />
                        </Box>
                    </AccordionSummary>
                    <AccordionDetails>
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell sx={{ width: 240 }}>Значение</TableCell>
                                    <TableCell>Описание</TableCell>
                                    <TableCell sx={{ width: 60, textAlign: "center" }}>Ошибок</TableCell>
                                    <TableCell sx={{ width: 400 }}>Сообщение</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {entries.map((ex, idx) => (
                                    <TableRow key={idx}>
                                        <TableCell sx={{ width: 240, p: 1 }}>
                                            <Box
                                                sx={{
                                                    maxWidth: 240,
                                                    overflowX: "auto",
                                                    whiteSpace: "nowrap"
                                                }}
                                            >
                                                {ex.value}
                                            </Box>
                                        </TableCell>

                                        <TableCell sx={{ width: 250, p: 1 }}>
                                            <Box sx={{
                                                whiteSpace: "normal",
                                                wordBreak: "break-word"
                                            }}>
                                                {ex.description}
                                            </Box>
                                        </TableCell>

                                        <TableCell sx={{ width: 60, textAlign: "center", p: 1 }}>
                                            {ex.count}
                                        </TableCell>

                                        <TableCell sx={{ width: 400, p: 1 }}>
                                            <Box
                                                sx={{
                                                    maxWidth: 400,
                                                    overflowX: "auto",
                                                    whiteSpace: "nowrap"
                                                }}
                                            >
                                                {ex.sampleError}
                                            </Box>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </AccordionDetails>
                </Accordion>
            ))}
        </Paper>
    );
}
