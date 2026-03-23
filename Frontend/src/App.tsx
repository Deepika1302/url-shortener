import React, { useState, useEffect, useCallback } from "react";
import UrlForm from "./components/UrlForm";
import UrlTable from "./components/UrlTable";
import Stats from "./components/Stats";
import { getRecentUrls, UrlEntry } from "./api";
import "./components/styles.css";

function App() {
  const [urls, setUrls] = useState<UrlEntry[]>([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);

  const fetchUrls = useCallback(async (p: number) => {
    setLoading(true);
    try {
      const data = await getRecentUrls(p);
      setUrls(data.urls);
      setTotalPages(data.totalPages);
      setPage(data.page);
    } catch (err) {
      console.error("Failed to fetch URLs:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchUrls(1);
  }, [fetchUrls]);

  const handleUrlCreated = () => {
    fetchUrls(1);
  };

  const handlePageChange = (newPage: number) => {
    if (newPage >= 1 && newPage <= totalPages) {
      fetchUrls(newPage);
    }
  };

  return (
    <div>
      <h3 className="header-title">Easy URL Shortener</h3>

      <div className="hero">
        <h4 className="hero-heading">Simplify your URL</h4>
        <UrlForm onUrlCreated={handleUrlCreated} />
        <p className="hero-subtext">
          All the Shorted URL and their analytics are public.
        </p>
      </div>

      <div className="container">
        <UrlTable urls={urls} loading={loading} />

        {totalPages > 0 && (
          <div className="pagination">
            <button
              className="page-btn page-arrow"
              onClick={() => handlePageChange(page - 1)}
              disabled={page <= 1}
            >
              &lt;
            </button>
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((p) => (
              <button
                key={p}
                className={`page-btn ${p === page ? "page-active" : ""}`}
                onClick={() => handlePageChange(p)}
              >
                {String(p).padStart(2, "0")}
              </button>
            ))}
            <button
              className="page-btn page-arrow"
              onClick={() => handlePageChange(page + 1)}
              disabled={page >= totalPages}
            >
              &gt;
            </button>
          </div>
        )}

        <Stats />
      </div>
    </div>
  );
}

export default App;
