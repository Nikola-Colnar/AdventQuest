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

const GetALLGroupsAdmin = (props) => {
  const [open, setOpen] = useState(false);
  const [groups, setGroups] = useState([]);
  const [selectedGroupId, setSelectedGroupId] = useState("");
  const [selectedGroupName, setSelectedGroupName] = useState("");

  const fetchUserGroups = async () => {
    const adminName = localStorage.getItem("username");
    try {
      const response = await fetch(`http://localhost:8080/${adminName}/getAllGroups`, {
        credentials : "include",
      });
      if (response.ok) {
        const data = await response.json();
        console.log("Fetched groups:", data); // Provjera podataka
        setGroups(data); // Postavljanje grupa
      } 
      else if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
      }else {
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
    const selectedGroup = groups.find(group => group.groupId === groupId);
    setSelectedGroupName(selectedGroup?.groupName || ""); // Postavimo naziv grupe
  };

  const handleSubmit = () => {
    if (selectedGroupId) {
      // eslint-disable-next-line react/prop-types
      props.setSelectedGroupId(selectedGroupId); // postavlja group ID i u App da ga koriste druge komponente
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


    </Box>
  );
};

export default GetALLGroupsAdmin;