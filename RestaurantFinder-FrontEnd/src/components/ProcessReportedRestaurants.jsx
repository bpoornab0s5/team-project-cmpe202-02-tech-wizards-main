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

const ProcessReportedRestaurants = () => {
  const navigate = useNavigate();
  const [reported, setReported] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchReportedRestaurants();
  });

  const token = getTokenFromCookies();
  if (!token) {
    setError("Authentication token not found.");
    return;
  }

  const fetchReportedRestaurants = async () => {
    try {
      const response = await fetch(`${config.BACKEND_URL}/api/restaurants/reported-closed`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setReported(data);
      } else {
        const errorData = await response.json();
        setError(errorData.error || "Failed to fetch reported restaurants.");
      }
    } catch (err) {
      setError("Failed to fetch reported restaurants.");
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Are you sure you want to delete this record?");
    if (!confirmed) return;

    try {
      const response = await fetch(`${config.BACKEND_URL}/api/business-owner/restaurants/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        alert("Record deleted successfully.");
        fetchReportedRestaurants();
      } else {
        const errorData = await response.json();
        alert(errorData.error || "Failed to delete the record.");
      }
    } catch (err) {
      alert("Failed to delete the record.");
    }
  };

  const handleMarkOpen = async (id) => {
    try {
      const response = await fetch(`${config.BACKEND_URL}/api/restaurants/${id}/mark-open`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        alert("Restaurant marked as open successfully.");
        fetchReportedRestaurants();
      } else {
        const errorData = await response.json();
        alert(errorData.error || "Failed to mark the restaurant as open.");
      }
    } catch (err) {
      alert("Failed to mark the restaurant as open.");
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
      {/* Header Section */}
      <div className="bg-blue-600 text-white px-6 py-4 flex justify-between items-center">
        <h1 className="text-xl font-semibold">Reported Restaurants</h1>
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

      {/* Content Section */}
      <div className="p-6">
        {error && <p className="text-red-500 text-center">{error}</p>}

        <div className="overflow-x-auto bg-white rounded-lg shadow-md">
          <table className="min-w-full table-auto">
            <thead className="bg-gray-200 text-gray-700">
              <tr>
                <th className="py-3 px-6 text-left font-semibold">Restaurant ID</th>
                <th className="py-3 px-6 text-left font-semibold">Restaurant Name</th>
                <th className="py-3 px-6 text-left font-semibold">Address</th>
                <th className="py-3 px-6 text-left font-semibold">City</th>
                <th className="py-3 px-6 text-left font-semibold">State</th>
                <th className="py-3 px-6 text-left font-semibold">Contact</th>
                <th className="py-3 px-6 text-left font-semibold">Actions</th>
              </tr>
            </thead>
            <tbody className="text-gray-600">
              {reported.length > 0 ? (
                reported.map((restaurant) => (
                  <tr key={restaurant.id} className="hover:bg-gray-100">
                    <td className="py-3 px-6 border-b">{restaurant.id}</td>
                    <td className="py-3 px-6 border-b">{restaurant.name}</td>
                    <td className="py-3 px-6 border-b">{restaurant.address}</td>
                    <td className="py-3 px-6 border-b">{restaurant.city}</td>
                    <td className="py-3 px-6 border-b">{restaurant.state}</td>
                    <td className="py-3 px-6 border-b">{restaurant.contactInfo}</td>
                    <td className="py-3 px-6 border-b flex space-x-2">
                      <button
                        onClick={() => handleDelete(restaurant.id)}
                        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                      >
                        Delete
                      </button>
                      <button
                        onClick={() => handleMarkOpen(restaurant.id)}
                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                      >
                        Mark Open
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" className="text-center py-6 text-gray-500">
                    No reported restaurants found.
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

export default ProcessReportedRestaurants;
