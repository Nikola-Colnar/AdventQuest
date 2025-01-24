import { useState } from "react";
import {
  Box,
  Button,
  Dialog, DialogActions,
  DialogContent,
  DialogTitle,
  List,
  ListItem,
  ListItemText,
  Typography,
} from "@mui/material";

const ShowAllUsersFromGroup = () => {
  const [open, setOpen] = useState(false);
  const [users, setUsers] = useState([]);



  const fetchUsersByGroup = async () => {
    const groupId = localStorage.getItem("myGroupId"); // ID grupe u kojem se nalazi ovaj user
    const loggedInUsername = localStorage.getItem("username");
    try {
      const response = await fetch(`http://localhost:8080/${groupId}/getUsers`, {
        credentials : "include",
      }); //dohvacamo sve usere
      if (response.ok) {
        const data = await response.json();
        //console.log(data);
        const filteredUsers = data.filter(username => username !== loggedInUsername); //filtrirano da se taj korisnik ne prikazuje na spisku
        //console.log(filteredUsers); filtrirano
        setUsers(filteredUsers);
      } 
      else if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
      }else {
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

  return (
    <Box>
      <Button sx={{}} variant="contained" color="primary" onClick={handleClickOpen}>
        Show participants
      </Button>
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle sx={{ backgroundColor: '#f0f0f0', fontWeight: 'bold' }}>Users in Group</DialogTitle>
        <DialogContent
          sx={{
            maxHeight: 400, // Maksimalna visina liste
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
    </Box>
  );
};

export default ShowAllUsersFromGroup;