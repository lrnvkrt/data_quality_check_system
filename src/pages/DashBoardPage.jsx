import React, { useEffect, useState } from "react";
import {Box, Typography, Grid, Divider, Stack, Paper} from "@mui/material";
import {
    fetchTopics,
    fetchTopicSummaries,
    fetchFailureTrend,
    fetchFailureExamples,
    fetchRuleSummaries,
    fetchTopicErrorStats,
    fetchGlobalStats
} from "../api/analyticsApi";

import TopicSelector from "../components/dashboard/TopicSelector.jsx";
import TopicSummaryCard from "../components/dashboard/TopicSummaryCard.jsx";
import TrendChart from "../components/dashboard/TrendChart";
import ValueFailureTable from "../components/dashboard/ValueFailureTable";
import RuleSummaryTable from "../components/dashboard/RuleSummaryTable";
import TopicErrorPieChart from "../components/dashboard/TopicErrorPieChart";
import GlobalStatsCard from "../components/dashboard/GlobalStatsCard";
import SearchOffIcon from '@mui/icons-material/SearchOff';

export default function DashboardPage() {
    const [topics, setTopics] = useState([]);
    const [selectedTopic, setSelectedTopic] = useState("");
    const [summaryMap, setSummaryMap] = useState({});
    const [trend, setTrend] = useState([]);
    const [examples, setExamples] = useState([]);
    const [ruleSummaries, setRuleSummaries] = useState([]);
    const [topicErrorStats, setTopicErrorStats] = useState([]);
    const [globalStats, setGlobalStats] = useState(null);
    const [trendInterval, setTrendInterval] = useState("hourly");

    useEffect(() => {
        fetchTopics().then(setTopics);
        fetchTopicSummaries().then(summaries => {
            const map = {};
            summaries.forEach(s => map[s.topic] = s);
            setSummaryMap(map);
        });
        fetchRuleSummaries().then(setRuleSummaries);
        fetchTopicErrorStats().then(setTopicErrorStats);
        fetchGlobalStats().then(setGlobalStats);
    }, []);

    useEffect(() => {
        if (!selectedTopic) return;
        fetchFailureTrend(selectedTopic, trendInterval).then(setTrend);
        fetchFailureExamples(selectedTopic).then(setExamples);
    }, [selectedTopic, trendInterval]);

    return (
        <Box p={3}>

            <Typography variant="h4" gutterBottom>Cтатистика по правилам</Typography>
            <RuleSummaryTable data={ruleSummaries} />

            <Divider sx={{ mb: 2 }} />

            <Typography variant="h4" gutterBottom>Cводка по топикам</Typography>

            <Box
                sx={{
                    display: "flex",
                    gap: 3,
                    mb: 3,
                    width: "100%",
                    flexWrap: "wrap"
                }}
            >
                <Box sx={{ flex: 1, minWidth: 0, maxHeight: 350 }}>
                    <TopicErrorPieChart data={topicErrorStats} />
                </Box>
                <Box sx={{ flex: 1, minWidth: 0 }}>
                    {globalStats && <GlobalStatsCard stats={globalStats} />}
                </Box>
            </Box>

            <Box mb={3}>
                <TopicSelector topics={topics} selected={selectedTopic} onChange={setSelectedTopic} />
            </Box>

            {selectedTopic ? (
                <>
                    <Divider sx={{ mb: 2 }} />
                    <Typography variant="h4" gutterBottom>
                        Аналитика по топику: {selectedTopic}
                    </Typography>

                    <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap', mb: 3 }}>
                        <Box sx={{ flex: 1, minWidth: 0 }}>
                            <TopicSummaryCard topic={selectedTopic} summary={summaryMap[selectedTopic]} />
                        </Box>
                        <Box sx={{ flex: 1, minWidth: 0 }}>
                            <TrendChart data={trend} onIntervalChange={setTrendInterval} interval={trendInterval} />
                        </Box>
                    </Box>

                    <Box>
                        <ValueFailureTable data={examples} />
                    </Box>
                </>
            ) : (
                <Paper
                    elevation={0}
                    variant="outlined"
                    sx={{
                        py: 6,
                        px: 4,
                        textAlign: "center",
                        backgroundColor: "#fafafa",
                        borderStyle: "dashed",
                        borderColor: "divider"
                    }}
                >
                    <SearchOffIcon fontSize="large" color="disabled" sx={{ mb: 2 }} />
                    <Typography variant="h6" gutterBottom>
                        Топик не выбран
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Пожалуйста, выберите топик выше, чтобы просмотреть аналитику.
                    </Typography>
                </Paper>
            )}
        </Box>
    );
}