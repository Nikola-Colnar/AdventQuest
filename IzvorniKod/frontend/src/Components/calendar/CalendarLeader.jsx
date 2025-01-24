import { useState, useEffect, useRef, useCallback } from "react";
import { Calendar } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import "./CalendarComponent.css";


const CalendarLeader = () => {
  const calendarRef = useRef(null);
  const calendarInstance = useRef(null); // Referenca na instancu kalendara
  const [events, setEvents] = useState([]);
  const [showEventForm, setShowEventForm] = useState(false);
  const [eventDetails, setEventDetails] = useState({
    title: "",
    description: "",
    start: "",
    end: "",
    color: "#a31515",
  });

  const [isCalendarVisible, setIsCalendarVisible] = useState(false); // Za kontrolu prikaza kalendara

  // fetchanje s backenda
  const fetchEvents = useCallback(async () => {
    const groupId = localStorage.getItem("myGroupId");
    try {
      const response = await fetch(`http://localhost:8080/api/groups/${groupId}/getEvents`, {
        headers: {
          "Content-Type": "application/json",
        },
        credentials : "include",
      });
      if (response.ok) {
        const data = await response.json();
        const formattedEvents = data.map((event) => ({
          title: event.eventName,
          start: event.startDate,
          end: event.endDate,
          description: event.description,
          color: event.color,
          eventId: event.eventId,
        }));
        setEvents(formattedEvents);

        // azuriranje s novim dogadanjima
        if (calendarInstance.current) {
          calendarInstance.current.setOption('events', formattedEvents);
        }
      }
      else if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
      } else {
        console.error("Failed to fetch events");
      }
    } catch (error) {
      console.error("Error fetching events:", error);
    }
  }, []);


  // Inicijalizacija kalendara (jednom)
  useEffect(() => {
    if (!calendarRef.current) return;

    calendarInstance.current = new Calendar(calendarRef.current, {
      plugins: [dayGridPlugin, timeGridPlugin, listPlugin],
      initialView: "dayGridMonth",
      headerToolbar: {
        left: "prev,next today",
        center: "title",
        right: "dayGridMonth,timeGridWeek,listWeek",
      },
      events: events,
      eventClick: (info) => {
        const idEvent = info.event.extendedProps.eventId;
        console.log(idEvent);
        if (window.confirm(`
        Event: ${info.event.title}
        Start: ${info.event.start}
        End: ${info.event.end}
        Description: ${info.event.extendedProps.description || "N/A"}
        
        Do you want to delete this event?
      `)) {
          handleDeleteEvent(idEvent);
        }
      },
      eventTimeFormat: {
        hour: "numeric",
        minute: "2-digit",
        meridiem: "short", // Za "AM" i "PM" - u cssu po zelji promijeniti malo ili veliko slovo
      },
    });

    calendarInstance.current.render();

    return () => {
      calendarInstance.current.destroy();
    };
  }, [isCalendarVisible]);
  //delete handler
  const handleDeleteEvent = async (idEvent) => {
    const groupId = localStorage.getItem("myGroupId");

    try {
      const response = await fetch(`http://localhost:8080/api/groups/${groupId}/deleteEvent/${idEvent}`, {
        method: "DELETE",
        credentials : "include",
        headers: {
          "Content-Type": "application/json",
        },
      });
      console.log(response)
      if (response.ok) {
        console.log("Event deleted successfully");
        await fetchEvents(); // Osvježavanje događaja
      }
      else if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
      } else {
        console.error("Failed to delete event");
      }
    } catch (error) {
      console.error("Error deleting event:", error);
    }
  };
  useEffect(() => {
    fetchEvents();
  }, [fetchEvents]);

  // funkcije za dodavanje dogadaja u kalendar
  const handleEventFormChange = (e) => {
    setEventDetails({
      ...eventDetails,
      [e.target.name]: e.target.value,
    });
  };

  const handleEventSubmit = async (e) => {
    e.preventDefault();

    // groupId
    const groupId = localStorage.getItem("myGroupId"); // Zamijeni sa stvarnim ID-om grupe

    const newEvent = {
      eventName: eventDetails.title,
      StartDate: eventDetails.start,
      EndDate: eventDetails.end,
      description: eventDetails.description,
      color: eventDetails.color,
      //groupId: groupId, // Povezivanje događaja s grupom
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
        //ako je u redu dohvacamo s backenda podatke
        await fetchEvents();
        setShowEventForm(false);
        setEventDetails({
          title: "",
          description: "",
          start: "",
          end: "",
          color: "#a31515",
        });

      }
      else if(response.status == 401){
        console.log("Unauthorized: Redirecting to /logout")
        window.location.href = "/logout";
      } else {
        console.error("Failed to save event to backend");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  // funkcija za zatvaranje kalendara kada je otvoren
  const handleCloseButtonClick = () => {
    setIsCalendarVisible(false); //sakrij kalendar
  };

  const handleOpenButtonClick = () => {
    setIsCalendarVisible(true); // prikazi kalendar
  };

  return (
    <div>
      {/* Dugme van kalendara koje otvara cijeli calendar-wrapper */}
      <button className="open-btn" onClick={handleOpenButtonClick}>Leader Calendar</button>

      {/* Cijeli calendar-wrapper sakriven dok isCalendarVisible nije true */}
      {isCalendarVisible && (
        <div className="calendar-wrapper">
          <button className="close-btn" onClick={handleCloseButtonClick}>✖</button>
          <div className="calendar-container">
            <div ref={calendarRef}></div>
            <button className="add-event-btn" onClick={() => setShowEventForm(true)}>
              Add Event
            </button>

            {showEventForm && (
              <div className="event-form">
                <h3>Add Event</h3>
                <form onSubmit={handleEventSubmit}>
                  <label>
                    Event Title:
                    <input
                      type="text"
                      name="title"
                      value={eventDetails.title}
                      onChange={handleEventFormChange}
                      required
                    />
                  </label>
                  <label>
                    Event Description:
                    <textarea
                      name="description"
                      value={eventDetails.description}
                      onChange={handleEventFormChange}
                    ></textarea>
                  </label>
                  <label>
                    Start Time:
                    <input
                      type="datetime-local"
                      name="start"
                      value={eventDetails.start}
                      onChange={handleEventFormChange}
                      required
                    />
                  </label>
                  <label>
                    End Time:
                    <input
                      type="datetime-local"
                      name="end"
                      value={eventDetails.end}
                      onChange={handleEventFormChange}
                      required
                    />
                  </label>
                  <label>
                    Event Color:
                    <input
                      type="color"
                      name="color"
                      value={eventDetails.color}
                      onChange={handleEventFormChange}
                      style={{
                        backgroundColor: eventDetails.color,
                        border: "1px solid #ccc",
                        borderRadius: "5px",
                        cursor: "pointer",
                      }}
                    />
                  </label>
                  <button type="submit">Save Event</button>
                </form>
                <button className="cancel-btn" onClick={() => setShowEventForm(false)}>
                  Cancel
                </button>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};


export default CalendarLeader;