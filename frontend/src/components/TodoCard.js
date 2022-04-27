import React, { useState } from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import DeleteIcon from "@mui/icons-material/Delete";
import Input from "@mui/material/Input";
import { CardActions } from "@mui/material";
import {
  DONE_TODO,
  DELETE_TODO,
  UPDATE_TODO,
} from "../context/constants/ActionConstants";
import axios from "axios";

const TodoCard = ({ id, content, completed, dispatch }) => {
  const [editing, setEditing] = useState(false);
  const [editContent, setEditContent] = useState(content);

  const handleChange = (e) => {
    setEditContent(e.target.value);
  };

  const handleDone = async (e) => {
    e.preventDefault();
    const todo_id = e.target.todo_id.value;

    const { data, status } = await axios.put(`/api/todos/complete/${todo_id}`, {
      withCredentials: true,
    });
    if (status === 200) {
      dispatch({ type: DONE_TODO, payload: data });
    }
  };

  const handleDelete = async (e) => {
    e.preventDefault();
    const todo_id = e.target.todo_id.value;
    const { data } = await axios.delete(`/api/todos/delete/${todo_id}`, {
      withCredentials: true,
    });
    dispatch({ type: DELETE_TODO, payload: data });
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    const todo_id = e.target.todo_id.value;
    const updatedTodo = { content: e.target.editContent.value };
    const { data, status } = await axios.put(
      `/api/todos/update/${todo_id}`,
      updatedTodo,
      {
        headers: { "Content-type": "application/json" },
        withCredentials: true,
      }
    );

    if (status === 200) {
      setEditing(false);
      dispatch({ type: UPDATE_TODO, payload: data });
    }
  };

  return (
    <Card sx={{ minWidth: 275, marginBottom: "0.5rem" }}>
      <CardContent className="card-content">
        <Typography
          sx={{ fontSize: 20, marginTop: "0.7rem" }}
          style={{ textDecoration: completed ? "line-through" : "none" }}
          color="text.secondary"
        >
          {editing ? (
            <Input
              type="text"
              onChange={(e) => handleChange(e)}
              value={editContent}
            />
          ) : (
            content
          )}
        </Typography>
        <CardActions>
          {/* <form onSubmit={(e) => handleDone(e)}>
            <input type="hidden" value={id} name="todo_id" />
            <Button type="submit" size="small">
              <DoneIcon />
            </Button>
          </form>
          <form onSubmit={(e) => handleUpdate(e)}>
            <input type="hidden" value={id} name="todo_id" />
            <input type="hidden" value="Why you do this" name="editContent" />
            <Button type={editing ? "submit" : "button"} size="small">
              {editing ? (
                <SaveIcon />
              ) : (
                <EditIcon onClick={() => setEditing(true)} />
              )}
            </Button>
          </form> */}

          <form onSubmit={(e) => handleDelete(e)}>
            <input type="hidden" value={id} name="todo_id" />
            <Button type="submit" size="small">
              <DeleteIcon />
            </Button>
          </form>
        </CardActions>
      </CardContent>
    </Card>
  );
};

export default TodoCard;
