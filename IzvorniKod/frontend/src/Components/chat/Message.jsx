import React from "react";

function Message(props) {
    return (
        <li className={props.isSentMsg?"message":"message sent-message"}>
            <div className="msg-sender">{props.msg.idSender}</div>
            <div className="msg-time">{props.msg.date}</div>
            <div className="msg-text">{props.msg.poruka}</div>
        </li>
    );
}
export default Message;