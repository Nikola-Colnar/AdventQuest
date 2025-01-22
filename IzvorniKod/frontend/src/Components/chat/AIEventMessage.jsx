import React from "react";
import parseDate from "./ParseDate";
import "../../styles/Message.css";

function AIEventMessage(props) {
    const parts = props.msg.poruka.split("|");
    console.log(parts);
    const hasDate = parts[2].charAt(0) != "0";
    return (
        <li className={"message AI-message " + (props.displayTime?" ":" time-hidden")}>
            <div className="msg-sender">{props.msg.username}</div>
            <div className="prompt"> Želite li dodati ovu aktivnost?</div>
            <div className="msg-time">{parseDate(props.msg.date)}</div>
            <div className="msg-activity"><span>aktivnost: </span>{parts[0]}</div>
            <div className="msg-description"><span>opis: </span>{parts[1]}</div>
            <div className="msg-act-date"><span>datum: </span>{hasDate?parts[2]:"no date"}</div>
            <div className="msg-control">
                <button className="da">DA</button>
                <button className="ne">NE</button>
            </div>
        </li>
    );
}
export default AIEventMessage;