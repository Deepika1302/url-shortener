import React, { useState } from "react";
import { createShortUrl } from "../api";

interface UrlFormProps {
  onUrlCreated: () => void;
}

function UrlForm({ onUrlCreated }: UrlFormProps) {
  const [url, setUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    if (!url.trim()) {
      setError("Please enter a URL");
      return;
    }
    setLoading(true);
    setError("");
    try {
      await createShortUrl(url.trim());
      setUrl("");
      onUrlCreated();
    } catch (err: any) {
      setError(err.message || "Failed to shorten URL");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="form">
        <input
          type="text"
          placeholder="Enter your original URL eg. http://demos.nellwinne.net/URLShorterner/"
          className="input"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleSubmit()}
        />
        <button className="short-btn" onClick={handleSubmit} disabled={loading}>
          {loading ? "Shortening..." : "Shorten URL"}
        </button>
      </div>
      {error && <p className="form-error">{error}</p>}
    </div>
  );
}

export default UrlForm;
