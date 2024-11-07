import React, { useEffect, useState } from 'react';
import './snowfall.css';

const Snowflake = ({ style }) => {
  return (
    <p className="Snowflake" style={style}>
      ❄
    </p>
  );
};

const Snowfall = () => {
  const [snowflakes, setSnowflakes] = useState([]);

  useEffect(() => {
    const snowflakeArray = Array.from('❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄❄');
    
    const newSnowflakes = snowflakeArray.map((_, i) => {
      const animationDelay = `${(Math.random() * 25).toFixed(2)}s`;
      const fontSize = `${(Math.floor(Math.random() * 20) + 10)}px`;
      const leftPosition = `${Math.random() * 96 + 2}vw`; // Nasumična horizontalna pozicija od 0 do 100% širine ekrana
      const topPosition = `-${Math.random() * 50+10}vh`; // Pahuljice počinju iznad ekrana
      const animationDuration = `${(Math.random() * 40 + 20).toFixed(2)}s`;
      const rotateDuration = `${(Math.random() * 10 + 15).toFixed(2)}s`; // Nasumična rotacija traje između 10-20 sekundi
      const rotateDirection = Math.random() > 0.5 ? '1' : '-1'; // Nasumični smjer rotacije (1 za u smjeru kazaljke na satu, -1 za suprotno)


      const style = {
        animationDelay,
        fontSize,
        left: leftPosition, // Nasumična horizontalna pozicija
        animationDuration,
        top: topPosition,   // Pahuljica počinje iznad ekrana
        rotateDuration,
        '--rotateDirection': rotateDirection, // Dodajemo CSS varijablu za rotaciju
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