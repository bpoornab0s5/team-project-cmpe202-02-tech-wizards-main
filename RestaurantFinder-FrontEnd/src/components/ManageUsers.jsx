import React, { useEffect, useState } from "react";
import config from "../services/config";
import { useNavigate } from "react-router-dom";

const getTokenFromCookies = () => {
  const cookies = document.cookie.split(";").reduce((acc, cookie) => {
    const [key, value] = cookie.trim().split("=");
    acc[key] = value;
    return acc;
  }, {});
  return cookies["jwt"];
};

const ManageUsers = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);

  const token = getTokenFromCookies();

  useEffect(() => {
    fetchUsers();
  });

  const fetchUsers = async () => {
    try {
      const response = await fetch(`${config.BACKEND_URL}/api/users`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setUsers(data);
      } else {
        const errorData = await response.json();
        setError(errorData.error || "Failed to fetch users.");
      }
    } catch (err) {
      setError("Failed to fetch users.");
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Are you sure you want to delete this user?");
    if (!confirmed) return;

    try {
      const response = await fetch(`${config.BACKEND_URL}/api/users/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        alert("User deleted successfully.");
        fetchUsers();
      } else {
        const errorData = await response.json();
        alert(errorData.error || "Failed to delete the user.");
      }
    } catch (err) {
      alert("Failed to delete the user.");
    }
  };

  const handleLogout = () => {
    document.cookie = "jwt=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
    navigate("/");
  };

  const goToAdminDashboard = () => {
    navigate("/admin/dashboard");
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <div className="bg-blue-600 text-white px-6 py-4 flex justify-between items-center">
        <h1 className="text-xl font-semibold">Manage Users</h1>
        <div>
          <button
            onClick={goToAdminDashboard}
            className="bg-white text-blue-600 px-4 py-2 rounded-lg hover:bg-gray-100 mr-2"
          >
            Back to Dashboard
          </button>
          <button
            onClick={handleLogout}
            className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
          >
            Logout
          </button>
        </div>
      </div>

      {/* Content */}
      <div className="p-6">
        {error && <p className="text-red-500 text-center">{error}</p>}
        <div className="overflow-x-auto bg-white rounded-lg shadow-md">
          <table className="min-w-full table-auto">
            <thead className="bg-gray-200 text-gray-700">
              <tr>
                <th className="py-3 px-6 text-left font-semibold">User ID</th>
                <th className="py-3 px-6 text-left font-semibold">Username</th>
                <th className="py-3 px-6 text-left font-semibold">Email</th>
                <th className="py-3 px-6 text-left font-semibold">Role</th>
                <th className="py-3 px-6 text-left font-semibold">Actions</th>
              </tr>
            </thead>
            <tbody className="text-gray-600">
              {users.length > 0 ? (
                users.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-100">
                    <td className="py-3 px-6 border-b">{user.id}</td>
                    <td className="py-3 px-6 border-b">{user.username}</td>
                    <td className="py-3 px-6 border-b">{user.email}</td>
                    <td className="py-3 px-6 border-b">{user.role}</td>
                    <td className="py-3 px-6 border-b flex space-x-2">
                      <button
                        onClick={() => handleDelete(user.id)}
                        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center py-6 text-gray-500">
                    No users found
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ManageUsers;
