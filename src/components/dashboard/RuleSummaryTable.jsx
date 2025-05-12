import React, { useState } from "react";
import {
    Paper, Typography, Table, TableHead, TableRow, TableCell,
    TableBody, TablePagination, Popover, Box, CircularProgress, Chip
} from "@mui/material";
import { fetchExpectationById } from "../../api/expectationApi.js";

export default function RuleSummaryTable({ data }) {
    const [page, setPage] = useState(0);
    const [rowsPerPage] = useState(10);

    const [anchorEl, setAnchorEl] = useState(null);
    const [popupContent, setPopupContent] = useState(null);
    const [loading, setLoading] = useState(false);
    const [cache, setCache] = useState({});

    const handleClick = async (event, id) => {
        setAnchorEl(event.currentTarget);

        if (cache[id]) {
            setPopupContent(cache[id]);
            return;
        }

        setLoading(true);
        setPopupContent(null);

        try {
            const rule = await fetchExpectationById(id);
            setPopupContent(rule);
            setCache(prev => ({ ...prev, [id]: rule }));
        } catch (e) {
            console.error(e);
            setPopupContent({ error: "Не удалось загрузить правило" });
        } finally {
            setLoading(false);
        }
    };

    const handleClose = () => {
        setAnchorEl(null);
        setPopupContent(null);
    };

    const open = Boolean(anchorEl);
    const visibleRows = data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);

    return (
        <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>Все правила и их статистика</Typography>

            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>Rule ID</TableCell>
                        <TableCell>Поле</TableCell>
                        <TableCell>Тип</TableCell>
                        <TableCell>Ошибок</TableCell>
                        <TableCell>Всего</TableCell>
                        <TableCell>%</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {visibleRows.map((r) => (
                        <TableRow key={r.expectationId}>
                            <TableCell
                                onClick={(e) => handleClick(e, r.expectationId)}
                                sx={{ cursor: "pointer", color: "primary.main", textDecoration: "underline" }}
                            >
                                {r.expectationId}
                            </TableCell>
                            <TableCell>{r.field}</TableCell>
                            <TableCell>{r.expectationType}</TableCell>
                            <TableCell>{r.failed}</TableCell>
                            <TableCell>{r.total}</TableCell>
                            <TableCell>{r.rate}%</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>

            <TablePagination
                component="div"
                count={data.length}
                page={page}
                onPageChange={(e, newPage) => setPage(newPage)}
                rowsPerPage={rowsPerPage}
                rowsPerPageOptions={[10]}
            />

            <Popover
                open={open}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
                transformOrigin={{ vertical: 'top', horizontal: 'left' }}
            >
                <Box sx={{ p: 2, maxWidth: 350, minWidth: 280 }}>
                    {loading ? (
                        <CircularProgress size={20} />
                    ) : popupContent?.error ? (
                        <Typography color="error">{popupContent.error}</Typography>
                    ) : popupContent ? (
                        <>
                            <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 600 }}>
                                {popupContent.expectationCode}
                            </Typography>

                            {popupContent.description && (
                                <Typography variant="body2" gutterBottom>
                                    {popupContent.description}
                                </Typography>
                            )}

                            <Box mb={1}>
                                <Chip
                                    label={`Severity: ${popupContent.severity}`}
                                    color={
                                        popupContent.severity === "CRITICAL" ? "error" :
                                            popupContent.severity === "WARNING" ? "warning" : "default"
                                    }
                                    size="small"
                                    variant="outlined"
                                />
                            </Box>

                            <Box mb={1}>
                                <Typography variant="caption" fontWeight={600}>Параметры:</Typography>
                                <ul style={{ margin: 0, paddingLeft: 20 }}>
                                    {Object.entries(popupContent.kwargs || {}).map(([key, value]) => (
                                        <li key={key}>
                                            <Typography variant="body2">
                                                {key}: {String(value)}
                                            </Typography>
                                        </li>
                                    ))}
                                </ul>
                            </Box>

                            {popupContent.rowCondition && (
                                <Box mb={1}>
                                    <Typography variant="caption" fontWeight={600}>Условие применения:</Typography>
                                    <Typography variant="body2" sx={{ fontStyle: 'italic' }}>
                                        {popupContent.rowCondition}
                                    </Typography>
                                </Box>
                            )}

                            <Box>
                                <Typography variant="caption" color="text.secondary">
                                    Создано: {new Date(popupContent.createdAt).toLocaleString()}
                                </Typography>
                                <Typography variant="caption" color={popupContent.enabled ? "success.main" : "text.disabled"} display="block">
                                    {popupContent.enabled ? "Включено" : "Отключено"}
                                </Typography>
                            </Box>
                        </>
                    ) : (
                        <Typography variant="body2">Нет данных</Typography>
                    )}
                </Box>
            </Popover>
        </Paper>
    );
}