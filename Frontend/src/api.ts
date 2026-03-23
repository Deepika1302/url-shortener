const API_BASE = process.env.REACT_APP_API_URL || "";

export interface UrlEntry {
  id: number;
  originalUrl: string;
  shortCode: string;
  shortUrl: string;
  createdAt: string;
  clickCount: number;
  lastAccessedAt: string | null;
}

export interface UrlsResponse {
  urls: UrlEntry[];
  page: number;
  totalPages: number;
  totalItems: number;
}

export interface ClickRecord {
  id: number;
  clickedAt: string;
}

export interface AnalyticsData extends UrlEntry {
  clicks: ClickRecord[];
}

export interface StatsEntry {
  date: string;
  clicks: number;
  creations: number;
}

export async function createShortUrl(originalUrl: string): Promise<UrlEntry> {
  const response = await fetch(`${API_BASE}/api/urls`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ originalUrl }),
  });
  if (!response.ok) {
    const err = await response.json().catch(() => ({}));
    throw new Error(err.message || "Failed to create short URL");
  }
  return response.json();
}

export async function getRecentUrls(
  page = 1,
  perPage = 10
): Promise<UrlsResponse> {
  const response = await fetch(
    `${API_BASE}/api/urls?page=${page}&per_page=${perPage}`
  );
  if (!response.ok) throw new Error("Failed to fetch URLs");
  return response.json();
}

export async function getUrlAnalytics(id: number): Promise<AnalyticsData> {
  const response = await fetch(`${API_BASE}/api/urls/${id}/analytics`);
  if (!response.ok) throw new Error("Failed to fetch analytics");
  return response.json();
}

export async function getStats(): Promise<StatsEntry[]> {
  const response = await fetch(`${API_BASE}/api/stats`);
  if (!response.ok) throw new Error("Failed to fetch stats");
  return response.json();
}
