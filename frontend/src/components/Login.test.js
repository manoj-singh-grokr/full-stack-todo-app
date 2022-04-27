import { act, fireEvent, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

import renderComponentWithContext from "../testUtils";
import Login from "./Login";
import axios from "axios";

jest.mock("axios");

const contextValue = {
  state: { firstName: "", isLoggedIn: false, todos: [] },
  dispatch: jest.fn(),
};

describe("login", () => {
  test("login form should be displayed", () => {
    renderComponentWithContext(contextValue, <Login />);
    expect(screen.getByLabelText("Email")).toBeInTheDocument();
    expect(screen.getByLabelText("Password")).toBeInTheDocument();
    expect(screen.getByText("Submit")).toBeInTheDocument();
  });

  describe("login function", () => {
    const username = "test@example.com";
    const password = "whatever123";

    describe("with success", () => {
      const data = { firstName: "Test" };

      it("should return response data", async () => {
        axios.post.mockImplementationOnce(() => Promise.resolve(data));
        const response = await axios.post("http://localhost:8080/api/login", {
          username,
          password,
        });
        expect(response).toStrictEqual(data);
      });
    });

    describe("with failure", () => {
      it("should return error", async () => {
        const errorMessage = "Invalid Credentials";
        axios.post.mockImplementationOnce(() =>
          Promise.reject(new Error(errorMessage))
        );
        renderComponentWithContext(contextValue, <Login />);

        const response = axios.post("http://localhost:8080/api/login", {
          username,
          password,
        });
        await expect(response).rejects.toThrow(errorMessage);
        fireEvent.change(screen.getByLabelText("Email"), {
          target: { value: username },
        });
        fireEvent.change(screen.getByLabelText("Password"), {
          target: { value: password },
        });
        await act(async () => {
          userEvent.click(screen.getByRole("loginButton"));
        });
        expect(screen.getByText("Invalid Credentials")).toBeInTheDocument();
      });
    });
  });
});
