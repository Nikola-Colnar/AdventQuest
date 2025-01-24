import React, { useState } from "react";
import {
  Box,
  Typography,
  Paper,
  Divider,
  Stack,
} from "@mui/material";
import CreateGroupButton from "./CreateGroupButton";
import SelectGroupForUserButton from "./SelectGroupForUserButton";
import AddUserToGroupButton from "./AddUserToGroupButton";
import DeleteUserFromGroup from "./DeleteUserFromGroup";
import GroupEventPresident from "./GroupEventPresident";
import AdventCalendar from "./AdventCalendar/AdventCalendar";
import AddEvent from "./AddEvent";
import AddToCalendar from "./AddToCalendar";
import EventProposal from "./EventProposals";
import PastEventsList from "./PastEventsList";
import Conversation from "./chat/Conversation";
import useIsPresident from "../hooks/useIsPresident";

const GroupDashboard = ({ username, userID, refreshHeader }) => {
  const [selectedGroupId, setSelectedGroupId] = useState(
    localStorage.getItem("myGroupId") || null
  );
  const { isPresident, loading, error } = useIsPresident(username, selectedGroupId || -1);
  const [refresh, setRefresh] = useState(0);

  const refreshComponent = () => {
    setRefresh((prevState) => prevState + 1);
  };

  return (
    <Box
      paddingTop={2}
      display="flex"
      flexDirection="column"
      alignItems="center"
      sx={{ zIndex: 11, maxWidth: '100%' }}>
      {/* Pocetni dio - biranje grupe ili stvaranje nove */}
      <Typography variant="h4" gutterBottom>
        Group Management
      </Typography>

      <Box mb={3} display="flex" gap={2}>
        <CreateGroupButton setSelectedGroupId={setSelectedGroupId} refreshHeader={refreshHeader} />
        <SelectGroupForUserButton setSelectedGroupId={setSelectedGroupId} refreshHeader={refreshHeader} />
      </Box>

      <Divider sx={{ width: "80%" }} />

      {!selectedGroupId ? (
        <Typography variant="h6" color="textSecondary" mt={3}>
          Please select a group to continue.
        </Typography>
      ) : loading ? (
        <Typography variant="h6" mt={3}>
          Loading...
        </Typography>
      ) : error ? (
        <Typography variant="h6" color="error" mt={3}>
          Error: {error}
        </Typography>
      ) : (
        <Stack spacing={3} mt={3} sx={{ width: "80%" }}>
          {/* Panel samo koji predsjednik vidi */}
          {isPresident && (
            <Paper elevation={3} sx={{ p: 2 ,
              display: "flex" ,
              flexDirection: "row",
              justifyContent: "space-between"
            }}>
              <Typography variant="h6">President Panel</Typography>
              <AddUserToGroupButton />
              <DeleteUserFromGroup />
              <GroupEventPresident />
              <AddToCalendar refreshComponent={refreshComponent} refreshProp={refresh} />
            </Paper>
          )}

          {/* Chat Section */}
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h6">Group Chat</Typography>
            <Conversation
              groupID={selectedGroupId}
              user={{ name: username, ID: userID }}
            />
          </Paper>

          {/* Panel koji svi vide */}
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h6">
              Advent Calendar
            </Typography>
            <AdventCalendar refreshProp={refresh} />
            <Box sx={{display: "flex", flexDirection:"row"}}>
            <AddEvent refreshComponent={refreshComponent}/>
            <EventProposal />
            </Box>
            <PastEventsList refreshProp={refresh} />
          </Paper>
        </Stack>
      )}
    </Box>
  );
};

export default GroupDashboard;
