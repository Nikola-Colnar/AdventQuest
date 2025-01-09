import { useState } from "react";
import {
  Box,
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";

const AddUserToGroupButton = () => {
  const [open, setOpen] = useState(false);
  const [username, setUsername] = useState("");
  const [groupId, setGroupId] = useState("");

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleAddUser = async () => {
    if (!username || !groupId) return;

    const userToAdd = {
      username: username,
      groupId: parseInt(groupId, 10), // Pretvaranje u cijeli broj
    };

    try {
      const response = await fetch(`http://localhost:8080/api/user-groups/${username}/group/${groupId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userToAdd),
      });

      if (response.ok) {
        console.log("User added to group successfully");
        setOpen(false);
        setUsername("");
        setGroupId("");
      } else {
        console.error("Failed to add user to group");
      }
    } catch (error) {
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
          <TextField
            margin="dense"
            label="Group ID"
            type="number"
            fullWidth
            value={groupId}
            onChange={(e) => setGroupId(e.target.value)}
          />
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