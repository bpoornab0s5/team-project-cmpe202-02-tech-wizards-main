import React, { useEffect, useState } from "react";
import config from "../services/config";
import { useNavigate } from "react-router-dom";

// Function to retrieve JWT token from cookies
const getTokenFromCookies = () => {
  const cookies = document.cookie.split(";").reduce((acc, cookie) => {
    const [key, value] = cookie.trim().split("=");
    acc[key] = value;
    return acc;
  }, {});
  return cookies["jwt"];
};

const DuplicateRestaurants = () => {
  const navigate = useNavigate();
  const [duplicates, setDuplicates] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDuplicates();
  }, []);

  const fetchDuplicates = async () => {
    const token = getTokenFromCookies();
    if (!token) {
      setError("Authentication token not found.");
      return;
    }

    try {
      const response = await fetch(`${config.BACKEND_URL}/api/admin/duplicates`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        credentials: "include",
      });

      if (!response.ok) {
        const responseText = await response.text();
        console.error(`Error: ${response.status}`, responseText);
        throw new Error(`Request failed with status ${response.status}`);
      }

      const data = await response.json();
      setDuplicates(data);
    } catch (err) {
      console.error("Fetch failed:", err.message);
      setError("Failed to fetch duplicate restaurants.");
    }
  };

  const handleDelete = async (id) => {
    const token = getTokenFromCookies();
    if (!token) {
      alert("Authentication token not found.");
      return;
    }

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
        fetchDuplicates();
      } else {
        const errorData = await response.json();
        alert(errorData.error || "Failed to delete the record.");
      }
    } catch (err) {
      alert("Failed to delete the record.");
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
        <h1 className="text-xl font-semibold">Duplicate Restaurants</h1>
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
              {duplicates.length > 0 ? (
                duplicates.map((restaurant) => (
                  <tr key={restaurant.id} className="hover:bg-gray-100">
                    <td className="py-3 px-6 border-b">{restaurant.id}</td>
                    <td className="py-3 px-6 border-b">{restaurant.name}</td>
                    <td className="py-3 px-6 border-b">{restaurant.address}</td>
                    <td className="py-3 px-6 border-b">{restaurant.city}</td>
                    <td className="py-3 px-6 border-b">{restaurant.state}</td>
                    <td className="py-3 px-6 border-b">{restaurant.contactInfo}</td>
                    <td className="py-3 px-6 border-b">
                      <button
                        onClick={() => handleDelete(restaurant.id)}
                        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" className="text-center py-6 text-gray-500">
                    No duplicate restaurants found.
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

export default DuplicateRestaurants;
