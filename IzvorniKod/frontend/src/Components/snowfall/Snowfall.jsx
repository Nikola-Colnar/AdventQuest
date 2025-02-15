import { useEffect, useState } from "react";
import PropTypes from "prop-types";
import "./snowfall.css";


const Snowflake = ({ style }) => {
  return (
    <p className="Snowflake" style={style}>
      ❄
    </p>
  );
};

Snowflake.propTypes = {
  style: PropTypes.object.isRequired,
};

const Snowfall = () => {
  const [snowflakes, setSnowflakes] = useState([]);

  useEffect(() => {
    const snowflakeArray = Array.from({ length: 65 });

    const newSnowflakes = snowflakeArray.map((_, i) => {
      const animationDelay = `${(Math.random() * 25).toFixed(2)}s`;
      const fontSize = `${(Math.floor(Math.random() * 20) + 10)}px`;
      // nasumicna horizontalna pozicija od 0 do 100% sirine ekrana
      const leftPosition = `${Math.random() * 94 + 2}vw`;
      // pahuljice pocinju iznad ekrana
      const topPosition = `-${Math.random() * 50 + 10}vh`;
      const animationDuration = `${(Math.random() * 40 + 20).toFixed(2)}s`;
      // nasumicna rotacija traje izmedu 10-20 sekundi
      const rotateDuration = `${(Math.random() * 10 + 15).toFixed(2)}s`;
      // nasumicni smjer rotacije (1 za u smjeru kazaljke na satu, -1 za suprotno)
      const rotateDirection = Math.random() > 0.5 ? "1" : "-1";

      const style = {
        zIndex: 10,
        animationDelay,
        fontSize,
        left: leftPosition, // nasumicna horizontalna pozicija
        animationDuration,
        top: topPosition,   // pahuljica pocinje iznad ekrana
        rotateDuration,
        // dodajemo CSS varijablu za rotaciju
        "--rotateDirection": rotateDirection,
      };

      return { id: i, style };
    });

    setSnowflakes(newSnowflakes);
  }, []);

  return (
    <div className="snow-container">
      {snowflakes.map((snowflake) => (
        <Snowflake key={snowflake.id} style={snowflake.style} />
      ))}
    </div>
  );
};

export default Snowfall;