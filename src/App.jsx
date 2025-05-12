import React from 'react'
import { Routes, Route } from 'react-router-dom'
import Navbar from './components/layout/Navbar'
import DashboardPage from './pages/DashboardPage'
import ExpectationManagementPage from './pages/ExpectationManagementPage'
import DataSourceManagementPage from "./pages/DataSourceManagmentPage.jsx";

export default function App() {
    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/" element={<DashboardPage />} />
                <Route path="/expectations/manage" element={<ExpectationManagementPage />} />
                <Route path="/datasource/manage" element={<DataSourceManagementPage />} />
            </Routes>
        </>
    )
}