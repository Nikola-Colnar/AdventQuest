import CreateGroupButton from "./CreateGroupButton.jsx"
import AddUserToGroupButton from "./AddUserToGroupButton.jsx"
import React, { useState } from "react";
import SelectGroupForUserButton from "./SelectGroupForUserButton";
import useIsPresident from "../hooks/useIsPresident";
import Conversation from "./chat/Conversation.jsx";
import AdventCalendar from "./AdventCalendar/AdventCalendar.jsx"
import AddEvent from "./AddEvent.jsx"
import AddToCalendar from "./AddToCalendar.jsx"
import DeleteUserFromGroup from "./DeleteUserFromGroup.jsx"
import EventProposal from "./EventProposals.jsx"
import GroupEventPresident from "./GroupEventPresident.jsx"
import PastEventsList from "./PastEventsList.jsx"
import ShowAllEventsFromGroup from "./ShowAllEventsFromGroup.jsx"
import ShowAllUsersFromGroup from "./ShowAllUsersFromGroup.jsx"

const GroupDashboard = ({ username, userID }) => {
  const [selectedGroupId, setSelectedGroupId] = useState(localStorage.getItem("myGroupId") || null);

  // Hook se uvijek poziva, ali `useIsPresident` neće raditi ništa dok nema `selectedGroupId`
  const { isPresident, loading, error } = useIsPresident(username, selectedGroupId || -1);
  //promjena refresha, rerendera komponente
  const [refresh, setRefresh] = useState(0);

    //promjena refresha
  const refreshComponent = () => {
    setRefresh(prevState => prevState + 1);
    };


  return (
    <div>
      <h2>Group Management</h2>
      <CreateGroupButton />
      <SelectGroupForUserButton setSelectedGroupId={setSelectedGroupId} />

      {!selectedGroupId ? (
        <p>Please select a group to continue.</p>
      ) : loading ? (
        <p>Loading...</p>
      ) : error ? (
        <p>Error: {error}</p>
      ) : isPresident ? (
        <div>
          <h3>President Panel</h3>
          <p>You have access to president functionalities.</p>
          {/* Komponente samo za predsjednika */}
          <AddUserToGroupButton />
          <DeleteUserFromGroup />
          <GroupEventPresident />
          {/*Ovo su komponente koje imaju i predsjednik i obicni clanovi*/}
          <AdventCalendar refreshProp = {refresh}/>
          <AddEvent />
          <AddToCalendar refreshComponent={refreshComponent}/>
          <EventProposal />
          <PastEventsList refreshProp = {refresh}/>
          <Conversation groupID={selectedGroupId} user={{ name: username, ID: userID }} />
        </div>
      ) : (
        <div>
          <h3>Group Member Panel</h3>
          <p>You have access to member functionalities.</p>
          {/* Dodaj dodatne funkcionalnosti za obicno clana grupe */}
          <AdventCalendar />
          <AddEvent />
          <AddToCalendar />
          <EventProposal />
          <PastEventsList />
          <ShowAllEventsFromGroup />
          <Conversation groupID={selectedGroupId} user={{ name: username, ID: userID }} />
        </div>
      )}
    </div>
  );
};

export default GroupDashboard;
