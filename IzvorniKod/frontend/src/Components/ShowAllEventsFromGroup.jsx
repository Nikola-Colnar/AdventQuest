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
  TextField,
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

const ShowAllEventsFromGroup = () => {
  const [open, setOpen] = useState(false);
  const [openDetailDialog, setOpenDetailDialog] = useState(false);
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  const fetchEventsByGroup = async () => {
    const groupId = localStorage.getItem("myGroupId");
    try {
      const response = await fetch(
        `http://localhost:8080/api/groups/${groupId}/getEvents`
      , {
        credentials : "include",
      });
      if (response.ok) {
        const data = await response.json();
        setEvents(data);
      }
      else if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
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
    }, 300); // timeout 0.3s za tranziciju
  };

  const handleCardClick = (event) => {
    setSelectedEvent(event);
    setOpenDetailDialog(true);
  };

  const handleDetailDialogClose = () => {
    setOpenDetailDialog(false);
    setSelectedEvent(null); // Resetiranje odabranog događaja kad se dijalog zatvori
  };

  const handleEditDetails = () => {
    setIsEditing(true);
  };

  const handleBackToDetails = () => {
    setIsEditing(false); // Vraćanje na detalje
  };

  const handleSaveChanges = async () => {
    const groupId = localStorage.getItem("myGroupId");
    const updatedEvent = {
      ...selectedEvent,
      eventName: selectedEvent.eventName,
      description: selectedEvent.description,
      color: selectedEvent.color,
    };

    try {
      const response = await fetch(
        `http://localhost:8080/api/groups/${groupId}/updateEvent/${selectedEvent.eventId}`,
        {
          method: "PUT",
          credentials : "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(updatedEvent),
        }
      );

      if (response.ok) {
        console.log("Event updated successfully");
        setIsEditing(false);
        await fetchEventsByGroup();
      }
      else if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
      } else {
        console.error("Failed to update event");
      }
    } catch (error) {
      console.error("Error updating event:", error);
    }
  };

  return (
    <Box>
      <Button sx = {{}} variant="contained" color="primary" onClick={handleClickOpen}>
        Holiday Quests
      </Button>
      <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
        <DialogTitle
          sx={{
            textAlign: "center",
            fontWeight: "bold",
            fontSize: "1rem",
          }}
        >
          Holiday Activity
        </DialogTitle>
        <DialogContent
          sx={{
            maxHeight: 500,
            overflowY: "auto",
            textAlign: "center",
            wordWrap: "break-word",
            overflowWrap: "break-word",
            whiteSpace: "normal",
            wordBreak: "break-word",

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
                        sx={{
                          fontWeight: "bold",
                          color: "#fff",
                          textShadow: "2px 2px 4px rgba(0, 0, 0, 0.7)",
                          wordWrap: "break-word",
                          overflowWrap: "break-word",
                          whiteSpace: "normal",
                          wordBreak: "break-word",
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
            <Typography>No Quests available</Typography>
          )}
        </DialogContent>
      </Dialog>

      {/* Dialog za selektirani event */}
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
              wordWrap: "break-word",
              overflowWrap: "break-word",
              whiteSpace: "normal",
              wordBreak: "break-word",
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
            {isEditing ? (
              <>
                <TextField
                  label="Activity Title"
                  fullWidth
                  variant="outlined"
                  value={selectedEvent.eventName}
                  onChange={(e) =>
                    setSelectedEvent({
                      ...selectedEvent,
                      eventName: e.target.value,
                    })
                  }
                  sx={{ marginBottom: "1rem", marginTop: "1rem", wordWrap: "break-word",
                    overflowWrap: "break-word",
                    whiteSpace: "normal",
                    wordBreak: "break-word", }}
                />
                <TextField
                  label="Activity Details"
                  fullWidth
                  variant="outlined"
                  multiline
                  rows={4}
                  value={selectedEvent.description}
                  onChange={(e) =>
                    setSelectedEvent({
                      ...selectedEvent,
                      description: e.target.value,
                    })
                  }
                  sx={{ marginBottom: "1rem", wordWrap: "break-word",
                    overflowWrap: "break-word",
                    whiteSpace: "normal",
                    wordBreak: "break-word", }}
                />
                <Box
                  sx={{
                    display: "flex",
                    alignItems: "center",
                    marginBottom: "1rem",
                  }}
                >
                  <Typography
                    sx={{
                      marginRight: "1rem",
                      fontWeight: "bold",
                    }}
                  >
                    Select Color:
                  </Typography>
                  <input
                    type="color"
                    value={selectedEvent.color || "#ffffff"}
                    onChange={(e) =>
                      setSelectedEvent({
                        ...selectedEvent,
                        color: e.target.value,
                      })
                    }
                    style={{
                      width: "100%",
                      height: "32px",
                      border: "none",
                      cursor: "pointer",
                    }}
                  />
                </Box>
                <Button
                  variant="contained"
                  color="primary"
                  onClick={handleSaveChanges}
                  sx={{ marginTop: "1rem" }}
                >
                  Save Changes
                </Button>
                <Button
                  variant="outlined"
                  color="secondary"
                  onClick={handleBackToDetails}
                  sx={{ marginTop: "1rem", marginLeft: "1rem" }}
                >
                  Back to Details
                </Button>
              </>
            ) : (
              <>
                <Typography variant="body1"
                            sx={{wordWrap: "break-word",
                                  overflowWrap: "break-word",
                                  whiteSpace: "normal",
                                  wordBreak: "break-word",}}>
                  {selectedEvent.description || "Details not planned, yet!"}
                </Typography>
                <Button
                  variant="outlined"
                  color="primary"
                  onClick={handleEditDetails}
                  sx={{ marginTop: "1rem" }}
                >
                  Edit Details
                </Button>
                <Button
                  variant="outlined"
                  color="primary"
                  onClick={handleDetailDialogClose}
                  sx={{ marginTop: "1rem", marginLeft: "1rem"  }}
                >
                  Back
                </Button>
              </>
            )}

          </DialogContent>
        </Dialog>
      )}
    </Box>
  );
};

export default ShowAllEventsFromGroup;