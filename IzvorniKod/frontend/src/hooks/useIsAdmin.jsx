import { useState, useEffect } from "react";

const API_BASE_URL = "http://localhost:8080";

const useIsAdmin = (username) => {
  const [isAdmin, setIsAdmin] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchIsAdmin = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_BASE_URL}/isAdmin`, {
          credentials: "include",
        });
        if (!response.ok) {
          throw new Error("Failed to fetch admin status.");
        }
        const isAdminData = await response.json();
        setIsAdmin(isAdminData);
      } catch (err) {
        console.error(err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchIsAdmin();
  }, [username]);

  return { isAdmin, loading, error };
};

export default useIsAdmin;
