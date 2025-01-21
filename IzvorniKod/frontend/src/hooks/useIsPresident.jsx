import { useState, useEffect } from "react";

const API_BASE_URL = "http://localhost:8080";

const useIsPresident = (username, groupId) => {
  const [isPresident, setIsPresident] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchIsPresident = async () => {
      try {
        setLoading(true);
        const response = await fetch(
          `${API_BASE_URL}/api/groups/${groupId}/getIdPredstavnik`
        );
        if (!response.ok) {
          throw new Error("Failed to fetch president ID.");
        }
        const presidentId = await response.json();
        setIsPresident(presidentId === parseInt(username, 10));
      } catch (err) {
        console.error(err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchIsPresident();
  }, [username, groupId]);

  return { isPresident, loading, error };
};

export default useIsPresident;
