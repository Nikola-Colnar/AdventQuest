import PropTypes from "prop-types";
import useIsAdmin from "../hooks/useIsAdmin";
import { Box, Divider, Paper, Stack, Typography } from "@mui/material";
import AddUserToGroupButton from "./PresidentKomponente/AddUserToGroupButton.jsx";
import DeleteUserFromGroup from "./PresidentKomponente/DeleteUserFromGroup.jsx";
import AddToCalendar from "./PresidentKomponente/AddToCalendar.jsx";
import GroupEventPresident from "./PresidentKomponente/GroupEventPresident.jsx";
import AddEvent from "./Pres_User_komponente/AddEvent.jsx";
import EventProposal from "./Pres_User_komponente/EventProposals.jsx";
import DeleteUser from "./AdminKomponente/DeleteUser.jsx";
import Conversation from "./chat/Conversation.jsx";
import AdventCalendar from "./AdventCalendar/AdventCalendar.jsx";
import PastEventsList from "./Pres_User_komponente/PastEventsList.jsx";
import { useState } from "react";
import GetAllGroupsAdmin from "./AdminKomponente/GetAllGroupsAdmin.jsx";

const AdminRoute = ({ username, userID }) => {
  const { isAdmin, loading, error } = useIsAdmin(username);
  const [selectedGroupId, setSelectedGroupId] = useState(
    localStorage.getItem("myGroupId") || null
  );
  const [refresh, setRefresh] = useState(0);

  const refreshComponent = () => {
    setRefresh((prevState) => prevState + 1);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (isAdmin) {
    // Renderirajte komponente direktno
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
          Select Advent Group to Moderate or Manage Page Users
        </Typography>

        <Box sx = {{backgroundColor: "transparent"}}mb={3} display="flex" gap={2}>
          <GetAllGroupsAdmin setSelectedGroupId={setSelectedGroupId} />
          <DeleteUser></DeleteUser>
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
              <Paper elevation={3} sx={{ p: 2 , zIndex: 12,
                display: "flex" ,
                flexDirection: "row",
                justifyContent: "space-between",
                flexWrap: "wrap",

              }}>
                <Typography variant="h6">President panel</Typography>
                <AddUserToGroupButton />
                <DeleteUserFromGroup />
                <AddToCalendar refreshComponent={refreshComponent} refreshProp={refresh} />
                <GroupEventPresident />
                <AddEvent refreshComponent={refreshComponent}/>
                <EventProposal />
              </Paper>

            {/* Chat Section */}
            <Paper elevation={3} sx={{ p: 2, zIndex: 11}}>
              <Typography variant="h6">Group Chat</Typography>
              <Conversation
                groupID={selectedGroupId}
                user={{ name: username, ID: userID }}
              />
            </Paper>

            {/* Panel koji svi vide */}
            <Paper  elevation={3} sx={{ p: 2, zIndex: 11}}>
              <Typography variant="h6">
                Advent Calendar
              </Typography>
              <AdventCalendar refreshProp={refresh} />
              <PastEventsList refreshProp={refresh} />
            </Paper>
          </Stack>
        )}
      </Box>
    );
  }

  return <div>Unauthorized</div>;
};

AdminRoute.propTypes = {
  username: PropTypes.string.isRequired,
  children: PropTypes.node,
};

export default AdminRoute;
