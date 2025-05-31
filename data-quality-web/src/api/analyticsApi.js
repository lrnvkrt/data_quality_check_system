const API_BASE = "/api/analytics";

export async function fetchTopics() {
    const res = await fetch(`${API_BASE}/topics`);
    if (!res.ok) throw new Error("Ошибка загрузки списка топиков");
    const data = await res.json();
    return data.map(summary => summary.topic);
}

export async function fetchTopicSummaries() {
    const res = await fetch(`${API_BASE}/topics`);
    if (!res.ok) throw new Error("Ошибка загрузки сводной информации по топикам");
    return await res.json();
}

export async function fetchFieldFailures(topic) {
    const res = await fetch(`${API_BASE}/topics/${topic}/fields`);
    if (!res.ok) throw new Error("Ошибка загрузки ошибок по полям и правилам");
    return await res.json();
}

export async function fetchFailureTrend(topic, interval = "hourly") {
    const res = await fetch(`${API_BASE}/topics/${topic}/trend?interval=${interval}`);
    if (!res.ok) throw new Error("Ошибка загрузки тренда по ошибкам");
    return await res.json();
}

export async function fetchFailureExamples(topic) {
    const res = await fetch(`${API_BASE}/topics/${topic}/examples`);
    if (!res.ok) throw new Error("Ошибка загрузки примеров значений с ошибками");
    return await res.json();
}

export async function fetchRuleSummaries() {
    const res = await fetch(`${API_BASE}/rules`);
    if (!res.ok) throw new Error("Ошибка загрузки правил");
    return await res.json();
}

export async function fetchTopicErrorStats() {
    const res = await fetch(`${API_BASE}/topics/errors`);
    if (!res.ok) throw new Error("Ошибка загрузки ошибок по топикам");
    return await res.json();
}

export async function fetchGlobalStats() {
    const res = await fetch(`${API_BASE}/topics/total`);
    if (!res.ok) throw new Error("Ошибка загрузки общей статистики");
    return await res.json();
}