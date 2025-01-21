import React from "react";
import "../../styles/Message.css";

const MINUTE = 60*1000;
const DAY = MINUTE*60*24;
const MONTH = 30*DAY;

function parseDate(dateString) {
    const date = new Date(dateString);
    let rez = date.getHours() + ":" + date.getMinutes()
    let current = new Date();
    let d = Math.floor((current-MINUTE*current.getTimezoneOffset())/DAY) - Math.floor((date-MINUTE*date.getTimezoneOffset() )/DAY);
    if(d > 0) {
        rez += ", " + d + ((d==1)?" day ago":" days ago");
    }
    return rez;
}

function Message(props) {
    return (
        <li className={props.isSentMsg?"message sent-message":"message"}>
            <div className="msg-sender">{props.msg.idSender}</div>
            <div className="msg-time">{parseDate(props.msg.date)}</div>
            <div className="msg-text">{props.msg.poruka}</div>
        </li>
    );
}
export default Message;