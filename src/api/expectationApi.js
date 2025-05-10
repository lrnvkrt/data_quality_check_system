const API_BASE = '/api'

export async function fetchExpectationCatalog() {
    const res = await fetch(`${API_BASE}/expectations/catalog`)
    if (!res.ok) throw new Error('Ошибка загрузки справочника ожиданий')
    return await res.json()
}

export async function fetchFieldExpectations(fieldId) {
    const res = await fetch(`${API_BASE}/fields/${fieldId}/expectations`)
    if (!res.ok) throw new Error('Ошибка загрузки правил поля')
    return await res.json()
}

export async function createFieldExpectation(fieldId, payload) {
    const res = await fetch(`${API_BASE}/fields/${fieldId}/expectations`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    if (!res.ok) throw new Error('Ошибка при создании правила')
    return await res.json()
}

export async function deleteExpectation(expectationId) {
    const res = await fetch(`${API_BASE}/expectations/${expectationId}`, { method: 'DELETE' })
    if (!res.ok) throw new Error('Ошибка при удалении')
}

export async function toggleExpectation(expectationId) {
    const res = await fetch(`${API_BASE}/expectations/${expectationId}/toggle`, { method: 'POST' })
    if (!res.ok) throw new Error('Ошибка при переключении состояния')
}

export async function fetchExpectationById(id) {
    const res = await fetch(`${API_BASE}/expectations/${id}`);
    if (!res.ok) throw new Error("Ошибка загрузки описания правила");
    return await res.json();
}