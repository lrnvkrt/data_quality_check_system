import React from 'react'
import { Routes, Route } from 'react-router-dom'
import Navbar from './components/layout/Navbar'
import DataSourceManagementPage from "./pages/DataSourceManagmentPage.jsx";

export default function App() {
    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/datasource/manage" element={<DataSourceManagementPage />} />
            </Routes>
        </>
    )
}