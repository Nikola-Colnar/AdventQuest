import { useState } from "react";
import {
  Box,
  Button,
  Dialog,
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

  return (
    <Box>
      <Button sx={{marginTop: "20px"}} variant="contained" color="primary" onClick={handleClickOpen}>
        Show participants
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle
          sx={{
            textAlign: "center",
            fontWeight: "bold",
            fontSize: "1rem",
          }}
        >
          Users in Group
        </DialogTitle>
        <DialogContent
          sx={{
            maxHeight: 200, // Maksimalna visina za prikaz
            overflowY: "auto", // Omogućeno skrolanje
            textAlign: "center", // Centrirani tekst
          }}
        >
          {users.length > 0 ? (
            <List
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center", // Centriranje stavki
              }}
            >
              {users.map((username, index) => (
                <ListItem key={index}>
                  <ListItemText
                    primary={
                      <Typography
                        sx={{
                          textAlign: "center", // Centriranje imena
                        }}
                      >
                        {username}
                      </Typography>
                    }
                  />
                </ListItem>
              ))}
            </List>
          ) : (
            <Typography>No participants</Typography>
          )}
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default ShowAllUsersFromGroup;