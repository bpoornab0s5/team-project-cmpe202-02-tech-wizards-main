import React from "react";
import { useNavigate } from "react-router-dom";

const RoleSelection = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-3xl font-bold mb-8">Select Your Role</h1>
      <div className="flex flex-col gap-4">
        <button
          className="px-6 py-3 bg-blue-500 text-white rounded"
          onClick={() => navigate("/login/user")}
        >
          User
        </button>
        <button
          className="px-6 py-3 bg-green-500 text-white rounded"
          onClick={() => navigate("/login/business-owner")}
        >
          Business Owner
        </button>
        <button
          className="px-6 py-3 bg-red-500 text-white rounded"
          onClick={() => navigate("/login/admin")}
        >
          Admin
        </button>
      </div>
    </div>
  );
};

export default RoleSelection;
