import React from "react";
import { useNavigate } from "react-router-dom";

const RootPage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-r from-blue-100 via-white to-green-100">
      {/* Main Container */}
      <div className="bg-white shadow-lg rounded-lg p-8 max-w-md w-full text-center">
        {/* Title */}
        <h1 className="text-4xl font-extrabold text-gray-800 mb-4">
          Welcome to Restaurant Finder
        </h1>
        <p className="text-lg text-gray-600 mb-6">
          Discover and explore the best restaurants in your area.
        </p>

        <p className="text-lg text-yellow-600 mb-4">Select your role to proceed:</p>
        
        {/* Role Selection */}
        <div className="space-y-4">
          <button
            className="w-full px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition duration-300"
            onClick={() => navigate("/login/user")}
          >
            User
          </button>
          <button
            className="w-full px-6 py-3 bg-green-500 text-white font-semibold rounded-lg shadow-md hover:bg-green-600 transition duration-300"
            onClick={() => navigate("/login/business-owner")}
          >
            Business Owner
          </button>
          <button
            className="w-full px-6 py-3 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 transition duration-300"
            onClick={() => navigate("/login/admin")}
          >
            Admin
          </button>
        </div>
      </div>
    </div>
  );
};

export default RootPage;
