import { useState } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogContent,
  DialogTitle,
  Typography,
  Grid,
  Card,
  CardContent,
} from "@mui/material";
import { styled } from "@mui/system";

const StyledCard = styled(Card)({
  transition: "transform 0.3s ease, box-shadow 0.3s ease",
  position: "relative",
  height: "auto",
  padding: "1rem",
  cursor: "pointer",
  boxShadow: "0 4px 10px rgba(0, 0, 0, 0.1)",
  "&:hover": {
    transform: "scale(1.05)",
  },
});

/*
const DetailCard = styled(Card)({
  width: "100%",
  maxWidth: "500px",
  margin: "auto",
  padding: "2rem",
  textAlign: "center",
  boxShadow: "0 4px 10px rgba(0, 0, 0, 0.2)",
  backgroundColor: "#fff",
});*/

const ShowAllEventsFromGroup = () => {
  const [open, setOpen] = useState(false);
  const [openDetailDialog, setOpenDetailDialog] = useState(false);  // State za otvaranje detaljnog dijaloga
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);

  const fetchEventsByGroup = async () => {
    const groupId = localStorage.getItem("myGroupId");
    try {
      const response = await fetch(
        `http://localhost:8080/api/groups/${groupId}/getEvents`
      );
      if (response.ok) {
        const data = await response.json();
        setEvents(data);
      } else {
        console.error("Failed to fetch events by group");
      }
    } catch (error) {
      console.error("Error fetching events:", error);
    }
  };

  const handleClickOpen = () => {
    fetchEventsByGroup();
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setTimeout(() => {
      setSelectedEvent(null);
    }, 300); //timeout 0.3s za tranziciju
  };

  const handleCardClick = (event) => {
    setSelectedEvent(event);
    setOpenDetailDialog(true);
  };

  const handleDetailDialogClose = () => {
    setOpenDetailDialog(false);
  };

  return (
    <Box>
      <Button variant="contained" color="primary" onClick={handleClickOpen}>
        Show Events
      </Button>
      <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
        <DialogTitle
          sx={{
            textAlign: "center",
            fontWeight: "bold",
            fontSize: "1rem",
          }}
        >
          Events in Group
        </DialogTitle>
        <DialogContent
          sx={{
            maxHeight: 500,
            overflowY: "auto",
            textAlign: "center",
          }}
        >
          {events.length > 0 ? (
            <Grid container spacing={2}>
              {events.map((event) => (
                <Grid item xs={12} sm={6} md={4} key={event.eventId}>
                  <StyledCard
                    onClick={() => handleCardClick(event)}
                    style={{
                      backgroundColor: event.color || "#e0e0e0",
                    }}
                  >
                    <CardContent>
                      <Typography
                        variant="h6"
                        sx={{ fontWeight: "bold", color: "#fff",
                          textShadow: "2px 2px 4px rgba(0, 0, 0, 0.7)" //sjena da se vidi na bilokojoj boji
                        }}
                      >
                        {event.eventName || "Untitled"}
                      </Typography>
                    </CardContent>
                  </StyledCard>
                </Grid>
              ))}
            </Grid>
          ) : (
            <Typography>No events available</Typography>
          )}
        </DialogContent>
      </Dialog>

      {/* Detaljni dijalog za odabrani event */}
      {selectedEvent && (
        <Dialog
          open={openDetailDialog}
          onClose={handleDetailDialogClose}
          maxWidth="md"
          fullWidth
        >
          <DialogTitle
            sx={{
              textAlign: "center",
              fontWeight: "bold",
              fontSize: "1.25rem",
            }}
          >
            {selectedEvent?.eventName || "Untitled Event"}
          </DialogTitle>
          <DialogContent
            sx={{
              textAlign: "center",
              padding: "2rem",
            }}
          >
            <Typography variant="body1">
              {selectedEvent.description || "Details not planned, yet!"}
            </Typography>
            {/*
            <Typography variant="body2" sx={{ marginTop: "1rem", fontStyle: "italic" }}>
              {selectedEvent.date
                ? new Date(selectedEvent.date).toLocaleDateString()
                : "No date provided"}
            </Typography>
          */}
            <Button
              variant="outlined"
              color="primary"
              onClick={handleDetailDialogClose}
              sx={{ marginTop: "1rem" }}
            >
              Back to Events
            </Button>
          </DialogContent>
        </Dialog>
      )}
    </Box>
  );
};

export default ShowAllEventsFromGroup;