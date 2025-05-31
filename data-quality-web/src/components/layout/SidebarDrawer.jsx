import React, { useState } from "react";
import {
    Drawer,
    Box,
    Typography,
    Divider,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Dialog,
    DialogTitle,
    DialogActions,
    Button
} from "@mui/material";

import DashboardIcon from "@mui/icons-material/Dashboard";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import Dns from "@mui/icons-material/Dns";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";

import { useLocation, useNavigate } from "react-router-dom";

const grafanaUrl = import.meta.env.VITE_GRAFANA_URL;

const menuItems = [
    {
        label: "Аналитика",
        path: "/",
        icon: <DashboardIcon />
    },
    {
        label: "Управление топиками",
        path: "/datasource/manage",
        icon: <Dns />
    },
    {
        label: "Управление проверками",
        path: "/expectations/manage",
        icon: <CheckCircleIcon />
    }
];

export default function SidebarDrawer({ open, onClose }) {
    const navigate = useNavigate();
    const location = useLocation();
    const [confirmOpen, setConfirmOpen] = useState(false);

    const isActive = (path) => location.pathname === path;

    const handleGrafanaClick = () => {
        setConfirmOpen(true);
    };

    const confirmExternal = () => {
        window.open(grafanaUrl, "_blank", "noopener,noreferrer");
        setConfirmOpen(false);
        onClose();
    };

    return (
        <>
            <Drawer anchor="left" open={open} onClose={onClose}>
                <Box
                    sx={{
                        width: 280,
                        height: "100%",
                        display: "flex",
                        flexDirection: "column"
                    }}
                >
                    <Box sx={{ p: 2 }}>
                        <Typography variant="h6" fontWeight="bold">
                            DQ Service
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Контроль качества данных
                        </Typography>
                    </Box>

                    <Divider />

                    <List sx={{ mt: 1 }}>
                        {menuItems.map((item) => {
                            const active = isActive(item.path);
                            return (
                                <ListItemButton
                                    key={item.path}
                                    onClick={() => {
                                        navigate(item.path);
                                        onClose();
                                    }}
                                    sx={{
                                        mx: 1,
                                        mb: 1,
                                        border: active ? "2px solid #1976d2" : "2px solid transparent",
                                        borderRadius: 2,
                                        backgroundColor: active ? "rgba(25, 118, 210, 0.05)" : "transparent",
                                        transition: "all 0.2s ease-in-out",
                                        "&:hover": {
                                            backgroundColor: "action.hover"
                                        }
                                    }}
                                >
                                    <ListItemIcon
                                        sx={{
                                            minWidth: 36,
                                            color: active ? "primary.main" : "inherit"
                                        }}
                                    >
                                        {item.icon}
                                    </ListItemIcon>
                                    <ListItemText
                                        primary={item.label}
                                        primaryTypographyProps={{
                                            fontWeight: active ? 600 : 400,
                                            color: active ? "primary.main" : "inherit"
                                        }}
                                    />
                                </ListItemButton>
                            );
                        })}

                        <ListItemButton
                            onClick={handleGrafanaClick}
                            sx={{
                                mx: 1,
                                mb: 1,
                                border: "2px solid transparent",
                                borderRadius: 2,
                                transition: "all 0.2s ease-in-out",
                                "&:hover": {
                                    backgroundColor: "action.hover"
                                }
                            }}
                        >
                            <ListItemIcon sx={{ minWidth: 36 }}>
                                <OpenInNewIcon />
                            </ListItemIcon>
                            <ListItemText
                                primary="Мониторинг"
                                primaryTypographyProps={{
                                    fontWeight: 500
                                }}
                            />
                        </ListItemButton>
                    </List>
                </Box>
            </Drawer>

            <Dialog open={confirmOpen} onClose={() => setConfirmOpen(false)}>
                <DialogTitle>Вы переходите на внешний ресурс (Grafana)</DialogTitle>
                <DialogActions>
                    <Button onClick={() => setConfirmOpen(false)}>Отмена</Button>
                    <Button
                        onClick={confirmExternal}
                        variant="contained"
                        color="primary"
                        autoFocus
                    >
                        Перейти
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
}