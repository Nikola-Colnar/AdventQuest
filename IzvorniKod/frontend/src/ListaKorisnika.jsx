
import React, { useState } from 'react';

const USERS_REST_API_URL = 'http://localhost:8080/api/users';

function ListaKorisnika() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  // Funkcija za dohvaćanje korisnika
  const fetchUsers = async () => {
    setLoading(true); // Postavljanje loading na true kada započne dohvaćanje
    try {
      const response = await fetch(USERS_REST_API_URL);
      if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
      }
      const data = await response.json();
      setUsers(data); // Postavljanje dohvaćenih korisnika u stanje
    } catch (error) {
      console.error('Error fetching users:', error);
    } finally {
      setLoading(false); // Postavljanje loading na false
    }
  };

  return (
    <div style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px', width: '300px', margin: '20px auto' }}>
      <h2>User List</h2>
      <button onClick={fetchUsers} disabled={loading} style={{ marginBottom: '10px' }}>
        {loading ? 'Loading...' : 'Fetch Users'}
      </button>
      {users.length > 0 ? (
        <ul>
          {users.map(user => (
            <li key={user.id}>
              {user.firstName} {user.lastName} - {user.email}
            </li>
          ))}
        </ul>
      ) : (
        !loading && <p>No users found. Click the button to load users.</p>
      )}
    </div>
  );
}

export default ListaKorisnika;