import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { setToken } from "../services/api";
import config from "../services/config";

const LoginPage = () => {
  const { role } = useParams();
  const navigate = useNavigate();
  const [credentials, setCredentials] = useState({ username: "", password: "" });
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validation for mandatory fields
    if (!credentials.username || !credentials.password) {
      setError("Username and Password are required.");
      return;
    }

    const urlMap = {
      user: "/api/users/login",
      "business-owner": "/api/business-owner/login",
      admin: "/api/users/loginAdmin",
    };

    try {
      const response = await fetch(`${config.BACKEND_URL}${urlMap[role]}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        const errorText = await response.text();
        setError(errorText || "Login failed");
        return;
      }

      const token = await response.text();
      setToken(token); // Store token in cookies

      const decoded = jwtDecode(token);
      const userRole = decoded.role;

      if (userRole === "ROLE_RegularUser") navigate("/user/dashboard");
      else if (userRole === "ROLE_BusinessOwner") navigate("/business-owner/dashboard");
      else if (userRole === "ROLE_Admin") navigate("/admin/dashboard");
      else setError("Unauthorized role!");
    } catch (error) {
      console.error("Login error:", error);
      setError("An unexpected error occurred during login.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-r from-blue-100 via-white to-green-100 flex flex-col items-center justify-center">
      <div className="w-full max-w-md bg-white rounded-lg shadow-lg p-8">
        <h1 className="text-4xl font-bold text-center text-black-600 mb-6">
          {role.replace("-", " ").toLocaleUpperCase()} LOGIN
        </h1>
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <input
              type="text"
              placeholder="Username"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
              onChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
            />
          </div>
          <div>
            <input
              type="password"
              placeholder="Password"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
              onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
            />
          </div>
          <button
            type="submit"
            className="w-full py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition duration-300"
          >
            Login
          </button>
        </form>
        <div className="mt-6 text-center text-sm text-gray-500">
          Don't have an account?{" "}
          <span
            className="text-blue-500 cursor-pointer hover:underline"
            onClick={() => navigate(`/register/${role}`)}
          >
            Create one
          </span>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
