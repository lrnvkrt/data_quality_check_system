import React from "react";
import { Box, Paper, Stack, Typography } from "@mui/material";
import {
    PieChart,
    Pie,
    Cell,
    Tooltip,
    ResponsiveContainer
} from "recharts";
import CircleIcon from "@mui/icons-material/Circle";

import {
    blue,
    green,
    purple,
    orange,
    teal,
    cyan,
    pink,
    indigo,
    amber,
    deepOrange,
    lightGreen,
    lime,
    deepPurple
} from "@mui/material/colors";

const MATERIAL_COLORS = [
    blue[500],
    green[500],
    purple[500],
    orange[500],
    teal[500],
    cyan[500],
    pink[500],
    indigo[500],
    amber[500],
    deepOrange[500],
    lightGreen[500],
    lime[500],
    deepPurple[500]
];

export default function TopicErrorPieChart({ data }) {
    return (
        <Paper sx={{ p: 3, width: "100%", height: "100%" }}>
            <Typography variant="h6" gutterBottom>Ошибки по топикам</Typography>

            <ResponsiveContainer width="100%" height={220}>
                <PieChart margin={{ right: 20, top: 20, left: 20 }}>
                    <Pie
                        data={data}
                        dataKey="errorCount"
                        nameKey="topic"
                        cx="50%"
                        cy="50%"
                        outerRadius={80}
                        innerRadius={50}
                        isAnimationActive={false}
                    >
                        {data.map((_, i) => (
                            <Cell key={i} fill={MATERIAL_COLORS[i % MATERIAL_COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip
                        formatter={(value) => [`${value}`, "Ошибок"]}
                        contentStyle={{
                            padding: "8px 12px",
                            borderRadius: 8,
                            boxShadow: "0 2px 8px rgba(0,0,0,0.15)"
                        }}
                    />
                </PieChart>
            </ResponsiveContainer>

            <Stack direction="row" spacing={3} justifyContent="center" flexWrap="wrap" mt={1}>
                {data.map((entry, i) => (
                    <Box key={entry.topic} display="flex" alignItems="center" gap={0.75}>
                        <CircleIcon fontSize="small" sx={{ color: MATERIAL_COLORS[i % MATERIAL_COLORS.length] }} />
                        <Typography variant="body1">
                            {entry.topic}
                        </Typography>
                    </Box>
                ))}
            </Stack>
        </Paper>
    );
}
