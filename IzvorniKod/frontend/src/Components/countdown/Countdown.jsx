import { useState, useEffect } from "react";
import "./countdown.css";
import PropTypes from "prop-types";


function Countdown({ targetDate }) {
  const [timeLeft, setTimeLeft] = useState(calculateTimeLeft());

  function calculateTimeLeft() {
    // razlika se izracuna u milisekundama
    const difference = new Date(targetDate) - new Date();
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
    // nakon svakih 1000 milisekundi se izvrsava izracun preostalog vremena
    const timer = setInterval(() => {
      setTimeLeft(calculateTimeLeft());
    }, 1000);
    return () => clearInterval(timer);
  }, [targetDate]);

  if (!timeLeft) {
    // poruka kada vrijeme istekne
    return <div>Marry Christmas</div>;
  }

  // dinamicko ispisivanje mnozine i jednine
  const getSecondsLabel = (time) => {
    return time === 1 ? "second" : "seconds";
  };

  const getMinutesLabel = (time) => {
    return time === 1 ? "minute" : "minutes";
  };

  const getHoursLabel = (time) => {
    return time === 1 ? "hour" : "hours";
  };

  const getYearsLabel = (days) => {
    return days === 1 ? "day" : "days";
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

Countdown.propTypes = {
  targetDate: PropTypes.string.isRequired,
};

export default Countdown;