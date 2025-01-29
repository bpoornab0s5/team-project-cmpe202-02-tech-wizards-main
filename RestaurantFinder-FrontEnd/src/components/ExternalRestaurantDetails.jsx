import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

// Fix for Leaflet default marker icon
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
});

const ExternalRestaurantDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const restaurant = location.state;

  if (!restaurant) {
    return (
      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-3xl font-bold text-center mb-6">Restaurant Details</h1>
        <p className="text-center text-gray-600">No restaurant data available.</p>
        <button
          onClick={() => navigate(-1)}
          className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Back
        </button>
      </div>
    );
  }

    // Function to handle logout
    const handleLogout = () => {
    
      document.cookie = 'jwt=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;'; // Remove JWT cookie
      navigate('/'); // Redirect to login or home page
    };
  
    // Function to navigate back to Admin Dashboard
    const goToUserDashboard = () => {
      navigate('/user/dashboard'); // Replace with your actual Admin Dashboard route
    };
  
  

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
       <div className="flex justify-between items-center mb-8">
      <button
        onClick={goToUserDashboard}
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Back to User Dashboard
      </button>
      <button
        onClick={handleLogout}
        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
      >
        Logout
      </button>
    </div>
      <div className="max-w-4xl mx-auto bg-white shadow-lg rounded-md p-6">
        {/* Restaurant Name */}
        <h1 className="text-4xl font-bold mb-6 text-center">{restaurant.name}</h1>

        {/* Address and Contact Info */}
        <p className="text-lg mb-4 text-gray-700">
          <span className="font-semibold">Address:</span> {restaurant.address}
        </p>
        <p className="text-lg mb-4 text-gray-700">
          <span className="font-semibold">Contact Info:</span> {restaurant.contactInfo || "N/A"}
        </p>

        {/* Hours */}
        <p className="text-lg mb-4 text-gray-700">
          <span className="font-semibold">Hours:</span> {restaurant.hours || "N/A"}
        </p>

        {/* Additional Information */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Cuisine:</span> {restaurant.cuisine || "N/A"}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Food Type:</span> {restaurant.foodType || "N/A"}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Price Range:</span> {restaurant.priceRange || "N/A"}
          </p>
        </div>

        {/* Rating and Description */}
        <p className="text-lg mb-4 text-gray-700">
          <span className="font-semibold">Rating:</span> {restaurant.rating || "N/A"}
        </p>
       

        {/* Map Section */}
        <div className="w-full h-60 md:h-80 mb-6">
          {restaurant.location?.latitude && restaurant.location?.longitude ? (
            <MapContainer
              center={[restaurant.location.latitude, restaurant.location.longitude]}
              zoom={15}
              scrollWheelZoom={false}
              className="w-full h-full rounded-md shadow"
            >
              <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
              />
              <Marker position={[restaurant.location.latitude, restaurant.location.longitude]}>
                <Popup>{restaurant.name}</Popup>
              </Marker>
            </MapContainer>
          ) : (
            <p className="text-gray-500 text-center">Location not available on map.</p>
          )}
        </div>

        {/* City, Zip Code, State, and Country */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <p className="text-lg text-gray-700">
            <span className="font-semibold">City:</span> {restaurant.city || "N/A"}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Zip Code:</span> {restaurant.zipCode || "N/A"}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">State:</span> {restaurant.state || "N/A"}
          </p>
          <p className="text-lg text-gray-700">
            <span className="font-semibold">Country:</span> {restaurant.country || "N/A"}
          </p>
        </div>

       
      </div>
    </div>
  );
};

export default ExternalRestaurantDetails;
