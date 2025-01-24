import { useState, useEffect, useRef, useCallback } from "react";
import { Calendar } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction"; // Za podršku klikova
import "./AdventCalendar.css"; // Dodajte prilagođene stilove

const AdventCalendar = (refresh) => {
  const calendarRef = useRef(null);
  const calendarInstance = useRef(null);
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null); // Za pohranu podataka o odabranom događaju

  // Fetchanje događaja s backend-a
  const fetchEvents = useCallback(async () => {
    const groupId = localStorage.getItem("myGroupId");
    try {
      const response = await fetch(
        `http://localhost:8080/api/groups/${groupId}/getEvents`,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.ok) {
        const data = await response.json();
        const formattedEvents = data.map((event) => ({
          id: event.id,
          title: event.eventName,
          start: event.date,
          description: event.description,
          color: event.color || "#3174ad",
        }));
        setEvents(formattedEvents);

        // Ažuriranje događaja u kalendaru
        if (calendarInstance.current) {
          calendarInstance.current.setOption("events", formattedEvents);
        }
      } else {
        console.error("Failed to fetch events");
      }
    } catch (error) {
      console.error("Error fetching events:", error);
    }
  }, [refresh]);

  // Inicijalizacija kalendara
  useEffect(() => {
    if (!calendarRef.current) return;

    calendarInstance.current = new Calendar(calendarRef.current, {
      plugins: [dayGridPlugin, interactionPlugin],
      initialView: "dayGridMonth",
      headerToolbar: {
        left: "prev,next",
        center: "title",
        right: "",
      },
      events: events,
      eventContent: (arg) => {
        const eventDate = new Date(arg.event.start);
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Postavljanje trenutnog datuma na 00:00 (bez vremena)

        let content;
        if (eventDate > today) {
          // Ako je događaj u budućnosti, prikazuje ikonu (npr. "zaključan")
          content = `
            <div class="fc-daygrid-day-content locked-day">
              <span class="fc-daygrid-day-number-sticky locked-day">${eventDate.getDate()}</span>
              <div class="fc-event-card locked" >
                <span class="locked-icon">🔒</span>
              </div>
            </div>
          `;
        } else {
          // Ako događaj nije u budućnosti, prikazuje naziv događaja
          content = `
            <div class="fc-daygrid-day-content">
              <span class="fc-daygrid-day-number-sticky">${eventDate.getDate()}</span>
              <div class="fc-event-card" style="background-color: ${arg.event.backgroundColor}; color: white;">
                ${arg.event.title}
              </div>
            </div>
          `;
        }

        return { html: content };
      },
      eventClick: (info) => {
        const eventDate = new Date(info.event.start);
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Postavljanje trenutnog datuma na 00:00 (bez vremena)

        // Ako je događaj u budućnosti, spriječiti otvaranje detalja
        if (eventDate > today) {
          // Preventing the default action (opening event details)
          info.jsEvent.preventDefault();
          return;
        }

        // Postavljanje podataka o događaju za prikaz u prozoru za detalje eventa
        setSelectedEvent({
          title: info.event.title,
          date: info.event.start.toDateString(),
          description: info.event.extendedProps.description || "Just have as much fun as you can!",
        });
      },
      height: "auto",
    });

    calendarInstance.current.render();

    return () => {
      calendarInstance.current.destroy();
    };
  }, [events]);

  useEffect(() => {
    fetchEvents();
  }, [fetchEvents]);

  return (
    <div className="calendar-container">
      <div ref={calendarRef}></div>
      {selectedEvent && (
        <div className="modal">
          <div className="modal-content">
            <h2>Quest To Do</h2>
            <p><strong>Quest Name: </strong> {selectedEvent.title}</p>
            <p><strong>Details:</strong> {selectedEvent.description}</p>
            <p><strong>Date:</strong> {selectedEvent.date}</p>

            <button onClick={() => setSelectedEvent(null)}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdventCalendar;