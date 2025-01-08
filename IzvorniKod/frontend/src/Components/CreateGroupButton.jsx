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

const CreateGroupButton = () => {
  const [open, setOpen] = useState(false);
  const [groupName, setGroupName] = useState("");

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleCreateGroup = async () => {
    if (!groupName) return;

    // OVO JE SAMO ZA TESTIRANJE, KASNIJE CE SE UID DOHVACATI NA PRAVI NACIN
    // I PO POTREBI MIJENJATI STO SE SALJE NA BACKEND
    const newGroup = {
      nazivGrupa: groupName,
      uidPredstavnika: 123456789,
    };

    try {
      const response = await fetch("http://localhost:8080/api/groups", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newGroup),
      });

      if (response.ok) {
        console.log("Group created successfully");
        setOpen(false);
        setGroupName("");
      } else {
        console.error("Failed to create group");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <Box>
      <Button variant="contained" color="primary" onClick={handleClickOpen}>
        Create New Group
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Create New Group</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Group Name"
            type="text"
            fullWidth
            value={groupName}
            onChange={(e) => setGroupName(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="secondary">
            Cancel
          </Button>
          <Button onClick={handleCreateGroup} color="primary">
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default CreateGroupButton;
