import React, { useEffect, useState } from "react";
import {
  getRestaurantsByOwner,
  deleteRestaurant,
  uploadRestaurantPhoto,
} from "../services/api";
import PhotoSlider from "./PhotoSlider"; // Import PhotoSlider component
import { Link, useNavigate } from "react-router-dom";
import { getOwnerIdFromToken } from "../services/api";

const BusinessOwnerDashboard = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [errorMessage, setErrorMessage] = useState(null);
  const navigate = useNavigate();

  const ownerId = getOwnerIdFromToken(); // Replace with actual owner ID

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const data = await getRestaurantsByOwner(ownerId);
        setRestaurants(data);
      } catch (error) {
        setErrorMessage(error.message || "Failed to fetch restaurants.");
      }
    };

    fetchRestaurants();
  }, [ownerId]);

  const handleLogout = () => {
    document.cookie =
      "jwt=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT; SameSite=Strict";
    alert("You have been logged out.");
    navigate("/");
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this restaurant?")) {
      try {
        await deleteRestaurant(id);
        setRestaurants((prevRestaurants) =>
          prevRestaurants.filter((restaurant) => restaurant.id !== id)
        );
        alert("Restaurant deleted successfully.");
      } catch (error) {
        alert(`Failed to delete restaurant: ${error}`);
      }
    }
  };

  const handleEdit = (id) => {
    navigate(`/edit-restaurant/${id}`);
  };

  const goToUserProfile = () => {
    navigate("/user/profile"); // Replace with your actual route to UserProfile
  };

  const handlePhotoUpload = async (id, file) => {
    try {
      const response = await uploadRestaurantPhoto(id, file);
      alert(response.message || "Photo uploaded successfully.");
      setRestaurants((prevRestaurants) =>
        prevRestaurants.map((restaurant) =>
          restaurant.id === id
            ? { ...restaurant, photos: [...(restaurant.photos || []), response.url] }
            : restaurant
        )
      );
    } catch (error) {
      alert(`Failed to upload photo: ${error}`);
    }
  };

  const handleFileChange = (id, e) => {
    const file = e.target.files[0];
    if (file) {
      handlePhotoUpload(id, file);
    }
  };

  return (
    <div className="container mx-auto p-8 bg-gray-50 min-h-screen">
      {/* Header Section */}
      <div className="flex justify-between items-center mb-10">
        <h1 className="text-4xl font-bold text-gray-800">
          Welcome, Business Owner {ownerId}
        </h1>
        <div className="flex gap-4">
          <Link
            to="/business-owner/restaurants"
            className="bg-blue-600 text-white py-2 px-6 rounded-lg hover:bg-blue-700 shadow-lg transition-all"
          >
            Add New Restaurant
          </Link>
          <button
      onClick={goToUserProfile}
      className="px-12 py-3 bg-blue-500 text-white rounded hover:bg-blue-600"
    >
      Go to User Profile
    </button>
          <button
            onClick={handleLogout}
            className="bg-red-600 text-white py-2 px-6 rounded-lg hover:bg-red-700 shadow-lg transition-all"
          >
            Logout
          </button>
        </div>
      </div>

      {/* Error Message */}
      {errorMessage && (
        <p className="text-red-500 text-center mb-4">{errorMessage}</p>
      )}

      {/* Restaurants Grid */}
      {restaurants.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {restaurants.map((restaurant) => (
            <div
              key={restaurant.id}
              className="border rounded-lg shadow-md bg-white p-6 hover:shadow-lg transition-shadow"
            >
              <h2 className="text-2xl font-bold text-gray-800 mb-3">
                {restaurant.name}
              </h2>
              <p className="text-gray-700 mb-2">
                <strong>Address:</strong> {restaurant.address}, {restaurant.city}
              </p>
              <p className="text-gray-700 mb-2">
                <strong>Hours:</strong> {restaurant.hours || "Not specified"}
              </p>
              <p className="text-gray-700 mb-4">
                <strong>Cuisine:</strong> {restaurant.cuisine || "Not specified"}
              </p>

              {/* Photo Slider */}
              <div className="mb-4">
                <h3 className="font-semibold text-gray-700">Photos:</h3>
                {restaurant.photos && restaurant.photos.length > 0 ? (
                  <PhotoSlider photos={restaurant.photos} />
                ) : (
                  <p className="text-gray-500">No photos available.</p>
                )}
              </div>

              {/* Upload Photo */}
              <label className="block mb-4">
                <span className="text-sm font-medium text-gray-700">
                  Upload a Photo:
                </span>
                <input
                  type="file"
                  accept="image/*"
                  onChange={(e) => handleFileChange(restaurant.id, e)}
                  className="block w-full mt-2 text-sm text-gray-700 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                />
              </label>

              {/* Action Buttons */}
              <div className="flex gap-4">
                <button
                  onClick={() => handleEdit(restaurant.id)}
                  className="bg-yellow-500 text-white py-2 px-4 rounded-lg hover:bg-yellow-600 shadow-md transition-all"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(restaurant.id)}
                  className="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-600 shadow-md transition-all"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <p className="text-center text-gray-600 text-lg mt-10">
          No restaurants found. Add your first restaurant to get started!
        </p>
      )}
    </div>
  );
};

export default BusinessOwnerDashboard;
