
import React, { useState } from "react";
import { fetchWithAuth } from "../services/api"; // Replace with your actual fetch service
import { useNavigate } from "react-router-dom";

const UserDashboard = () => {
  const [filters, setFilters] = useState({
    city: "",
    zipCode: "",
    cuisine: [],
    foodType: "",
    priceRange: "",
    minRating: "",
  });
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(false); // Loading state
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();
  const defaultRestaurantImage = "/restaurant-stock.png";

  // Predefined filters
  const cuisines = ["Greek", "Mexican", "Italian", "Chinese", "Thai", "Indian"];
  const foodTypes = ["Vegetarian", "Vegan", "Gluten-Free", "Non-Vegetarian"];
  const priceRanges = ["$", "$$", "$$$", "$$$$"];
  const minRatings = [1,2,3,4,5];

  const handleSearch = async () => {
    if (!filters.city || !filters.zipCode) {
      setErrorMessage("City and Zip Code are mandatory fields!");
      return;
    }
  
    setErrorMessage("");
    setLoading(true);
  
    const internalRestaurants = [];
    const externalRestaurants = new Map(); // Use a Map to ensure no duplicates in external restaurants
  
    try {
      // Determine whether multiple calls are needed
      const cuisineFilters = filters.cuisine.length > 0 ? filters.cuisine : [null];
      const foodTypeFilters = filters.foodType.length > 0 ? filters.foodType : [null];
  
      for (const cuisine of cuisineFilters) {
        for (const foodType of foodTypeFilters) {
          const queryParams = Object.entries({ ...filters, cuisine, foodType })
            .filter(([key, value]) => value)
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join("&");
  
          const response = await fetchWithAuth(`/api/restaurants/search?${queryParams}`);
          if (response) {
            // Add internal restaurants
            internalRestaurants.push(
              ...response.internalRestaurants.map((restaurant) => ({
                ...restaurant,
                isExternal: false,
              }))
            );
  
            response.externalRestaurants.forEach((restaurant) => {
              if (!externalRestaurants.has(restaurant.name)) { // Check by name
                externalRestaurants.set(restaurant.name, {
                  ...restaurant,
                  isExternal: true,
                });
              }
            });
          }
        }
      }
  
      // Combine internal and unique external restaurants
      setRestaurants([
        ...internalRestaurants,
        ...Array.from(externalRestaurants.values()), // Convert Map to array
      ]);
    } catch (error) {
      console.error("Error fetching restaurants:", error);
      setRestaurants([]);
    } finally {
      setLoading(false); // Stop loading
    }
  };
  
  
  
  
  

  const handleLogout = () => {
    
    document.cookie = 'jwt=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;'; // Remove JWT cookie
    navigate('/'); // Redirect to login or home page
  };

  // Handle Restaurant Click
  const handleRestaurantClick = (restaurant) => {
    if (restaurant.isExternal) {
      navigate("/external-restaurant-details", { state: restaurant });
    } else {
      navigate(`/restaurant/${restaurant.id}`);
    }
  };

  const goToUserProfile = () => {
    navigate("/user/profile"); // Replace with your actual route to UserProfile
  };


  const renderStars = (rating) => {
    const maxStars = 5;
    return (
      <div className="flex">
        {Array.from({ length: maxStars }, (_, index) => (
          <span
            key={index}
            className={`material-icons ${index < rating ? "text-yellow-500" : "text-gray-300"}`}
          >
            star
          </span>
        ))}
      </div>
    );
  };


  return (
    <div className="container mx-auto p-6 bg-gray-100 min-h-screen">
     
  <div className="flex justify-between items-center mb-6">
    {/* Logout Button - Left */}
    <button
      onClick={handleLogout}
      className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
    >
      Logout
    </button>

    {/* Go to User Profile Button - Right */}
    <button
      onClick={goToUserProfile}
      className="px-12 py-3 bg-blue-500 text-white rounded hover:bg-blue-600"
    >
      Go to User Profile
    </button>
  </div>

      <h1 className="text-3xl font-bold text-center mb-6">Find Your Favorite Restaurants</h1>

      {/* Search Form */}
      <div className="bg-white shadow-lg p-6 rounded-md mb-6">
        <div className="flex flex-wrap gap-4 mb-4">
          <input
            type="text"
            placeholder="City"
            className="border p-3 rounded-md w-full md:w-1/2"
            value={filters.city}
            onChange={(e) => setFilters({ ...filters, city: e.target.value })}
          />
          <input
            type="text"
            placeholder="Zip Code"
            className="border p-3 rounded-md w-full md:w-1/2"
            value={filters.zipCode}
            onChange={(e) => setFilters({ ...filters, zipCode: e.target.value })}
          />
        </div>

        {/* Filters */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {/* Cuisine Filter */}
         {/* Cuisine Filter */}
<div>
  <h3 className="font-bold text-lg mb-2">Cuisine</h3>
  {cuisines.map((cuisine) => (
    <label key={cuisine} className="block mb-2">
      <input
        type="checkbox"
        value={cuisine}
        className="mr-2"
        checked={filters.cuisine.includes(cuisine)} // Check if cuisine is selected
        onChange={(e) => {
          const selectedCuisines = filters.cuisine;
          if (e.target.checked) {
            // Add cuisine to the array
            setFilters({ ...filters, cuisine: [...selectedCuisines, cuisine] });
          } else {
            // Remove cuisine from the array
            setFilters({
              ...filters,
              cuisine: selectedCuisines.filter((item) => item !== cuisine),
            });
          }
        }}
      />
      {cuisine}
    </label>
  ))}
</div>


          {/* Food Type Filter */}
          <div>
  <h3 className="font-bold text-lg mb-2">Food Type</h3>
  {foodTypes.map((foodType) => (
    <label key={foodType} className="block mb-2">
      <input
        type="checkbox"
        value={foodType}
        className="mr-2"
        checked={filters.foodType.includes(foodType)} // Check if foodType is selected
        onChange={(e) => {
          const selectedFoodTypes = filters.foodType;
          if (e.target.checked) {
            // Add foodType to the array
            setFilters({ ...filters, foodType: [...selectedFoodTypes, foodType] });
          } else {
            // Remove foodType from the array
            setFilters({
              ...filters,
              foodType: selectedFoodTypes.filter((item) => item !== foodType),
            });
          }
        }}
      />
      {foodType}
    </label>
  ))}
</div>


          {/* Price Range Filter */}
          <div>
            <h3 className="font-bold text-lg mb-2">Price Range</h3>
            {priceRanges.map((priceRange) => (
              <label key={priceRange} className="block mb-2">
                <input
                  type="radio"
                  name="priceRange"
                  value={priceRange}
                  className="mr-2"
                  checked={filters.priceRange === priceRange}
                  onChange={() => setFilters({ ...filters, priceRange })}
                />
                {priceRange}
              </label>
            ))}
          </div>

          {/* Minimum Rating Filter */}
          <div>
            <h3 className="font-bold text-lg mb-2">Minimum Rating</h3>
            {minRatings.map((rating) => (
              <label key={rating} className="block mb-2">
                <input
                  type="radio"
                  name="minRating"
                  value={rating}
                  className="mr-2"
                  checked={filters.minRating === String(rating)}
                  onChange={() => setFilters({ ...filters, minRating: String(rating) })}
                />
                {rating}
              </label>
            ))}
          </div>
        </div>

        <button
          className="mt-4 w-full bg-blue-500 text-white font-bold py-3 rounded-md hover:bg-blue-600 transition"
          onClick={handleSearch}
        >
          Search
        </button>
        
    <button
      className="mt-4 w-full bg-gray-500 text-white font-bold py-3 rounded-md hover:bg-gray-600 transition"
      onClick={() => window.location.reload()} // Reload the entire page
    >
      Reload
    </button>
        {errorMessage && <p className="text-red-500 mt-4">{errorMessage}</p>}
      </div>

      {/* Results */}

      <div>
  <h2 className="text-2xl font-bold mb-4">Search Results</h2>
  {loading ? (
    <div className="flex justify-center items-center min-h-[200px]">
      <div className="w-16 h-16 border-4 border-blue-500 border-t-transparent border-solid rounded-full animate-spin"></div>
    </div>
  ) : restaurants.length === 0 ? (
    <p className="text-gray-500">No results found.</p>
  ) : (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {restaurants.map((restaurant) => (
        <div
          key={restaurant.id || restaurant.name}
          className="bg-white border rounded-md shadow hover:shadow-lg transition cursor-pointer"
          onClick={() => handleRestaurantClick(restaurant)}
        >
          <img
            src={
              restaurant.photos && restaurant.photos.length > 0
                ? restaurant.photos[0]
                : defaultRestaurantImage
            }
            alt={restaurant.name}
            className="w-full h-40 object-cover rounded-t-md"
          />
          <div className="p-4">
            <h3 className="font-bold text-lg">{restaurant.name}</h3>
            <p className="text-sm text-gray-600">{restaurant.address}</p>
            <p className="text-sm text-gray-600">
              {restaurant.city}, {restaurant.state}, {restaurant.zipCode}
            </p>
            <div className="mt-4 flex items-center">
              {restaurant.isExternal ? (
                <div className="flex items-center text-red-500">
                  <span className="material-icons mr-2">flag</span> {/* Red Flag */}
                  <p className="text-sm font-medium">Un-Partnered Restaurant</p>
                </div>
              ) : (
                <div className="flex justify-between items-center w-full">
                {/* Left Side: Partnered Restaurant Label */}
                <div className="flex items-center text-green-500">
                  <span className="material-icons mr-2">flag</span>
                  <p className="text-sm font-medium">Partnered Restaurant</p>
                </div>
              
                {/* Right Side: Stars */}
                <div className="flex items-center">{renderStars(restaurant.rating || 0)}</div>
              </div>
              
              
              
                
              )}
            </div>
          </div>
        </div>
      ))}
    </div>
  )}
</div>

         </div>
  );
};

export default UserDashboard;
