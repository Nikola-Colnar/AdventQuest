import { useState } from "react";
import {
  Box,
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormHelperText
} from "@mui/material";

const AddUserToGroupButton = () => {
  const [open, setOpen] = useState(false);
  const [username, setUsername] = useState("");

  //Povratne poruke za korisnika
  const [infoMessage, setInfoMessage] = useState("");

  const handleClickOpen = () => {
    setOpen(true);
    setInfoMessage("");
  };

  const handleClose = () => {
    setOpen(false);
    setInfoMessage("");
  };

  const handleAddUser = async () => {
    if (!username) {
      setInfoMessage("Username required!");
      return;
    }

    const groupId = localStorage.getItem("myGroupId");
    if (!groupId) {
      setInfoMessage("Join group to add users!");
      return;
    }

    const userToAdd = {
      username: username,
      groupId: parseInt(groupId, 10),
    };

    try {
      const response = await fetch(`http://localhost:8080/${groupId}/addUser`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userToAdd),
      });

      if (response.ok) {
        setInfoMessage("New Adventurer added");
        setUsername("");
        setTimeout(() => setInfoMessage(""), 2000);
      } else {
        setInfoMessage("User doesn't exist"); // Tvoj tekst za poruku
        setTimeout(() => setInfoMessage(""), 2000);
      }
    } catch (error) {
      setInfoMessage("Error occurred. Please try again.");
      setTimeout(() => setInfoMessage(""), 2000);
      console.error("Error:", error);
    }
  };

  return (
    <Box>
      <Button variant="contained" color="primary" onClick={handleClickOpen}>
        Add User to Group
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add User to Group</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Username"
            type="text"
            fullWidth
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          {infoMessage && (
            <FormHelperText>{infoMessage}</FormHelperText> //poruke ispod textfielda
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="secondary">
            Cancel
          </Button>
          <Button onClick={handleAddUser} color="primary">
            Add
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AddUserToGroupButton;