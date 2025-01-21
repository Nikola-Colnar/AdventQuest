import React from "react";
import { useState, useEffect } from "react";
import Message from "./Message";
import useSocket from "./useSocket";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPaperPlane } from "@fortawesome/fontawesome-free-regular";
import "../../styles/Conversation.css"

function Conversation(props) {
  const [messageList, setMessageList] = useState([]);
  const { newInMsg, isConnected, sendMsg } = useSocket(props.groupID, props.user.ID);
  const [newOutMsg, setNewOutMsg] = useState("");

  useEffect(() => {
    fetch(`/api/groups/${props.groupID}/getMessages`)
    .then(data => data.json())
    .then(messageList => {
      console.log("lista", messageList);
      setMessageList(messageList);
    });
  }, [] );

  useEffect(() => {
    if(newInMsg.poruka != "") {
      setMessageList([...messageList, newInMsg]);
    }
  }, [newInMsg]);

  const sendNewMessage = () => {
    if(newOutMsg != "") {
      sendMsg(newOutMsg);
      setMessageList([...messageList, {
        idSender: props.user.ID,
        date: Date(),
        poruka: newOutMsg
      }]);
      setNewOutMsg("");
    }
  }
  const changeMsg = (evt) => {
    setNewOutMsg(evt.target.value);
  }

  return (
  <div className="chat">
    <h1>Chat za grupu {props.groupID}</h1>
      <ul> {
          messageList.map((msg, index) => <Message key={index} msg={msg} isSentMsg={msg.idSender == props.user.ID}/>)
      } </ul>
      <div className="controls">
        <textarea name="message-box" id="message-box" onChange={changeMsg} value={newOutMsg} placeholder="Napišite nešto lijepo..." rows="3"></textarea>
        <button id="send-button" onClick={sendNewMessage}>
          <FontAwesomeIcon icon={faPaperPlane} />
        </button>
      </div>
      </div>);
}
export default Conversation;