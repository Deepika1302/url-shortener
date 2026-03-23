import React from "react";
import { UrlEntry } from "../api";

interface UrlTableProps {
  urls: UrlEntry[];
  loading: boolean;
}

function getOrdinal(n: number): string {
  if (n > 3 && n < 21) return "th";
  switch (n % 10) {
    case 1: return "st";
    case 2: return "nd";
    case 3: return "rd";
    default: return "th";
  }
}

function formatDate(iso: string): string {
  const date = new Date(iso);
  const weekdays = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
  const months = ["January","February","March","April","May","June","July","August","September","October","November","December"];

  const weekday = weekdays[date.getDay()];
  const day = date.getDate();
  const month = months[date.getMonth()];
  const year = date.getFullYear();

  return `${weekday} ${String(day).padStart(2, "0")}${getOrdinal(day)} ${month}, ${year}`;
}

function UrlTable({ urls, loading }: UrlTableProps) {
  if (loading) {
    return <p className="loading-text">Loading recent URLs...</p>;
  }

  return (
    <div>
      <h3 className="section-title">Recent URLs</h3>
      <div className="table-wrapper">
        <table className="url-table">
          <thead>
            <tr>
              <th>Original URL</th>
              <th>Short URL</th>
              <th colSpan={3}></th>
              <th>Created on</th>
              <th>Clicks</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {urls.length === 0 ? (
              <tr>
                <td colSpan={8} className="empty-row">
                  No URLs found. Create one above!
                </td>
              </tr>
            ) : (
              urls.map((item) => (
                <tr key={item.id}>
                  <td className="url-cell">{item.originalUrl}</td>
                  <td className="url-cell">
                    <a
                      href={item.shortUrl}
                      className="short-link"
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      {item.shortUrl}
                    </a>
                  </td>
                  <td className="icon-cell">
                    <span
                      className="icon-btn icon-green"
                      title="Copy"
                      onClick={() => navigator.clipboard.writeText(item.shortUrl)}
                    >
                      &#128203;
                    </span>
                  </td>
                  <td className="icon-cell">
                    <span className="icon-btn icon-blue" title="QR Code">
                      &#9638;
                    </span>
                  </td>
                  <td className="icon-cell">
                    <span
                      className="icon-btn icon-green"
                      title="Open"
                      onClick={() => window.open(item.shortUrl, "_blank")}
                    >
                      &#8599;
                    </span>
                  </td>
                  <td>{formatDate(item.createdAt)}</td>
                  <td className="clicks-cell">{item.clickCount}</td>
                  <td>
                    <button className="analytics-btn">
                      <span className="analytics-icon">&#128200;</span> Analytics
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default UrlTable;
