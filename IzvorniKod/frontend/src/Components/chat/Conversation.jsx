import React from "react";
import { useState, useEffect } from "react";
import Message from "./message";
import useSocket from "./useSocket";

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
    setMessageList([...messageList, newInMsg]);
  }, [newInMsg]);

  const sendNewMessage = () => {
    if(newOutMsg != "") {
      sendMsg(newOutMsg);
      setNewOutMsg("");
    }
  }
  const changeMsg = (evt) => {
    setNewOutMsg(evt.target.value);
  }

  return (<>
    <h1>Chat za grupu {props.groupID}</h1>
      <ul> {
          messageList.map(msg => {
            <Message msg={msg} color={"lightblue"} isSentMsg={msg.sender == props.user.ID}/>
          })
      } </ul>
      <textarea name="message-box" id="message-box" onChange={changeMsg} value={newOutMsg} placeholder="type your message..."></textarea>
      <button id="send-button" onClick={sendNewMessage}>send</button>
  </>);
}
export default Conversation;