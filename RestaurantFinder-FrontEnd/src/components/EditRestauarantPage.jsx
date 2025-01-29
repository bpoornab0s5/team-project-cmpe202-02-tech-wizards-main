import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { updateRestaurant, getRestaurantsByOwner } from "../services/api";
import { getOwnerIdFromToken } from "../services/api";

const EditRestaurantPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [restaurantData, setRestaurantData] = useState(null);
  const [formData, setFormData] = useState({});
  const [errorMessage, setErrorMessage] = useState(null);

  const cuisines = ["Greek", "Mexican", "Italian", "Chinese", "Thai", "Indian"];
  const foodTypes = ["Vegetarian", "Vegan", "Gluten-Free", "Non-Vegetarian"];
  const priceRanges = ["$", "$$", "$$$", "$$$$"];

  useEffect(() => {
    const fetchRestaurant = async () => {
      try {
        const ownerId = getOwnerIdFromToken();
        const restaurants = await getRestaurantsByOwner(ownerId);
        const restaurant = restaurants.find((r) => r.id === parseInt(id));
        if (restaurant) {
          setRestaurantData(restaurant);
          setFormData({
            name: restaurant.name || "",
            address: restaurant.address || "",
            city: restaurant.city || "",
            state: restaurant.state || "",
            zipCode: restaurant.zipCode || "",
            contactInfo: restaurant.contactInfo || "",
            hours: restaurant.hours || "",
            cuisine: restaurant.cuisine || "",
            foodType: restaurant.foodType || "",
            priceRange: restaurant.priceRange || "",
            description: restaurant.description || "",
          });
        }
      } catch (error) {
        setErrorMessage("Failed to fetch restaurant data.");
      }
    };

    fetchRestaurant();
  }, [id]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleUpdate = async () => {
    // Validate mandatory fields
    const requiredFields = [
      "name",
      "address",
      "city",
      "state",
      "zipCode",
      "contactInfo",
      "cuisine",
      "foodType",
      "priceRange",
    ];

    for (const field of requiredFields) {
      if (!formData[field]) {
        setErrorMessage(`${field.charAt(0).toUpperCase() + field.slice(1)} is required.`);
        return;
      }
    }

    try {
      await updateRestaurant(id, formData);
      alert("Restaurant updated successfully.");
      navigate("/business-owner/home");
    } catch (error) {
      setErrorMessage(`Failed to update restaurant: ${error}`);
    }
  };

  const handleBack = () => {
    navigate("/business-owner/home");
  };

  const handleLogout = () => {
    document.cookie = "jwt=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT; SameSite=Strict";
    alert("You have been logged out.");
    navigate("/");
  };

  if (!restaurantData) {
    return <p className="text-center text-gray-700">Loading restaurant data...</p>;
  }

  return (
    <div className="container mx-auto p-6 bg-gray-100 min-h-screen">
      <div className="flex justify-between items-center mb-6">
        <button
          onClick={handleBack}
          className="bg-gray-500 text-white py-2 px-4 rounded hover:bg-gray-600"
        >
          Back
        </button>
        <button
          onClick={handleLogout}
          className="bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600"
        >
          Logout
        </button>
      </div>

      <h1 className="text-3xl font-bold text-center mb-6">Edit Restaurant</h1>
      {errorMessage && <p className="text-red-500 text-center mb-4">{errorMessage}</p>}
      <form
        className="bg-white p-6 shadow-md rounded-md max-w-4xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-4"
      >
        {/* Restaurant Name */}
        <label className="block col-span-1">
          <span className="text-gray-700">Restaurant Name</span>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          />
        </label>

        {/* Address */}
        <label className="block col-span-1">
          <span className="text-gray-700">Address</span>
          <input
            type="text"
            name="address"
            value={formData.address}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          />
        </label>

        {/* City */}
        <label className="block col-span-1">
          <span className="text-gray-700">City</span>
          <input
            type="text"
            name="city"
            value={formData.city}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          />
        </label>

        {/* State */}
        <label className="block col-span-1">
          <span className="text-gray-700">State</span>
          <input
            type="text"
            name="state"
            value={formData.state}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          />
        </label>

        {/* Zip Code */}
        <label className="block col-span-1">
          <span className="text-gray-700">Zip Code</span>
          <input
            type="text"
            name="zipCode"
            value={formData.zipCode}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          />
        </label>

        {/* Contact Info */}
        <label className="block col-span-1">
          <span className="text-gray-700">Contact Info</span>
          <input
            type="text"
            name="contactInfo"
            value={formData.contactInfo}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          />
        </label>

        {/* Business Hours */}
        <label className="block col-span-1">
          <span className="text-gray-700">Business Hours</span>
          <input
            type="text"
            name="hours"
            value={formData.hours}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
          />
        </label>

        {/* Cuisine */}
        <label className="block col-span-1">
          <span className="text-gray-700">Cuisine</span>
          <select
            name="cuisine"
            value={formData.cuisine}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          >
            <option value="">Select Cuisine</option>
            {cuisines.map((cuisine) => (
              <option key={cuisine} value={cuisine}>
                {cuisine}
              </option>
            ))}
          </select>
        </label>

        {/* Food Type */}
        <label className="block col-span-1">
          <span className="text-gray-700">Food Type</span>
          <select
            name="foodType"
            value={formData.foodType}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          >
            <option value="">Select Food Type</option>
            {foodTypes.map((foodType) => (
              <option key={foodType} value={foodType}>
                {foodType}
              </option>
            ))}
          </select>
        </label>

        {/* Price Range */}
        <label className="block col-span-1">
          <span className="text-gray-700">Price Range</span>
          <select
            name="priceRange"
            value={formData.priceRange}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
            required
          >
            <option value="">Select Price Range</option>
            {priceRanges.map((priceRange) => (
              <option key={priceRange} value={priceRange}>
                {priceRange}
              </option>
            ))}
          </select>
        </label>

        {/* Description */}
        <label className="block col-span-2">
          <span className="text-gray-700">Description</span>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="w-full p-2 border rounded mt-1"
          ></textarea>
        </label>

        {/* Buttons */}
        <div className="col-span-2 flex justify-end gap-4">
          <button
            type="button"
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
            onClick={() => navigate("/business-owner/home")}
          >
            Cancel
          </button>
          <button
            type="button"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            onClick={handleUpdate}
          >
            Update
          </button>
        </div>
      </form>
    </div>
  );
};

export default EditRestaurantPage;
