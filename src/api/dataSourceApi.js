const API_BASE = '/api'

export async function fetchDataSources() {
    const res = await fetch(`${API_BASE}/datasources`)
    if (!res.ok) throw new Error('Ошибка при получении DataSource')
    return await res.json()
}

export async function deleteDataSource(id) {
    const res = await fetch(`${API_BASE}/datasources/${id}`, {
        method: 'DELETE'
    })
    if (!res.ok) throw new Error('Ошибка при удалении DataSource')
}

export async function createDataSource(data) {
    const res = await fetch(`${API_BASE}/datasources`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    if (!res.ok) throw new Error('Ошибка при создании DataSource')
    return await res.json()
}