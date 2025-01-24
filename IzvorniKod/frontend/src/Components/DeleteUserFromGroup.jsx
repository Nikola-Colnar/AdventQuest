import { useState } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete"; // Ikona za brisanje

const DeleteUserFromGroup = () => {
  const [open, setOpen] = useState(false);
  const [users, setUsers] = useState([]);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState(null);



  const fetchUsersByGroup = async () => {
    const groupId = localStorage.getItem("myGroupId"); // ID grupe u kojem se nalazi ovaj user
    const loggedInUsername = localStorage.getItem("username");
    try {
      const response = await fetch(`http://localhost:8080/${groupId}/getUsers`); //dohvacamo sve usere
      if (response.ok) {
        const data = await response.json();
        //console.log(data);
         const filteredUsers = data.filter(username => username !== loggedInUsername); //filtrirano da se taj korisnik ne prikazuje na spisku
        //console.log(filteredUsers); filtrirano
        setUsers(filteredUsers);
      } else {
        console.error("Failed to fetch users by group");
      }
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const handleClickOpen = () => {
    setUsers([]);
    fetchUsersByGroup(); // Dohvati korisnike pri otvaranju dijaloga
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleDeleteUser = async () => {
    try {
      const groupId = localStorage.getItem("myGroupId"); // ID grupe u kojem se nalazi ovaj user
      const response = await fetch(`http://localhost:8080/user/${userToDelete}/group/${groupId}`, {
        method: "DELETE",
      });
      if (response.ok) {
        // Ako je korisnik uspješno obrisan, ažuriraj korisnički popis
        setUsers(users.filter(username => username !== userToDelete));
        setDeleteDialogOpen(false); // Zatvori dijalog
      } else {
        console.error("Failed to delete user");
      }
    } catch (error) {
      console.error("Error deleting user:", error);
    }
  };

  const handleDeleteDialogOpen = (username) => {
    setUserToDelete(username);
    setDeleteDialogOpen(true);
  };

  const handleDeleteDialogClose = () => {
    setDeleteDialogOpen(false);
    setUserToDelete(null);
  };

  return (
    <Box>
      <Button variant="contained" color="primary" onClick={handleClickOpen}>
        Delete from group
      </Button>
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle sx={{ backgroundColor: '#f0f0f0', fontWeight: 'bold' }}>Users in Group</DialogTitle>
        <DialogContent
          sx={{
            maxHeight: "300px", // Maksimalna visina liste
            overflowY: 'auto', // Omogući skrolanje
            backgroundColor: '#fafafa',
            padding: '16px',
          }}
        >
          <List>
            {users.map((username) => (
              <ListItem
                key={username}
                sx={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginBottom: '8px',
                  backgroundColor: '#ffffff',
                  borderRadius: '8px',
                  boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.1)',
                  padding: '8px 16px',
                }}
              >
                <ListItemText primary={username} sx={{ color: '#333' }} />
                <IconButton
                  edge="end"
                  aria-label="delete"
                  onClick={() => handleDeleteDialogOpen(username)}
                  sx={{ color: 'rgba(170,21,34,0.87)' }}
                >
                  <DeleteIcon />
                </IconButton>
              </ListItem>
            ))}
          </List>
        </DialogContent>
        <DialogActions sx={{ padding: '8px 16px', justifyContent: 'center' }}>
          <Button onClick={handleClose} variant="outlined" color="secondary">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>

      {/* Dijalog za potvrdu brisanja korisnika */}
      <Dialog open={deleteDialogOpen} onClose={handleDeleteDialogClose} maxWidth="xs">
        <DialogTitle sx={{ backgroundColor: '#f8d7da', color: '#721c24', fontWeight: 'bold' }}>
          Confirm Deletion
        </DialogTitle>
        <DialogContent sx={{ padding: '16px', backgroundColor: '#fefefe' }}>
          <p style={{ fontSize: '16px', color: '#333' }}>
            Are you sure you want to delete <b>{userToDelete}</b> from this group?
          </p>
        </DialogContent>
        <DialogActions sx={{ padding: '8px 16px', justifyContent: 'space-between' }}>
          <Button
            onClick={handleDeleteDialogClose}
            variant="outlined"
            color="secondary"
          >
            Cancel
          </Button>
          <Button
            onClick={handleDeleteUser}
            variant="contained"
            color="primary"
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default DeleteUserFromGroup;