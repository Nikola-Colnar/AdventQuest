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

        // dohvati korisnicki ID na temelju username
        const userIdResponse = await fetch(`${API_BASE_URL}/${username}/getUserId`);
        if (!userIdResponse.ok) {
          throw new Error("Failed to fetch user ID.");
        }
        const userId = await userIdResponse.json();

        // dohvati predsjednicki ID za grupu
        const presidentResponse = await fetch(
          `${API_BASE_URL}/api/groups/${groupId}/getIdPredstavnik`
        );
        if (!presidentResponse.ok) {
          throw new Error("Failed to fetch president ID.");
        }
        const presidentId = await presidentResponse.json();

        // provjeri je li korisnik predsjednik
        setIsPresident(userId === presidentId);
      } catch (err) {
        console.error(err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    // samo pokreni API zahtjev ako su username i groupId valjani
    if (username && groupId && groupId !== -1) {
      fetchIsPresident();
    } else {
      setLoading(false);
      setError(null); // Ako nema validnog groupId-a, nema greške
      setIsPresident(false);
    }
  }, [username, groupId]);

  return { isPresident, loading, error };
};

export default useIsPresident;
