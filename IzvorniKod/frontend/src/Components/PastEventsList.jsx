import { useState, useEffect, useCallback } from "react";
import {
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Avatar,
  Typography,
  Divider,
  Box,
  IconButton,
  Button,
  TextField,
} from "@mui/material";
import EventIcon from "@mui/icons-material/Event";
import ThumbUpIcon from "@mui/icons-material/ThumbUp";
import CommentIcon from "@mui/icons-material/Comment";

const PastEventList = () => {
  const [events, setEvents] = useState([]);
  const [likedEvents, setLikedEvents] = useState({}); //podaci o likeovima
  const [comments, setComments] = useState({}); //komentari po eventIdu
  const [commentInput, setCommentInput] = useState(""); //trenutni unos komentara
  const [selectedEventId, setSelectedEventId] = useState(null); //trenutno odabrani event za komentare

  const fetchEvents = useCallback(async () => {
    const groupId = localStorage.getItem("myGroupId");
    const username = localStorage.getItem("username");
    try {
      const response = await fetch(
        `http://localhost:8080/api/groups/${groupId}/getEvents`,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.ok) {
        const data = await response.json();
        const today = new Date(); //danasnji datum
        console.log(today);
        const formattedEvents = data
          .filter((event) => event.date && new Date(event.date).getTime() <= today) //micu se koji nemaju datum i koji su stariji od danas
          .map((event) => ({
            id: event.eventId,
            title: event.eventName,
            date: new Date(event.date),
            description: event.description,
            color: event.color || "#3174ad",
            likes: 0,
            userLiked: 0, //personalnilikeovi
          }))
          .sort((a, b) => b.date - a.date); //najnoviji prema starijima

        // Fetch likes and personal likes
        const likesResponse = await fetch(
          `http://localhost:8080/api/groups/${groupId}/getPastEvents/${username}`,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (likesResponse.ok) {
          const likesData = await likesResponse.json();

          // Add likes data to events
          const updatedEvents = formattedEvents.map((event) => {
            const eventLikes = likesData.find((e) => e.eventId === event.id);
            return {
              ...event,
              likes: eventLikes ? eventLikes.likes : 0,
              userLiked: eventLikes ? eventLikes.userLiked : 0,
            };
          });
          console.log(updatedEvents);
          setEvents(updatedEvents);
        } else {
          console.error("Failed to fetch likes");
        }
      } else {
        console.error("Failed to fetch events");
      }
    } catch (error) {
      console.error("Error fetching events:", error);
    }
  }, []);

  useEffect(() => {
    fetchEvents();
  }, [fetchEvents]);

  //upravljanje likeovima
  const handleLike = (eventId) => {
    setLikedEvents((prev) => ({
      ...prev,
      [eventId]: !prev[eventId],
    }));

    setEvents((prev) =>
      prev.map((event) =>
        event.id === eventId
          ? { ...event, likes: event.likes + (likedEvents[eventId] ? -1 : 1) }
          : event
      )
    );
//prop za kasniju implementaciju kada backend bude spreman
    const likeData = {
      eventId,
      liked: !likedEvents[eventId],
    };
    console.log("Like data to send:", likeData);
  };
  //prikaz komentara specificnog dogadaja
  const handleCommentsToggle = (eventId) => {
    setSelectedEventId((prev) => (prev === eventId ? null : eventId));
    if (!comments[eventId]) {
      setComments((prev) => ({
        ...prev,
        [eventId]: [],
      }));
    }
  };
  //slanje komentara
  const handleCommentSubmit = (eventId) => {
    if (!commentInput.trim()) return;
    const newComment = { id: Date.now(), text: commentInput };

    setComments((prev) => ({
      ...prev,
      [eventId]: [...prev[eventId], newComment],
    }));
    setCommentInput("");
    //koristim za slanje kasnije prema bakcendu
    const commentData = {
      eventId,
      comment: newComment.text,
    };
    console.log("Comment data to send:", commentData);
  };

  const isToday = (date) => {
    const today = new Date().setHours(0, 0, 0, 0);
    return date.setHours(0, 0, 0, 0) === today;
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "0 auto", padding: 2 }}>
      <Typography variant="h4" gutterBottom>
        Event List
      </Typography>
      <List>
        {events.map((event) => (
          <div key={event.id}>
            <ListItem
              alignItems="flex-start"
              sx={{
                border: isToday(event.date) ? "2px solid green" : "none",
                borderRadius: 2,
              }}
            >
              <ListItemAvatar>
                <Avatar sx={{ backgroundColor: event.color }}>
                  <EventIcon />
                </Avatar>
              </ListItemAvatar>
              <ListItemText
                primary={
                  <Typography
                    variant="h6"
                    sx={{
                      color: event.color,
                      fontWeight: "bold",
                    }}
                  >
                    {event.title}
                  </Typography>
                }
                secondary={
                  <>
                    <Typography
                      sx={{ display: "inline" }}
                      component="span"
                      variant="body2"
                      color="text.primary"
                    >
                      {event.date.toDateString()}
                    </Typography>
                    {" — " + event.description}
                  </>
                }
              />
              <Box sx={{ display: "flex", alignItems: "center" }}>
                <IconButton onClick={() => handleLike(event.id)}>
                  <ThumbUpIcon
                    sx={{ color: likedEvents[event.id] ? "#1976d2" : "gray" }}
                  />
                </IconButton>
                <Typography>{event.likes}</Typography>
                <Button
                  variant="outlined"
                  size="small"
                  startIcon={<CommentIcon />}
                  onClick={() => handleCommentsToggle(event.id)}
                  sx={{ marginLeft: 2 }}
                >
                  Comments
                </Button>
              </Box>
            </ListItem>
            {/*Sekcija za komentare za odredeni dogadaj otvara ili zatvara */}
            {selectedEventId === event.id && (
              <Box sx={{ marginLeft: 8, marginTop: 1 }}>
                <List>
                  {comments[event.id]?.map((comment) => (
                    <ListItem key={comment.id}>
                      <ListItemText primary={comment.text} />
                    </ListItem>
                  ))}
                </List>
                <TextField
                  label="Add a comment"
                  variant="outlined"
                  fullWidth
                  value={commentInput}
                  onChange={(e) => setCommentInput(e.target.value)}
                  sx={{ marginBottom: 2 }}
                />
                <Button
                  variant="contained"
                  onClick={() => handleCommentSubmit(event.id)}
                >
                  Submit
                </Button>
              </Box>
            )}
            <Divider variant="inset" component="li" />
          </div>
        ))}
      </List>
      {events.length === 0 && (
        <Typography variant="body1" sx={{ textAlign: "center", marginTop: 2 }}>
          No events available.
        </Typography>
      )}
    </Box>
  );
};

export default PastEventList;
