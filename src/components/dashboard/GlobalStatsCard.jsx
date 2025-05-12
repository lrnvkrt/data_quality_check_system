import React from "react";
import {Box, Paper, Stack, Typography} from "@mui/material";
import {
    PieChart,
    Pie,
    Cell,
    Tooltip,
    ResponsiveContainer,
    Legend
} from "recharts";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import DoneIcon from "@mui/icons-material/Done";

const COLORS = ["#4caf50", "#f44336"];

export default function GlobalStatsCard({ stats }) {
    const { errorCount, checkCount } = stats;
    const successCount = checkCount - errorCount;

    const pieData = [
        { name: "Успешно", value: successCount },
        { name: "Ошибки", value: errorCount }
    ];

    return (
        <Paper sx={{ p: 3, width: "100%", height: "100%" }}>
            <Typography variant="h6" gutterBottom>Общая статистика</Typography>
            <ResponsiveContainer width="100%" height={220}>
                <PieChart margin={{ top: 20, right: 20, left: 20 }}>
                    <Pie
                        data={pieData}
                        dataKey="value"
                        nameKey="name"
                        cx="50%"
                        cy="50%"
                        innerRadius={50}
                        outerRadius={80}
                        isAnimationActive={false}
                    >
                        {pieData.map((_, i) => (
                            <Cell key={i} fill={COLORS[i % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip
                        formatter={(value, name) => [`${value}`, name]}
                        contentStyle={{
                            padding: "8px 12px",
                            borderRadius: 8,
                            boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
                        }}
                    />
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
        </Paper>
    );
}