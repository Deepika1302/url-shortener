import React, { useState, useEffect } from "react";
import {
  ComposedChart,
  Bar,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import { getStats, StatsEntry } from "../api";

function getOrdinal(n: number): string {
  if (n > 3 && n < 21) return "th";
  switch (n % 10) {
    case 1: return "st";
    case 2: return "nd";
    case 3: return "rd";
    default: return "th";
  }
}

function Stats() {
  const [data, setData] = useState<StatsEntry[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const stats = await getStats();
        const formatted = stats.map((s) => {
          const d = new Date(s.date + "T00:00:00");
          const day = d.getDate();
          const weekday = d.toLocaleDateString("en-US", { weekday: "short" });
          return {
            ...s,
            date: `${day}${getOrdinal(day)} ${weekday}`,
          };
        });
        setData(formatted);
      } catch (err) {
        console.error("Failed to fetch stats:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading) return <p className="loading-text">Loading statistics...</p>;

  return (
    <div className="stats-section">
      <h3 className="section-title">Statistics</h3>
      <div className="chart-container">
        <h4 className="chart-title">Recent Statistics of Click Counts</h4>
        {data.length === 0 ? (
          <p style={{ textAlign: "center", padding: "40px 0" }}>
            No statistics data available yet.
          </p>
        ) : (
          <ResponsiveContainer width="100%" height={350}>
            <ComposedChart data={data}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" tick={{ fontSize: 12 }} />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar
                dataKey="clicks"
                name="URL Clicks"
                fill="#5b9bd5"
                barSize={30}
              />
              <Line
                type="monotone"
                dataKey="creations"
                name="URL Creations"
                stroke="#2f5597"
                strokeWidth={2}
                dot={{ r: 4 }}
              />
            </ComposedChart>
          </ResponsiveContainer>
        )}
      </div>
    </div>
  );
}

export default Stats;
