import React, { useCallback, useState } from "react";
import parseDate from "./ParseDate";
import "../../styles/Message.css";

function reformatDate(date) {
    const datum = date.split(".");
    var dateStandard = datum[2] + "-";  // Dodaje godinu i prvu crticu
    dateStandard += ((datum[1].length == 1) ? "0" : "") + datum[1] + "-";  // Dodaje mjesec i drugu crticu
    dateStandard += ((datum[0].length == 1) ? "0" : "") + datum[0];  // Dodaje dan
    return dateStandard;
}

function AIEventMessage(props) {
    const [active, setActive] = useState(true);
    const [resolveMessage, setResolveMessage] = useState("");
    const removeAIMessage = useCallback(
        async (msgID) => {
            try {
                const response = await fetch(`http://localhost:8080/api/groups/${props.groupID}/deleteAIMessage/${msgID}`, {
                    method: "POST",
                    credentials : "include",
                    headers: {
                        "Content-Type": "application/json",
                    }
                });
                if (response.ok) {
                    console.log("AI message successfully removed from database");
                } else {
                    console.error("Failed to remove AI message");
                }
            } catch (error) {
                console.error("Error:", error);
            }
        }
    );
    const acceptEvent = useCallback(
        async (event) => {
            const newEvent = {
                eventName: event[0],
                description: event[1],
                date: reformatDate(event[2]),
                color: "#a31515",
            };
            try {
                const response = await fetch(`http://localhost:8080/api/groups/${props.groupID}/addEvent`, {
                    method: "POST",
                    credentials : "include",
                    headers: {
                    "Content-Type": "application/json",
                    },
                    body: JSON.stringify(newEvent),
                });
                if (response.ok) {
                    console.log("Event successfully added");
                    setActive(false); // Zatvori dijalog
                    setResolveMessage("PRIHVAĆENO");
                    console.log("brisem AI poruku. id = ", props.msg.messageID);
                    removeAIMessage(props.msg.messageID);
                } else {
                    console.error("Failed to add event");
                }
            } catch (error) {
                console.error("Error:", error);
            }
        }
    );
    const rejectEvent = useCallback(
        (event) => {
            setActive(false); // Zatvori dijalog
            setResolveMessage("ODBIJENO");
            console.log("brisem AI poruku. id = ", props.msg.messageID);
            removeAIMessage(props.msg.messageID);
        }
    );

    const parts = props.msg.poruka.split("|");
    console.log(parts);
    const hasDate = parts[2].charAt(0) != "0";
    return (
        <li className={"message AI-message " + (props.displayTime?" ":" time-hidden")}>
            <div className="msg-sender">{props.msg.username}</div>
            <div className="prompt"> Would you like to add this activity?</div>
            <div className="msg-time">{parseDate(props.msg.date)}</div>
            <div className="msg-activity"><span>activity: </span>{parts[0]}</div>
            <div className="msg-description"><span>description: </span>{parts[1]}</div>
            <div className="msg-act-date"><span>date: </span>{hasDate?parts[2]:"no date"}</div>
            {active?
                <div className="msg-control">
                    <button className="da" onClick={()=>acceptEvent(parts)}>Yes</button>
                    <button className="ne" onClick={()=>rejectEvent(parts)}>No</button>
                </div>
                :
                <div className="resolutionMessage">{resolveMessage}</div>
            }
        </li>
    );
}
export default AIEventMessage;