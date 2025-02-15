import { useState, useEffect, useRef, useCallback } from "react";
import { Calendar } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import "./CalendarComponent.css";
import PropTypes from "prop-types";


const CalendarComponent = ({ hideCalendar }) => {
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
        }));
        setEvents(formattedEvents);

        // azuriranje s novim dogadanjima
        if (calendarInstance.current) {
          calendarInstance.current.setOption('events', formattedEvents);
        }
      }else if(response.status == 401){
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
        alert(`
          Event: ${info.event.title}
          Start: ${info.event.start}
          End: ${info.event.end}
          Description: ${info.event.extendedProps.description || "N/A"}
        `);
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
  }, []);

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
    hideCalendar(false);
  };

  return (
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
                  // input za boje mijennja boju ovisno o selektiranoj boji
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
  );
};

CalendarComponent.propTypes = {
  hideCalendar: PropTypes.func.isRequired,
};

export default CalendarComponent;