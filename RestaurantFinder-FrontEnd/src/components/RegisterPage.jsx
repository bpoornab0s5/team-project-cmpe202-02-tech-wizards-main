import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import config from "../services/config";

const RegisterPage = () => {
  const { role } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ username: "", email: "", password: "", code: "" });
  const [error, setError] = useState(""); // State to store validation errors

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validation for mandatory fields
    if (!formData.username || !formData.email || !formData.password) {
      setError("All fields (Username, Email, and Password) are mandatory.");
      return;
    }
    setError(""); // Clear any existing errors

    const urlMap = {
      user: "/api/users/register",
      "business-owner": "/api/business-owner/register",
      admin: `/api/users/registerAdmin?code=${formData.code}`,
    };

    const body = {
      username: formData.username,
      email: formData.email,
      password: formData.password,
    };

    try {
      const response = await fetch(`${config.BACKEND_URL}${urlMap[role]}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      const data = await response.json();
      if (response.ok) {
        alert("Registration successful");
        navigate(`/login/${role}`);
      } else {
        setError(data.error || "Registration failed");
      }
    } catch (error) {
      console.error("Registration error:", error);
      setError("User name or email already exists");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-100 via-white to-blue-100 flex items-center justify-center">
      <div className="max-w-md w-full bg-white rounded-lg shadow-lg p-8">
        <h1 className="text-4xl font-bold text-center text-green-600 mb-6">
          Register as {role.replace("-", " ")}
        </h1>
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-gray-600 font-medium mb-2">Username</label>
            <input
              type="text"
              placeholder="Enter your username"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
              onChange={(e) => setFormData({ ...formData, username: e.target.value })}
              required
            />
          </div>
          <div>
            <label className="block text-gray-600 font-medium mb-2">Email</label>
            <input
              type="email"
              placeholder="Enter your email"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              required
            />
          </div>
          <div>
            <label className="block text-gray-600 font-medium mb-2">Password</label>
            <input
              type="password"
              placeholder="Enter your password"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
              onChange={(e) => setFormData({ ...formData, password: e.target.value })}
              required
            />
          </div>
          {role === "admin" && (
            <div>
              <label className="block text-gray-600 font-medium mb-2">Admin Code</label>
              <input
                type="text"
                placeholder="Enter the admin code"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                onChange={(e) => setFormData({ ...formData, code: e.target.value })}
              />
            </div>
          )}
          <button
            type="submit"
            className="w-full bg-green-500 text-white py-3 rounded-lg shadow-lg hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400"
          >
            Register
          </button>
        </form>
        <div className="mt-6 text-center text-sm text-gray-500">
          Already have an account?{" "}
          <span
            className="text-green-500 cursor-pointer hover:underline"
            onClick={() => navigate(`/login/${role}`)}
          >
            Login
          </span>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
