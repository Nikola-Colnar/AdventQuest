import React, { useState, useEffect } from 'react';
import "./countdown.css"

function Countdown({ targetDate }) {
  const [timeLeft, setTimeLeft] = useState(calculateTimeLeft());

  function calculateTimeLeft() {
    const difference = new Date(targetDate) - new Date(); //razlika se izracuna u milisekundama
    if (difference > 0) {
      return {
        days: Math.floor(difference / (1000 * 60 * 60 * 24)), 
        hours: Math.floor((difference / (1000 * 60 * 60)) % 24),
        minutes: Math.floor((difference / (1000 * 60)) % 60),
        seconds: Math.floor((difference / 1000) % 60),
      };
    } else {
      return null;
    }
  }

  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft(calculateTimeLeft());
    }, 1000); //nakon svakih 1000 milisekundi se izvrsava funkcija

    return () => clearInterval(timer); //ako se unmounta, ukloni s ekrana, koristi druga stranica
  }, [targetDate]); //ako promjenimo datum, da se stvori novi timer

  if (!timeLeft) {
    return <div>Marry Christmas</div>; // Poruka kada vrijeme istekne
  }

  //dinamicko ispisivanje mnozine i jednine
  const getSecondsLabel = (time) => {
    return time === 1 ? 'second' : 'seconds';
  };
  const getMinutesLabel = (time) => {
    return time === 1 ? 'minute' : 'minutes';
  };
  const getHoursLabel = (time) => {
    return time === 1 ? 'hour' : 'hours';
  };
  const getYearsLabel = (days) => {
    return days === 1 ? 'day' : 'days';
  };
  return (
    <div className="countdown-container">
      <h1 className="countdown-title">Christmas Countdown</h1>
      <div className="countdown">
        <div className="time-box">
          <span className="time">{timeLeft.days}</span>
          <span className="label">{getYearsLabel(timeLeft.days)}</span>
        </div>
        <div className="time-box">
          <span className="time">{timeLeft.hours}</span>
          <span className="label">{getHoursLabel(timeLeft.hours)}</span>
        </div>
        <div className="time-box">
          <span className="time">{timeLeft.minutes}</span>
          <span className="label">{getMinutesLabel(timeLeft.minutes)}</span>
        </div>
        <div className="time-box">
          <span className="time">{timeLeft.seconds}</span>
          <span className="label">{getSecondsLabel(timeLeft.seconds)}</span>
        </div>
      </div>
    </div>
  );
}

export default Countdown;


