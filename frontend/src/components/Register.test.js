import { act, fireEvent, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

import renderComponentWithContext from "../testUtils";
import Register from "./register";
import axios from "axios";

jest.mock("axios");

const contextValue = {
  state: { firstName: "", isLoggedIn: false, todos: [] },
  dispatch: jest.fn(),
};

describe("register", () => {
  const firstName = "Test2";
  const lastName = "Uh";
  const email = "test2@example.com";
  const password = "testpassword";
  const confirmPassword = "testpassword";

  test("registration form should be displayed", () => {
    renderComponentWithContext(contextValue, <Register />);
    expect(screen.getByLabelText("First Name")).toBeInTheDocument();
    expect(screen.getByLabelText("Last Name")).toBeInTheDocument();
    expect(screen.getByLabelText("Email")).toBeInTheDocument();
    expect(screen.getByLabelText("Password")).toBeInTheDocument();
    expect(screen.getByLabelText("Confirm Password")).toBeInTheDocument();

    expect(screen.getByRole("registerButton")).toBeInTheDocument();
  });

  describe("register function", () => {
    describe("with success", () => {
      const data = { token: "randomto$&keb23qwrdnvkksadfkds", status: 200 };

      it("should return response data", async () => {
        axios.post.mockImplementationOnce(() => Promise.resolve(data));
        const response = await axios.post(
          "http://localhost:8080/api/register",
          { firstName, lastName, email, password, confirmPassword }
        );
        expect(response).toStrictEqual(data);
      });
    });

    describe("with failure", () => {
      it("should return error", async () => {
        const errorMessage = "email already exists";
        axios.post.mockImplementationOnce(() =>
          Promise.reject(new Error(errorMessage))
        );
        renderComponentWithContext(contextValue, <Register />);

        const response = axios.post("http://localhost:8080/api/register", {
          firstName,
          lastName,
          email,
          password,
          confirmPassword,
        });
        await expect(response).rejects.toThrow(errorMessage);
        fireEvent.change(screen.getByLabelText("First Name"), {
          target: { value: firstName },
        });
        fireEvent.change(screen.getByLabelText("Last Name"), {
          target: { value: lastName },
        });
        fireEvent.change(screen.getByLabelText("Email"), {
          target: { value: email },
        });
        fireEvent.change(screen.getByLabelText("Password"), {
          target: { value: password },
        });
        fireEvent.change(screen.getByLabelText("Confirm Password"), {
          target: { value: confirmPassword },
        });
        await act(async () => {
          userEvent.click(screen.getByRole("registerButton"));
        });
        expect(screen.getByText("Email already exists")).toBeInTheDocument();
      });
    });
  });
});
