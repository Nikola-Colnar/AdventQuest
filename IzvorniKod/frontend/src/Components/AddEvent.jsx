import { useState } from "react";
import {
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Grid,
  Box,
  Typography,
} from "@mui/material";

const AddEvent = () => {
  const [open, setOpen] = useState(false);
  const [eventDetails, setEventDetails] = useState({
    title: "",
    description: "",
    date: "",
    color: "#a31515",
  });

  const handleInputChange = (e) => {
    setEventDetails({
      ...eventDetails,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async () => {
    const groupId = localStorage.getItem("myGroupId"); // Dohvat ID grupe

    const newEvent = {
      eventName: eventDetails.title,
      description: eventDetails.description,
      date: eventDetails.date, //datum u standardnom formatu
      color: eventDetails.color,
    };

    try {
      const response = await fetch(`http://localhost:8080/api/groups/${groupId}/addEvent`, {
        method: "POST",
        credentials : "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newEvent),
      });

      if (response.ok) {
        console.log("Event successfully added");
        setOpen(false); // Zatvori dijalog
        setEventDetails({
          title: "",
          description: "",
          date: "",
          color: "#a31515",
        });
      } else {
        console.error("Failed to add event");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <div>
      <Button
        variant="contained"
        color="primary"
        onClick={() => setOpen(true)}
        style={{ }}
      >
        Add Quest
      </Button>

      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="sm" >
        <DialogTitle align="center">Add New Quest</DialogTitle>
        <DialogContent>
          <Grid container spacing={3} justifyContent="center" style={{ marginTop: "16px" }}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Quest Title"
                name="title"
                value={eventDetails.title}
                onChange={handleInputChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={3}
                label="Quest details"
                name="description"
                value={eventDetails.description}
                onChange={handleInputChange}
              />
            </Grid>
            {/* Ne Koristimo vise datum u dodavanju evenata, samo predsjednik moze rasporedivati evente
  <Grid item xs={12}>
    <TextField
      type="date"
      fullWidth
      name="date"
      label="Event Date"
      InputLabelProps={{ shrink: true }}
      value={eventDetails.date}
      onChange={handleInputChange}
    />
  </Grid>
  */}

            <Grid item xs={12}>
              <Typography align="center" gutterBottom>
                Color
              </Typography>
              <Box display="flex" alignItems="center" justifyContent="center" gap={2}>
                    <input
                      type="color"
                      name="color"
                      value={eventDetails.color}
                      onChange={handleInputChange}
                      // input za boje mijenja boju ovisno o selektiranoj boji
                      style={{
                        backgroundColor: eventDetails.color,
                        border: "1px solid #ccc",
                        borderRadius: "5px",
                        cursor: "pointer",
                        width: "80%",
                      }}
                    />
                </Box>
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)} color="secondary">
            Cancel
          </Button>
          <Button onClick={handleSubmit} color="primary" variant="contained" disabled={!eventDetails.title}>
            Save Activity
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default AddEvent;
