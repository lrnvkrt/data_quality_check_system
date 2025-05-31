import React, { useState } from 'react'
import Header from './Header'
import SidebarDrawer from './SidebarDrawer'

export default function Navbar() {
    const [drawerOpen, setDrawerOpen] = useState(false)

    const openDrawer = () => setDrawerOpen(true)
    const closeDrawer = () => setDrawerOpen(false)

    return (
        <>
            <Header onMenuClick={openDrawer} />
            <SidebarDrawer open={drawerOpen} onClose={closeDrawer} />
        </>
    )
}