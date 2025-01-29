import React, { useState, useEffect } from "react";
import { addRestaurant } from "../services/api";
import { getOwnerIdFromToken } from "../services/api";
import { useNavigate } from "react-router-dom";

const AddRestaurantForm = () => {
  const [formData, setFormData] = useState({
    name: "",
    address: "",
    city: "",
    state: "",
    zipCode: "",
    country: "USA",
    contactInfo: "",
    hours: "",
    cuisine: "",
    foodType: "",
    priceRange: "",
    description: "",
    businessOwnerId: 0,
  });

  const [responseMessage, setResponseMessage] = useState(null);
  const [errorMessage, setErrorMessage] = useState(null);

  const navigate = useNavigate();

  const cuisines = ["Greek", "Mexican", "Italian", "Chinese", "Thai", "Indian"];
  const foodTypes = ["Vegetarian", "Vegan", "Gluten-Free", "Non-Vegetarian"];
  const priceRanges = ["$", "$$", "$$$", "$$$$"];

  useEffect(() => {
    try {
      const ownerId = getOwnerIdFromToken();
      setFormData((prevData) => ({ ...prevData, businessOwnerId: ownerId }));
    } catch (error) {
      setErrorMessage("Failed to retrieve Business Owner ID from token.");
    }
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const result = await addRestaurant(formData);
      setResponseMessage(`Restaurant "${result.name}" added successfully!`);
      setErrorMessage(null);
      setFormData({
        name: "",
        address: "",
        city: "",
        state: "",
        zipCode: "",
        country: "USA",
        contactInfo: "",
        hours: "",
        cuisine: "",
        foodType: "",
        priceRange: "",
        description: "",
        businessOwnerId: formData.businessOwnerId,
      });
    } catch (error) {
      setErrorMessage(error.message || "Failed to add restaurant.");
      setResponseMessage(null);
    }
  };

  const handleLogout = () => {
    document.cookie = "jwt=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT; SameSite=Strict";
    alert("You have been logged out.");
    navigate("/");
  };

  const handleBack = () => {
    navigate("/business-owner/dashboard");
  };

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white shadow-md rounded-md">
      <div className="flex justify-between items-center mb-6">
        <button
          onClick={handleBack}
          className="bg-gray-500 text-white py-2 px-4 rounded-lg hover:bg-gray-600 shadow-md"
        >
          Back
        </button>
        <button
          onClick={handleLogout}
          className="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-600 shadow-md"
        >
          Logout
        </button>
      </div>

      <h2 className="text-2xl font-bold text-center mb-6">Add a New Restaurant</h2>
      {responseMessage && <p className="text-green-500 text-center">{responseMessage}</p>}
      {errorMessage && <p className="text-red-500 text-center">{errorMessage}</p>}

      <form className="space-y-4" onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <input
            type="text"
            name="name"
            value={formData.name}
            placeholder="Restaurant Name"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <input
            type="text"
            name="address"
            value={formData.address}
            placeholder="Address"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <input
            type="text"
            name="city"
            value={formData.city}
            placeholder="City"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <input
            type="text"
            name="state"
            value={formData.state}
            placeholder="State"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <input
            type="text"
            name="zipCode"
            value={formData.zipCode}
            placeholder="Zip Code"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <input
            type="text"
            name="contactInfo"
            value={formData.contactInfo}
            placeholder="Contact Info"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <input
            type="text"
            name="hours"
            value={formData.hours}
            placeholder="Business Hours (e.g., 9 AM - 9 PM)"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          />

          {/* Cuisine Dropdown */}
          <select
            name="cuisine"
            value={formData.cuisine}
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          >
            <option value="">Select Cuisine</option>
            {cuisines.map((cuisine) => (
              <option key={cuisine} value={cuisine}>
                {cuisine}
              </option>
            ))}
          </select>

          {/* Food Type Dropdown */}
          <select
            name="foodType"
            value={formData.foodType}
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          >
            <option value="">Select Food Type</option>
            {foodTypes.map((foodType) => (
              <option key={foodType} value={foodType}>
                {foodType}
              </option>
            ))}
          </select>

          {/* Price Range Dropdown */}
          <select
            name="priceRange"
            value={formData.priceRange}
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
          >
            <option value="">Select Price Range</option>
            {priceRanges.map((priceRange) => (
              <option key={priceRange} value={priceRange}>
                {priceRange}
              </option>
            ))}
          </select>
        </div>

        <textarea
          name="description"
          value={formData.description}
          placeholder="Description"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
        ></textarea>
        <div className="text-sm text-gray-500">
          <strong>Business Owner ID:</strong> {formData.businessOwnerId}
        </div>
        <button
          type="submit"
          className="w-full py-2 bg-blue-500 text-white font-bold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
        >
          Add Restaurant
        </button>
      </form>
    </div>
  );
};

export default AddRestaurantForm;
