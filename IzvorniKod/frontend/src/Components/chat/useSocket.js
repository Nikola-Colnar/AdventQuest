import { useCallback, useEffect, useState } from "react";
import { io } from "socket.io-client";
const SOCKET_HANDSHAKE_URI = "";

const useSocket = (group, userID) => {
  const [socket, setSocket] = useState(null);
  const [newInMsg, setNewInMsg] = useState({
      idSender: "",
      date: null,
      poruka: ""
    });
  const [isConnected, setConnected] = useState(false);
  const sendMsg = useCallback((text) => {
      socket.emit("send_message", {
          poruka: text,
          idSender: userID
    });
  }, [socket, group]);

  useEffect(() => {
      const s = io({
        reconnection: false,
        query: `group=${group}`
      });
      console.log("novi socket: ", s);
      if(socket !==null) {
        socket.disconnect();
      }
      setSocket(s);
      s.on("connect", () => setConnected(true));
      s.on("get_message", (data) => {
      setNewInMsg({
          idSender: data.idSender,
          date: data.date,
          poruka: data.poruka
        });
      });
      return () => {
        console.log("zatvoren socket ");
        s.disconnect(); // cleanup funkcija, zatvori stari socket kad se vise ne koristi
      };
  }, [group]);

  return { newInMsg, isConnected, sendMsg };
};

export default useSocket;