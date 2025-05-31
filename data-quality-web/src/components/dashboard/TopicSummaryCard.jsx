import React from "react";
import { Paper, Typography, Box, Grid, Chip, Stack } from "@mui/material";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import DoneIcon from '@mui/icons-material/Done';

const COLORS = ["#4caf50", "#f44336"];

export default function TopicSummaryCard({ topic, summary }) {
    if (!summary) return null;

    const { totalRows, failedRows, errorRate } = summary;
    const passedRows = totalRows - failedRows;

    const pieData = [
        { name: "Прошли", value: passedRows },
        { name: "Ошибки", value: failedRows }
    ];

    return (
        <Paper sx={{ p: 3, mb: 3, maxWidth: 600, height: 400, mx: "auto" }}>
            <Typography variant="h6" gutterBottom>
                Сводка по топику: <strong>{topic}</strong>
            </Typography>

            <Grid container spacing={2} alignItems="center">
                <Grid item xs={12} md={6}>
                    <Box display="flex" flexDirection="column" gap={1}>
                        <Typography variant="h6">Всего строк: <strong>{totalRows}</strong></Typography>
                        <Typography color="error" variant="h5">Ошибок: <strong>{failedRows}</strong></Typography>
                        <Typography variant="h6">
                            Процент ошибок:{" "}
                            <Chip
                                label={`${errorRate}%`}
                                color={errorRate > 20 ? "error" : errorRate > 5 ? "warning" : "success"}
                                variant="outlined"
                            />
                        </Typography>
                    </Box>
                </Grid>

                <Grid item xs={12} md={6}>
                    <ResponsiveContainer width={250} height={270}>
                        <PieChart>
                            <Pie
                                data={pieData}
                                cx="50%"
                                cy="50%"
                                innerRadius={80}
                                outerRadius={100}
                                dataKey="value"
                            >
                                {pieData.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={COLORS[index]} />
                                ))}
                            </Pie>
                            <Tooltip />
                        </PieChart>
                    </ResponsiveContainer>

                    <Stack direction="row" spacing={2} justifyContent="center" mt={1}>
                        <Box display="flex" alignItems="center" gap={0.5}>
                            <DoneIcon fontSize="small" color="success" />
                            <Typography variant="body2">Успешно</Typography>
                        </Box>
                        <Box display="flex" alignItems="center" gap={0.5}>
                            <ErrorOutlineIcon fontSize="small" color="error" />
                            <Typography variant="body2">Ошибки</Typography>
                        </Box>
                    </Stack>
                </Grid>
            </Grid>
        </Paper>
    );
}