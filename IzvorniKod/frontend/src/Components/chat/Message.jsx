import React from "react";
import "../../styles/Message.css";
import parseDate from "./ParseDate";

function Message(props) {
    return (
      <li
        className={"message " + (props.isSentMsg ? " sent-message" : " ") + (props.displayTime ? " " : " time-hidden")}>
          <div className="msg-time">{parseDate(props.msg.date)}</div>
          <div className="msg-sender">{props.msg.username}</div>
          <div className="msg-text">{props.msg.poruka}</div>
      </li>
    );
}

export default Message;