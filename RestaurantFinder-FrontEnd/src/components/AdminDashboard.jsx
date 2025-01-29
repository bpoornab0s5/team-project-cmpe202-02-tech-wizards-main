import React from "react";
import { useNavigate } from "react-router-dom"; // React Router for navigation
import { removeToken } from "../services/api";

const AdminDashboard = () => {
  const navigate = useNavigate(); // Initialize React Router's navigate function

  const handleLogout = () => {
    removeToken();
    window.location.href = "/";
  };

  const handleViewDuplicates = () => {
    navigate("/admin/duplicates"); // Navigate to the DuplicateRestaurants route
  };

  const handleProcessReported = () => {
    navigate("/admin/reported"); // Navigate to the ReportedRestaurants route
  };

  const handleUsers = () => {
    navigate("/admin/users"); // Navigate to the Users route
  };

  const goToUserProfile = () => {
    navigate("/user/profile"); // Navigate to User Profile route
  };

  return (
    <div className="min-h-screen bg-gradient-to-r from-blue-50 to-blue-100 flex flex-col items-center justify-center">
      <div className="bg-white shadow-md rounded-lg p-8 w-full max-w-lg">
        <h1 className="text-4xl font-bold text-center text-blue-700 mb-8">Admin Dashboard</h1>
        <div className="space-y-6">
          <button
            onClick={handleLogout}
            className="w-full px-6 py-3 bg-red-500 text-white rounded-lg font-semibold hover:bg-red-600 shadow-md transition"
          >
            Logout
          </button>
          <button
            onClick={handleViewDuplicates}
            className="w-full px-6 py-3 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 shadow-md transition"
          >
            View Duplicate Restaurants
          </button>
          <button
            onClick={handleProcessReported}
            className="w-full px-6 py-3 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 shadow-md transition"
          >
            View Reported Restaurants
          </button>
          <button
            onClick={handleUsers}
            className="w-full px-6 py-3 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 shadow-md transition"
          >
            View All Users
          </button>
          <button
            onClick={goToUserProfile}
            className="w-full px-6 py-3 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 shadow-md transition"
          >
            Go to User Profile
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
