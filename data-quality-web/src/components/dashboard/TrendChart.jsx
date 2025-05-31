import React from "react";
import {
    Paper, Typography, Box, useTheme, FormControl,
    InputLabel, Select, MenuItem
} from "@mui/material";
import {
    LineChart, Line, XAxis, YAxis, Tooltip,
    CartesianGrid, Legend, ResponsiveContainer
} from "recharts";
import dayjs from "dayjs";

export default function TrendChart({ data, interval, onIntervalChange }) {
    const theme = useTheme();

    return (
        <Paper sx={{ p: 3, mb: 3, height: 400 }}>
            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
                <Typography variant="h6">Тренд ошибок во времени</Typography>
                <FormControl size="small" sx={{ minWidth: 150 }}>
                    <InputLabel id="interval-select-label">Интервал</InputLabel>
                    <Select
                        labelId="interval-select-label"
                        value={interval}
                        label="Интервал"
                        onChange={(e) => onIntervalChange(e.target.value)}
                     variant={"filled"}>
                        <MenuItem value="minutely">Поминутный</MenuItem>
                        <MenuItem value="hourly">Почасовой</MenuItem>
                        <MenuItem value="daily">По дням</MenuItem>
                        <MenuItem value="weekly">По неделям</MenuItem>
                    </Select>
                </FormControl>
            </Box>

            <Box sx={{ minWidth: 600, width: "100%", height: 330 }}>
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart
                        data={data}
                        margin={{ top: 20, right: 40, left: 0, bottom: 20 }}
                    >
                        <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                        <XAxis
                            dataKey="timestamp"
                            tickFormatter={(value) => dayjs(value).format("DD.MM HH:mm")}
                            angle={-30}
                            textAnchor="end"
                            height={60}
                        />
                        <YAxis
                            tickFormatter={(value) => `${value}%`}
                            domain={[0, 'auto']}
                        />
                        <Tooltip
                            labelFormatter={(label) => `Время: ${dayjs(label).format("DD.MM.YYYY HH:mm")}`}
                            formatter={(value) => [`${value}%`, "Ошибка"]}
                            contentStyle={{ backgroundColor: theme.palette.background.paper }}
                        />
                        <Legend verticalAlign="top" height={30} />
                        <Line
                            type="monotone"
                            dataKey="failRate"
                            name="Ошибка (%)"
                            stroke="#d32f2f"
                            strokeWidth={2}
                            dot={{ r: 3 }}
                            activeDot={{ r: 6 }}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </Box>
        </Paper>
    );
}