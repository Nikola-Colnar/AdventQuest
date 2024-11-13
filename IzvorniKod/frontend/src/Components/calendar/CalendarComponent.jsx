import { useState, useEffect, useRef } from 'react';
import { Calendar } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import './CalendarComponent.css';
import { Button, Dialog } from "@mui/material";

const CalendarComponent = () => {
    const calendarRef = useRef(null);
    const [events, setEvents] = useState([]);
    const [showEventForm, setShowEventForm] = useState(false);
    const [eventDetails, setEventDetails] = useState({
        title: '',
        description: '',
        start: '',
        end: '',
        color: '#007bff',  // Default color for events
    });

    useEffect(() => {
        const calendar = new Calendar(calendarRef.current, {
            plugins: [dayGridPlugin, timeGridPlugin, listPlugin],
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,listWeek',
            },
            events: events, // Load events from state
        });
        calendar.render();

        return () => {
            calendar.destroy();
        };
    }, [events]);

    const handleEventFormChange = (e) => {
        setEventDetails({
            ...eventDetails,
            [e.target.name]: e.target.value,
        });
    };

    const handleEventSubmit = (e) => {
        e.preventDefault();
        setEvents([
            ...events,
            {
                title: eventDetails.title,
                start: eventDetails.start,
                end: eventDetails.end,
                description: eventDetails.description,
                color: eventDetails.color,
            },
        ]);
        setShowEventForm(false);
    };

    return (
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
          
    );
};

export default CalendarComponent;