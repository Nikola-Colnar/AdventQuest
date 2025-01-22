import { useState, useEffect, useRef, useCallback } from "react";
import { Calendar } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import "./AddToCalendar.css";
import { Button } from "@mui/material";

const AddToCalendar = () => {
  const calendarRef = useRef(null);
  const calendarInstance = useRef(null);
  const [events, setEvents] = useState([]);
  const [allEvents, setAllEvents] = useState([]);
  const [selectedDate, setSelectedDate] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [eventDetails, setEventDetails] = useState(null); // Za detalje događaja
  const [calendarVisible, setCalendarVisible] = useState(false); // Novo stanje za prikaz kalendara

  const fetchEvents = useCallback(async () => {
    const groupId = localStorage.getItem("myGroupId");
    try {
      const response = await fetch(
        `http://localhost:8080/api/groups/${groupId}/getEvents`,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      if (response.ok) {
        const data = await response.json();
        const formattedEvents = data
          .filter((event) => event.date !== null)
          .map((event) => ({
            id: event.eventId,
            title: event.eventName,
            date: event.date,
            description: event.description,
            color: event.color || "#3174ad",
          }));
        setEvents(formattedEvents);
        setAllEvents(data);
        if (calendarInstance.current) {
          calendarInstance.current.setOption("events", formattedEvents);
        }
      } else {
        console.error("Failed to fetch events");
      }
    } catch (error) {
      console.error("Error fetching events:", error);
    }
  }, []);

  const updateEventDate = async (eventId, date) => {
    const groupId = localStorage.getItem("myGroupId");

    try {
      const response = await fetch(
        `http://localhost:8080/api/groups/${groupId}/setDate/${eventId}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ date }),
        }
      );
      if (response.ok) {
        fetchEvents();
      } else {
        console.error("Failed to update event date");
      }
    } catch (error) {
      console.error("Error updating event date:", error);
    }
  };

  useEffect(() => {
    if (!calendarRef.current || !calendarVisible) return; // Kalendar se inicijalizira samo ako je vidljiv

    calendarInstance.current = new Calendar(calendarRef.current, {
      plugins: [dayGridPlugin, interactionPlugin],
      initialView: "dayGridMonth",
      headerToolbar: {
        left: "prev,next",
        center: "title",
        right: "",
      },
      events: events,
      dayCellContent: (arg) => {
        const dayNumber = arg.dayNumberText; // Prikaz broja dana
        const eventDate = arg.date;
        const hasEvent = events.some(
          (event) =>
            new Date(event.date).toDateString() === eventDate.toDateString()
        );

        return {
          html: `
          <div class="custom-day-content">
            ${
            hasEvent
              ? `<button class="custom-remove-btn">-</button>`
              : `<button class="custom-add-btn">+</button>`
          }
            <span class="custom-day-number">${dayNumber}</span>
          </div>
        `,
        };
      },
      dayCellDidMount: (arg) => {
        const addBtn = arg.el.querySelector(".custom-add-btn");
        const removeBtn = arg.el.querySelector(".custom-remove-btn");

        if (addBtn) {
          addBtn.addEventListener("click", (e) => {
            e.stopPropagation(); // Sprječava otvaranje detalja događaja
            setSelectedDate(arg.date);
            setModalOpen(true);
            fetchEvents(); // Refetch događaja
          });
        }

        if (removeBtn) {
          removeBtn.addEventListener("click", (e) => {
            e.stopPropagation(); // Sprječava otvaranje detalja događaja
            const event = events.find(
              (e) =>
                new Date(e.date).toDateString() === arg.date.toDateString()
            );
            if (event) updateEventDate(event.id, null);
          });
        }
      },
      eventContent: (arg) => {
        return {
          html: `
          <div class="custom-event-content">
            <span class="event-title">${arg.event.title}</span>
          </div>
        `,
        };
      },
      eventClick: (info) => {
        setEventDetails({
          id: info.event.id,
          title: info.event.title,
          description:
            info.event.extendedProps.description ||
            "Just have as much fun as you can!",
          date: info.event.start.toDateString(),
        });
        fetchEvents(); // Refetch događaja
      },
    });

    calendarInstance.current.render();

    return () => {
      calendarInstance.current.destroy();
    };
  }, [events, calendarVisible]);

  useEffect(() => {
    if (calendarVisible) fetchEvents();
  }, [fetchEvents, calendarVisible]);

  return (
    <div className="calendar-container">
      {/* Gumb za otvaranje kalendara */}
      <Button onClick={() => setCalendarVisible(!calendarVisible)}>
        {calendarVisible ? "Close Calendar" : "Open Calendar"}
      </Button>

      {/* Prikaz kalendara ako je vidljiv */}
      {calendarVisible && <div ref={calendarRef}></div>}

      {modalOpen && (
        <div className="modal">
          <div className="modal-content">
            <h2>Select a Christmas Event</h2>
            <ul>
              {allEvents
                .filter((event) => event.date === null)
                .map((event) => (
                  <li key={event.eventId}>
                    <button
                      style={{ backgroundColor: event.color || "#27ae60" }}
                      onClick={() => {
                        updateEventDate(
                          event.eventId,
                          selectedDate.toLocaleDateString("en-CA")
                        );
                        setModalOpen(false);
                      }}
                    >
                      {event.eventName}
                    </button>
                  </li>
                ))}
            </ul>
            <button className="close-btn" onClick={() => setModalOpen(false)}>
              Close
            </button>
          </div>
        </div>
      )}

      {/* Popup za detalje eventa */}
      {eventDetails && (
        <div className="modal">
          <div className="modal-content">
            <h2>Event Details</h2>
            <p>
              <strong>Name:</strong> {eventDetails.title}
            </p>
            <p>
              <strong>Description:</strong> {eventDetails.description}
            </p>
            <p>
              <strong>Date:</strong> {eventDetails.date}
            </p>
            <button onClick={() => setEventDetails(null)}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default AddToCalendar;