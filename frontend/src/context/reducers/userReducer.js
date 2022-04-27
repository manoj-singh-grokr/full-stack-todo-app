import {
  ADD_TODO,
  GET_TODOS,
  LOGIN,
  DELETE_TODO,
  DONE_TODO,
  LOGOUT,
  IS_LOGGED
} from "../constants/ActionConstants";
import { initialState } from "../UserContext";

export const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case GET_TODOS:
      return {
        ...state,
        todos: action.payload,
      };

    case LOGIN:
      return {
        ...state,
        isLoggedIn: true,
      };

    case ADD_TODO:
      return {
        ...state,
        todos: action.payload,
      };

    case DELETE_TODO:
      return {
        ...state,
        todos: action.payload,
      };

    case DONE_TODO:
      return {
        ...state,
        todos: action.payload,
      };

    case LOGOUT:
      return {
        ...state,
        firstName: '',
        isLoggedIn: false,
        todos: [],
      };
    case IS_LOGGED:
      return {
        ...state,
        firstName: action.payload,
        isLoggedIn: true,
      };
    default:
      return state;
  }
};
