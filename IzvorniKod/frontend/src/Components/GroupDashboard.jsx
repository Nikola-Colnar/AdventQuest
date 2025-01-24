import { useState } from "react";
import {
  Box,
  Typography,
  Paper,
  Divider,
  Stack,
} from "@mui/material";
import CreateGroupButton from "./Pres_User_komponente/CreateGroupButton.jsx";
import SelectGroupForUserButton from "./Pres_User_komponente/SelectGroupForUserButton.jsx";
import AddUserToGroupButton from "./PresidentKomponente/AddUserToGroupButton.jsx";
import DeleteUserFromGroup from "./PresidentKomponente/DeleteUserFromGroup.jsx";
import GroupEventPresident from "./PresidentKomponente/GroupEventPresident.jsx";
import AdventCalendar from "./AdventCalendar/AdventCalendar";
import AddEvent from "./Pres_User_komponente/AddEvent.jsx";
import AddToCalendar from "./PresidentKomponente/AddToCalendar.jsx";
import EventProposal from "./Pres_User_komponente/EventProposals.jsx";
import PastEventsList from "./Pres_User_komponente/PastEventsList.jsx";
import Conversation from "./chat/Conversation";
import useIsPresident from "../hooks/useIsPresident";
import ShowAllEventsFromGroup from "./Pres_User_komponente/ShowAllEventsFromGroup.jsx";
import ShowAllUsersFromGroup from "./Pres_User_komponente/ShowAllUsersFromGroup.jsx";

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
      sx={{ maxWidth: '100%' }}>
      {/* Pocetni dio - biranje grupe ili stvaranje nove */}
      <Typography
        variant="h5"
        gutterBottom
        sx={{
          color: "#B22222",
          fontWeight: "bold",
          textAlign: "center",
          textShadow: "2px 2px 4px rgba(0, 0, 0, 0.3)",
          position: "relative",
          cursor: "default",
        }}
      >
        Select your Advent Group
      </Typography>

      <Box sx = {{backgroundColor: "transparent"}}mb={3} display="flex" gap={2}>
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
          <Typography
            variant="h3"
            sx={{
              textAlign: "center",
              fontWeight: "bold",
              color: "#8B0000",
              textShadow: "4px 4px 6px rgba(0, 0, 0, 0.4)",
              position: "relative",
              padding: "10px 0",
              cursor: "default",


            }}
          >
            {localStorage.getItem("myGroupName")}
          </Typography>
          {isPresident && (
            <Paper elevation={3} sx={{ p: 2 , zIndex: 12,
              display: "flex" ,
              flexDirection: "column",
              justifyContent: "space-between",
              flexWrap: "wrap",
              backgroundColor: "rgba(255,255,255,0.75)"

            }}>
              <Typography sx={{
                color: "#2E8B57",
                fontWeight: "bold",
                textAlign: "left",
                textShadow: "4px 4px 6px rgba(0, 0, 0, 0.4)",
                position: "relative",
                cursor: "default",
              }} variant="h5">President Panel</Typography>
              <Paper elevation={0} sx={{ p: 2, zIndex: 11,
                display: "flex" ,
                flexDirection: "row",
                justifyContent: "space-between",
                flexWrap: "wrap",
                backgroundColor: "rgba(255,255,255,0)",
                borderTop: "solid",
                borderColor: "rgba(16,165,16,0.02)",
                padding:0,
                paddingTop: "10px"
              }}>
              <AddUserToGroupButton />
              <DeleteUserFromGroup />
              <AddToCalendar refreshComponent={refreshComponent} refreshProp={refresh} />
              <GroupEventPresident />
              <AddEvent refreshComponent={refreshComponent}/>
              <EventProposal />
              </Paper>
            </Paper>
          )}
          {!isPresident && (
            <Paper elevation={3} sx={{ p: 2, zIndex: 11,
              display: "flex" ,
              flexDirection: "column",
              justifyContent: "space-between",
              flexWrap: "wrap",
              backgroundColor: "rgba(255,255,255,0.75)"
            }}>
              <Typography
                sx={{
                  color: "#2E8B57",
                  fontWeight: "bold",
                  textAlign: "left",
                  textShadow: "4px 4px 6px rgba(0, 0, 0, 0.4)",
                  position: "relative",
                  cursor: "default",
                }}
                variant="h5">User Panel</Typography>
              <Paper elevation={0} sx={{ p: 2, zIndex: 11,
                display: "flex" ,
                flexDirection: "row",
                justifyContent: "space-between",
                flexWrap: "wrap",
                backgroundColor: "rgba(255,255,255,0)",
                borderTop: "solid",
                borderColor: "rgba(16,165,16,0.02)",
                padding:0,
                paddingTop: "10px"
              }}>
              <AddEvent refreshComponent={refreshComponent}/>
              <EventProposal />
              <ShowAllEventsFromGroup></ShowAllEventsFromGroup>
              <ShowAllUsersFromGroup></ShowAllUsersFromGroup>
              </Paper>
            </Paper>
          )}

          {/* Chat Section */}
          <Paper elevation={3} sx={{ p: 2, zIndex: 11, backgroundColor: "rgba(255,255,255,0.75)"}}>
            <Typography
              sx={{
                color: "#2E8B57",
                fontWeight: "bold",
                textAlign: "left",
                textShadow: "4px 4px 6px rgba(0, 0, 0, 0.4)",
                position: "relative",

                cursor: "default",
              }}
              variant="h4">Group Chat</Typography>
            <Paper  elevation={4} sx={{ zIndex: 11, backgroundColor: "rgba(255,255,255,0.75)",
              marginTop: 2 }}>
            <Conversation

              groupName = {localStorage.getItem("myGroupName")}
              groupID={selectedGroupId}
              user={{ name: username, ID: userID }}
            />
              </Paper>
          </Paper>

          {/* Panel koji svi vide */}
          <Paper  elevation={3} sx={{ p: 2, zIndex: 11, backgroundColor: "rgba(255,255,255,0.75)" }}>
            <Typography variant="h4"
                        sx={{
                          color: "#2E8B57",
                          fontWeight: "bold",
                          textAlign: "left",
                          textShadow: "4px 4px 6px rgba(0, 0, 0, 0.4)",
                          position: "relative",
                          cursor: "default",
                        }}>
              Advent Calendar
            </Typography>
            <Paper  elevation={2} sx={{ p: 2, zIndex: 11, marginTop: "10px" }}>
            <AdventCalendar refreshProp={refresh} />
            </Paper>
            </Paper>
          <Paper  elevation={2} sx={{ p: 2, zIndex: 11, marginTop: "15px" , backgroundColor: "rgba(255,255,255,0.75)"}}>
            <Typography variant="h4"
                        sx={{
                          color: "#2E8B57",
                          fontWeight: "bold",
                          textAlign: "left",
                          textShadow: "4px 4px 6px rgba(0, 0, 0, 0.4)",
                          position: "relative",
                          cursor: "default",
                        }}>
              Conquered Quests
            </Typography>
            <Paper  elevation={3} sx={{ p: 2, zIndex: 11, backgroundColor: "rgba(255,255,255,0.75)" }}>
              <PastEventsList refreshProp={refresh} />
            </Paper>

          </Paper>
        </Stack>
      )}
    </Box>
  );
};

export default GroupDashboard;
