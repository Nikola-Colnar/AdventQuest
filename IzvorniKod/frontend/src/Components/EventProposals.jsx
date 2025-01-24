import { useEffect, useState } from "react";
import { Card, CardContent, Typography, IconButton, Button, Modal, Box } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

const EventProposals = () => {
  const [events, setEvents] = useState([]);
  const [open, setOpen] = useState(false);

  const fetchEvents = async () => {
    const groupId = localStorage.getItem("myGroupId");

    try {
      //fetchanje evenata koji vec postoje (da se sprijece duplikacije)
      const response = await fetch(`http://localhost:8080/api/groups/${groupId}/getEvents`, { // ovo ne daje cookie
        credentials : "include",
      });
      const existingEvents = await response.json();
      if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout");
        window.location.href = "/logout";
        return;
      }

      //funkcija za dohvat i filtriranje evenata
      const getEventProposals = async (attempts = 0, collectedEvents = []) => {
        if (attempts >= 20 || collectedEvents.length >= 5) {
          console.log("Reached max attempts or 5 events");
          setEvents(collectedEvents); //finalna lista koja se prikazuje
          return; // ako ih je 5 stani
        }

        try {
          const response = await fetch("http://localhost:8080/api/groups/getEventProposals", {
            credentials : "include",
          });
          if(response.status == 401){
            console.log("Unauthorized: Redirecting to /logout")
            window.location.href = "/logout";
          }
          else if (!response.ok) {
            throw new Error("Failed to fetch event proposals.");
          }
          

          const data = await response.json();

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

          await getEventProposals(attempts + 1, updatedCollectedEvents);
        } catch (error) {
          console.error("Error fetching event proposals:", error.message);
        }
      };

      //retry logika
      await getEventProposals();
    } catch (error) {
      console.error("Error fetching existing events:", error.message);
      generateEventProposals();
    }
  };

//funkcija za slucaj ako nema kreiranih evenata
  const generateEventProposals = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/groups/getEventProposals", {
        credentials : "include",
      });
     if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
      }
      else if (!response.ok) {
        throw new Error("Failed to generate event proposals.");
      }
      const data = await response.json();
      const newEvents = data.slice(0, 5).map((event) => ({
        eventName: event,
        added: false,
      }));
      setEvents(newEvents);
    } catch (error) {
      console.error("Error generating event proposals:", error.message);
    }
  };
  useEffect(() => {
    fetchEvents();
  }, []);

  const handleAddEvent = async (eventTitle) => {
    const groupId = localStorage.getItem("myGroupId");
    const event = {
      eventName: eventTitle,
      description: "Details not planned, yet!",
    };
  
    try {
      const response = await fetch(`http://localhost:8080/api/groups/${groupId}/addEvent`, { // ovo ne daje cookie
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(event),
      });
  
      if (response.status == 401) {
        console.log("Unauthorized: Redirecting to /logout");
        window.location.href = "/logout";
        return; // Prekini izvršavanje funkcije nakon redirekcije
      }
  
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
  
      const data = await response.json();
      console.log("Event added:", data);
  
      // Ažurirajte stanje da označite da je događaj dodat
      setEvents((prevEvents) =>
        prevEvents.map((evt) =>
          evt.eventName === eventTitle ? { ...evt, added: true } : evt
        )
      );
    } catch (error) {
      console.error("Error adding event:", error);
    }
  };
  

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <div>

      <Button
        variant="contained"
        color="primary"
        onClick={handleOpen}
        style={{ display: "block", }}
      >
        Quest Suggestions
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
            Add Quests to Group List
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
            Refresh Suggestions
          </Button>
        </Box>
      </Modal>
    </div>
  );
};

export default EventProposals;