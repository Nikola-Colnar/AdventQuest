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
        // const filteredUsers = data.filter(username => username !== loggedInUsername); //filtrirano da se taj korisnik ne prikazuje na spisku
        //console.log(filteredUsers); filtrirano
        setUsers(data); 
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
            {users.map((username) => (
              <ListItem key={username}>
                <ListItemText primary={username} />
                <IconButton
                  edge="end"
                  aria-label="delete"
                  onClick={() => handleDeleteDialogOpen(username)}
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