const API_BASE = '/api'

export async function getFields(dataSourceId) {
    const res = await fetch(`${API_BASE}/datasources/${dataSourceId}/fields`)
    if (!res.ok) throw new Error('Ошибка загрузки полей')
    return await res.json()
}

export async function createField(dataSourceId, field) {
    const res = await fetch(`${API_BASE}/datasources/${dataSourceId}/fields`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(field)
    })
    if (!res.ok) throw new Error('Ошибка при создании поля')
}

export async function deleteField(dataSourceId, fieldId) {
    const res = await fetch(`${API_BASE}/datasources/${dataSourceId}/fields/${fieldId}`, {
        method: 'DELETE'
    })
    if (!res.ok) throw new Error('Ошибка при удалении поля')
}

export async function fetchFieldsByDataSource(dataSourceId) {
    const res = await fetch(`${API_BASE}/datasources/${dataSourceId}/fields`)
    if (!res.ok) throw new Error('Ошибка загрузки полей')
    return await res.json()
}
