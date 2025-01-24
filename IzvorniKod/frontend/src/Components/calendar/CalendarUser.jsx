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

        // Azuriranje s novim događanjima
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
        // Prikaz informacija o događaju bez opcije za brisanje
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
        meridiem: "short", // Za "AM" i "PM" - u cssu po želji promijeniti malo ili veliko slovo
      },
    });

    calendarInstance.current.render();

    return () => {
      calendarInstance.current.destroy();
    };
  }, [isCalendarVisible]);

  useEffect(() => {
    fetchEvents();
  }, [fetchEvents]);

  // Funkcija za zatvaranje kalendara
  const handleCloseButtonClick = () => {
    setIsCalendarVisible(false); // Sakrij kalendar
  };

  const handleOpenButtonClick = () => {
    setIsCalendarVisible(true); // Prikazi kalendar
  };

  return (
    <div>
      <button className="open-btn" onClick={handleOpenButtonClick}>User Calendar</button>

      {/* Cijeli calendar-wrapper sakriven dok isCalendarVisible nije true */}
      {isCalendarVisible && (
        <div className="calendar-wrapper">
          <button className="close-btn" onClick={handleCloseButtonClick}>✖</button>
          <div className="calendar-container">
            <div ref={calendarRef}></div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CalendarLeader;