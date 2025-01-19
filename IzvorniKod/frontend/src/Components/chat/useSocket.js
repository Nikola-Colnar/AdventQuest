import { useCallback, useEffect, useState } from "react";
import { io } from "socket.io-client";
const SOCKET_HANDSHAKE_URI = "";

const useSocket = (group, userID) => {
  const [socket, setSocket] = useState();
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
  }, [group, socket]);

  useEffect(() => {
      const s = io({
        reconnection: false,
        query: `group=${group}`
      });
      console.log("novi socket: ", s);
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
      s.disconnect(); // cleanup funkcija, zatvori stari socket kad se vise ne koristi
      };
  }, [group, userID]);

  return { newInMsg, isConnected, sendMsg };
};

export default useSocket;