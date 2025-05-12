import React from "react";
import { Paper, Typography, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

export default function FieldFailureTable({ data }) {
    return (
        <Paper sx={{ p: 2 }}>
            <Typography variant="h6">Ошибки по полям и правилам</Typography>
            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>Поле</TableCell>
                        <TableCell>Тип правила</TableCell>
                        <TableCell>Ошибок</TableCell>
                        <TableCell>%</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data.map((f, i) => (
                        <TableRow key={i}>
                            <TableCell>{f.field}</TableCell>
                            <TableCell>{f.expectationType}</TableCell>
                            <TableCell>{f.failed}</TableCell>
                            <TableCell>{f.rate}%</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Paper>
    );
}