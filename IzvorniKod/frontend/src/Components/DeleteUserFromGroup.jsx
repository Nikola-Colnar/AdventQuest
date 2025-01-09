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

  const groupId = 5; // ID grupe koji je hardkodiran

  const fetchUsersByGroup = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/user-groups/group/${groupId}`);
      if (response.ok) {
        const data = await response.json();
        setUsers(data); // Postavljanje korisnika u odabranoj grupi
      } else {
        console.error("Failed to fetch users by group");
      }
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const handleClickOpen = () => {
    fetchUsersByGroup(); // Dohvati korisnike pri otvaranju dijaloga
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleDeleteUser = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/user-groups/user/${userToDelete.username}/group/${groupId}`, {
        method: "DELETE",
      });
      if (response.ok) {
        // Ako je korisnik uspješno obrisan, ažuriraj korisnički popis
        setUsers(users.filter(user => user.id !== userToDelete.id));
        setDeleteDialogOpen(false); // Zatvori dijalog
      } else {
        console.error("Failed to delete user");
      }
    } catch (error) {
      console.error("Error deleting user:", error);
    }
  };

  const handleDeleteDialogOpen = (user) => {
    setUserToDelete(user);
    setDeleteDialogOpen(true);
  };

  const handleDeleteDialogClose = () => {
    setDeleteDialogOpen(false);
    setUserToDelete(null);
  };

  return (
    <Box>
      <Button variant="contained" color="primary" onClick={handleClickOpen}>
        Show Users from Group
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Users in Group</DialogTitle>
        <DialogContent
          sx={{
            maxHeight: 100, // maksimalnavisina prikaza
            overflowY: 'auto', // skrolanje
          }}
        >
          <List>
            {users.map((user) => (
              <ListItem key={user.id}>
                <ListItemText primary={user.username} />
                <IconButton
                  edge="end"
                  aria-label="delete"
                  onClick={() => handleDeleteDialogOpen(user)}
                >
                  <DeleteIcon />
                </IconButton>
              </ListItem>
            ))}
          </List>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="secondary">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>

      {/* Dijalog za potvrdu brisanja korisnika */}
      <Dialog open={deleteDialogOpen} onClose={handleDeleteDialogClose}>
        <DialogTitle>Delete User</DialogTitle>
        <DialogContent>
          <p>Are you sure you want to delete this user?</p>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteDialogClose} color="secondary">
            Cancel
          </Button>
          <Button onClick={handleDeleteUser} color="primary">
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default DeleteUserFromGroup;