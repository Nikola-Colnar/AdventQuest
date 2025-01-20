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

const GetALLGroupsAdmin = () => {
  const [open, setOpen] = useState(false);
  const [groups, setGroups] = useState([]);
  const [selectedGroupId, setSelectedGroupId] = useState("");
  const [selectedGroupName, setSelectedGroupName] = useState("");

  const fetchUserGroups = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/groups/getGroups`);
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
    const groupId = event.target.value; // GroupId iz odabrane vrijednosti
    setSelectedGroupId(groupId); // postavljamo vrijednost
    const selectedGroup = groups.find(group => group.idgroup === groupId);
    setSelectedGroupName(selectedGroup?.nazivGrupa || ""); // Postavimo naziv grupe
  };

  const handleSubmit = () => {
    if (selectedGroupId) {
      localStorage.setItem("myGroupId", selectedGroupId); // Spremi groupId u localStorage
      localStorage.setItem("myGroupName", selectedGroupName);
      console.log("Selected Group ID saved to localStorage:", selectedGroupId);
      console.log("Selected Group Name saved to localStorage:", selectedGroupName);
    } else {
      console.error("No group selected!");
    }

    setOpen(false);
  };


  return (
    <Box>
      <Button sx={{ backgroundColor: "green" }} variant="contained" color="primary" onClick={handleClickOpen}>
        Select Group
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Select Group</DialogTitle>
        <DialogContent sx={{ maxHeight: "400px", overflowY: "auto" }}>
          <FormControl fullWidth margin="dense">
            <InputLabel>Group</InputLabel>
            <Select
              value={selectedGroupId || ""}
              onChange={handleGroupSelect}
              label="Group"
              MenuProps={{
                PaperProps: {
                  style: {
                    maxHeight: 200,
                    overflowY: "auto", //scrolanje
                  },
                },
              }}
            >
              {groups.map((group) => (
                <MenuItem key={group.idgroup} value={group.idgroup}>
                  {group.nazivGrupa} {/* Provjera prikaza naziva grupe */}
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

      {selectedGroupId && <p>Selected Group: {selectedGroupName}</p>}
    </Box>
  );
};

export default GetALLGroupsAdmin;