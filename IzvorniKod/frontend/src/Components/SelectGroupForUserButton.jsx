import { useState } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from "@mui/material";

const SelectGroupForUserButton = () => {
  const [open, setOpen] = useState(false);
  const [groups, setGroups] = useState([]);
  const [selectedGroup, setSelectedGroup] = useState("");
  const [username, setUsername] = useState("markoG"); // Trenutno hardkodirano

  const fetchUserGroups = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/user-groups/user/${username}`);
      if (response.ok) {
        const data = await response.json();
        console.log("Fetched groups:", data); // Provjera podataka
        setGroups(data); // Postavljanje grupa
      } else {
        console.error("Failed to fetch user groups");
      }
    } catch (error) {
      console.error("Error fetching user groups:", error);
    }
  };

  const handleClickOpen = () => {
    fetchUserGroups(); // Dohvati grupe svaki put kad se otvori dijalog
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleGroupSelect = (event) => {
    setSelectedGroup(event.target.value);
  };

  const handleSubmit = () => {
    console.log("Selected Group:", selectedGroup);
    setOpen(false);
  };

  return (
    <Box>
      <Button variant="contained" color="primary" onClick={handleClickOpen}>
        Select Group for User
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Select Group</DialogTitle>
        <DialogContent>
          <FormControl fullWidth margin="dense">
            <InputLabel>Group</InputLabel>
            <Select
              value={selectedGroup}
              onChange={handleGroupSelect}
              label="Group"
            >
              {groups.map((group) => (
                <MenuItem key={group.id} value={group.groupName}>
                  {group.groupName} {/* Provjera prikaza naziva grupe */}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="secondary">
            Cancel
          </Button>
          <Button onClick={handleSubmit} color="primary">
            Select
          </Button>
        </DialogActions>
      </Dialog>

      {selectedGroup && <p>Selected Group: {selectedGroup}</p>}
    </Box>
  );
};

export default SelectGroupForUserButton;