import { useEffect, useState } from "react";
import { Card, CardContent, Typography, IconButton, Button, Modal, Box } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

const EventProposals = () => {
  const [events, setEvents] = useState([]);
  const [open, setOpen] = useState(false);

  const fetchEvents = () => {
    const groupId = localStorage.getItem("myGroupId");

    // Fetch current events to avoid duplication
    fetch(`http://localhost:8080/api/groups/${groupId}/getEvents`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((existingEvents) => {
        //funkcija za dohvat i filtriranje evenata
        const getEventProposals = (attempts = 0, collectedEvents = []) => {
          if (attempts >= 20 || collectedEvents.length >= 5) {
            console.log("Reached max attempts or 5 events");
            setEvents(collectedEvents); //finalna lista koja se prikazuje
            return; // ako ih je 5 stani
          }

          fetch("http://localhost:8080/api/groups/getEventProposals")
            .then((response) => {
              if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
              }
              return response.json();
            })
            .then((data) => {
              //Filtriram evente koje vec postoje u bazi
              const filteredEvents = data.filter(
                (event) =>
                  !existingEvents.some(
                    (existingEvent) => existingEvent.eventName === event
                  ) &&
                  !collectedEvents.some((collectedEvent) => collectedEvent.eventName === event)
              );

              //filtriraj dok ih nemas 5
              const newEvents = filteredEvents.slice(0, 5 - collectedEvents.length);

              const updatedCollectedEvents = [
                ...collectedEvents,
                ...newEvents.map((event) => ({
                  eventName: event,
                  added: false,
                })),
              ];


              getEventProposals(attempts + 1, updatedCollectedEvents);
            })
            .catch((error) => {
              console.error("Error fetching event proposals:", error);
            });
        };

        //retry logika
        getEventProposals();
      })
      .catch((error) => {
        console.error("Error fetching existing events:", error);
      });
  };

  useEffect(() => {
    fetchEvents();
  }, []);

  const handleAddEvent = (eventTitle) => {
    const groupId = localStorage.getItem("myGroupId");
    const event = {
      eventName: eventTitle,
      description: "Details not planned, yet!"
    };

    fetch(`http://localhost:8080/api/groups/${groupId}/addEvent`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(event),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log("Event added:", data);
        //state true ako smo ga sada dodali
        setEvents((prevEvents) =>
          prevEvents.map((evt) =>
            evt.eventName === eventTitle ? { ...evt, added: true } : evt
          )
        );
      })
      .catch((error) => {
        console.error("Error adding event:", error);
      });
  };

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <div>

      <Button
        variant="contained"
        color="primary"
        onClick={handleOpen}
        style={{ margin: "20px auto", display: "block" }}
      >
        Show Event Proposals
      </Button>

      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="event-proposals-modal"
        aria-describedby="event-proposals-description"
      >
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "background.paper",
            boxShadow: 24,
            p: 4,
            borderRadius: "8px",
          }}
        >
          <Typography id="event-proposals-modal" variant="h6" component="h2" gutterBottom>
            Event Proposals
          </Typography>
          <div>
            {events.map((event, index) => (
              <Card
                key={index}
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                  padding: "10px",
                }}
              >
                <CardContent>
                  <Typography variant="h6">{event.eventName}</Typography>
                </CardContent>
                {event.added ? (
                  <Typography style={{ color: "green", fontWeight: "bold" }}>
                    Added
                  </Typography>
                ) : (
                  <IconButton
                    color="primary"
                    onClick={() => handleAddEvent(event.eventName)}
                  >
                    <AddIcon />
                  </IconButton>
                )}
              </Card>
            ))}
          </div>

          <Button
            variant="contained"
            color="secondary"
            onClick={fetchEvents}
            style={{ marginTop: "20px" }}
          >
            Refresh Event Proposals
          </Button>
        </Box>
      </Modal>
    </div>
  );
};

export default EventProposals;