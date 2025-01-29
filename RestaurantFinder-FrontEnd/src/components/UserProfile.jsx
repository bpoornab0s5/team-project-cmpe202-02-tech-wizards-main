import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import config from "../services/config";
import {jwtDecode} from 'jwt-decode';


// Function to retrieve JWT token from cookies
const getTokenFromCookies = () => {
  const cookies = document.cookie.split(";").reduce((acc, cookie) => {
    const [key, value] = cookie.trim().split("=");
    acc[key] = value;
    return acc;
  }, {});
  return cookies["jwt"];
};

const UserProfile = () => {
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // Decode the JWT token to get the user ID
  const getUserIdFromToken = () => {
    const token = getTokenFromCookies();
    if (!token) return null;

    try {
      const decoded = jwtDecode(token);
      return decoded.id;
    } catch (err) {
      console.error("Invalid JWT token:", err.message);
      return null;
    }
  };

  // Fetch user profile on component mount
  useEffect(() => {
    const userId = getUserIdFromToken();
    if (!userId) {
      setError("Invalid or missing authentication token.");
      return;
    }
    fetchUserProfile(userId);
  }, []);

  // Function to fetch user profile
  const fetchUserProfile = async (userId) => {
    try {
      const token = getTokenFromCookies();
      if (!token) {
        setError("Authentication token not found.");
        return;
      }

      const response = await fetch(`${config.BACKEND_URL}/api/users/${userId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setUser(data);
      } else {
        const errorData = await response.json();
        setError(errorData.error || "Failed to fetch user profile.");
      }
    } catch (err) {
      console.error("Fetch failed:", err.message);
      setError("Failed to fetch user profile.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-gray-100">
      <div className="max-w-md w-full bg-white rounded-xl shadow-lg overflow-hidden">
        <div className="bg-blue-500 text-white text-center py-6">
          <h1 className="text-4xl font-bold">User Profile</h1>
        </div>
        <div className="p-6">
          {error && (
            <p className="text-red-500 text-center font-semibold mb-4">
              {error}
            </p>
          )}
          {user ? (
            <div className="space-y-6">
              <div className="flex items-center gap-4">
                <div className="w-16 h-16 bg-blue-100 text-blue-500 flex items-center justify-center rounded-full text-3xl font-bold">
                  {user.username[0].toUpperCase()}
                </div>
                <div>
                  <h2 className="text-xl font-bold text-gray-800">
                    {user.username}
                  </h2>
                  <p className="text-gray-600">{user.email}</p>
                </div>
              </div>
              <hr className="my-4 border-gray-300" />
              <div className="grid grid-cols-2 gap-4">
                <div className="text-gray-700">
                  <span className="font-semibold">ID:</span> {user.id}
                </div>
                <div className="text-gray-700">
                  <span className="font-semibold">Role:</span> {user.role}
                </div>
              </div>
              <div className="mt-6 text-center">
                <button
                  onClick={() => navigate(-1)}
                  className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-opacity-50 transition duration-300"
                >
                  Back to Dashboard
                </button>
              </div>
            </div>
          ) : (
            !error && (
              <p className="text-gray-500 text-center">Loading user profile...</p>
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
