import React from "react";
import { useState, useEffect } from "react";
import Message from "./Message";
import AIEventMessage from "./AIEventMessage";
import useSocket from "./useSocket";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPaperPlane } from "@fortawesome/fontawesome-free-regular";
import "../../styles/Conversation.css"

const MINUTE = 60 * 1000;
function areSentWithin(date2, date1, milisec) {
  return (Date.parse(date2) - Date.parse(date1) <= milisec);
}

function Conversation(props) {
  const [messageList, setMessageList] = useState([]);
  const { newInMsg, isConnected, sendMsg } = useSocket(props.groupID, props.user.ID);
  const [newOutMsg, setNewOutMsg] = useState("");

  useEffect(() => {
    fetch(`/api/groups/${props.groupID}/getMessages`, {
      credentials : "include",
    })
    .then(data => data.json())
    .then(messageList => {
      console.log("lista", messageList);
      setMessageList(messageList);
    });
  }, [] );

  useEffect(() => {
    if (newInMsg.poruka != "") {
      setMessageList([...messageList, newInMsg]);
    }
  }, [newInMsg]);

  const sendNewMessage = () => {
    if (newOutMsg != "") {
      sendMsg(newOutMsg);
      setMessageList([...messageList, {
        idSender: props.user.ID,
        date: Date(),
        poruka: newOutMsg,
        username: props.user.name
      }]);
      setNewOutMsg("");
    }
  }
  const changeMsg = (evt) => {
    setNewOutMsg(evt.target.value);
  }

  return (
  <div className="chat">
    <h1>CHAT - {props.groupName}</h1>
      <ul> {
          messageList.map((msg, index, list) => {
            const showTime = (index == 0)? true : (msg.idSender != list[index-1].idSender || !areSentWithin(msg.date, list[index-1].date, 2*MINUTE));
            if(msg.username == "chatBot") {
              return (<AIEventMessage key={index} msg={msg} groupID={props.groupID} hasTime={true} displayTime={showTime}/>);
            } else {
              return (<Message key={index} msg={msg} isSentMsg={msg.idSender == props.user.ID} displayTime ={showTime}/>);
            }
          }
      )} </ul>
      <div className="controls">
        <textarea name="message-box" id="message-box" onChange={changeMsg} value={newOutMsg} placeholder="Napišite nešto lijepo..." rows="3"></textarea>
        <button id="send-button" onClick={sendNewMessage}>
          <FontAwesomeIcon icon={faPaperPlane} />
        </button>
      </div>
    </div>);
}
export default Conversation;
