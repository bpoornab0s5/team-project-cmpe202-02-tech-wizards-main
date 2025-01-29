import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetchWithAuth } from "../services/api";
import { getOwnerIdFromToken } from "../services/api";
import PhotoSlider from "./PhotoSlider";


const RestaurantDetails = () => {
  const { id } = useParams(); // Get the restaurant ID from the URL
  const [restaurant, setRestaurant] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [reviewSummary, setReviewSummary] = useState({ averageRating: 0, totalReviews: 0 });
  const [newReview, setNewReview] = useState({ rating: "", comment: "" });
  const [usernames, setUsernames] = useState({}); // Map userId -> username
  const navigate = useNavigate();

  const currentUserId = getOwnerIdFromToken();

  // Fetch restaurant details, reviews, and review summary
  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const restaurantResponse = await fetchWithAuth(`/api/restaurants/${id}`);
        setRestaurant(restaurantResponse);

        const reviewsResponse = await fetchWithAuth(`/api/restaurants/${id}/reviews`);
        setReviews(reviewsResponse || []);

        const reviewSummaryResponse = await fetchWithAuth(`/api/restaurants/${id}/reviews-summary`);
        setReviewSummary(reviewSummaryResponse || { averageRating: 0, totalReviews: 0 });

        // Fetch usernames for all unique userIds in reviews
        const uniqueUserIds = [...new Set(reviewsResponse.map((review) => review.userId))];
        uniqueUserIds.forEach((userId) => fetchUserProfile(userId));
      } catch (error) {
        console.error("Failed to fetch details:", error);
      }
    };

    fetchDetails();
  }, [id]);

  // Fetch user profile and update usernames state
  const fetchUserProfile = async (userId) => {
    try {
      const response = await fetchWithAuth(`/api/users/${userId}`);
      setUsernames((prevUsernames) => ({
        ...prevUsernames,
        [userId]: response.username, // Assuming API returns { username: "..." }
      }));
    } catch (error) {
      console.error(`Failed to fetch user profile for userId ${userId}:`, error);
    }
  };

  // Handle adding a new review
  const handleAddReview = async () => {
    if (!newReview.rating || !newReview.comment) {
      console.error("Both rating and comment are required!");
      return;
    }

    try {
      const payload = {
        rating: parseFloat(newReview.rating),
        comment: newReview.comment,
        userId: currentUserId,
      };
      await fetchWithAuth(`/api/restaurants/${id}/reviews`, {
        method: "POST",
        body: JSON.stringify(payload),
      });
      setNewReview({ rating: "", comment: "" }); // Reset form
      const updatedReviews = await fetchWithAuth(`/api/restaurants/${id}/reviews`);
      setReviews(updatedReviews); // Refresh reviews

      const updatedSummary = await fetchWithAuth(`/api/restaurants/${id}/reviews-summary`);
      setReviewSummary(updatedSummary); // Refresh review summary
    } catch (error) {
      console.error("Failed to add review:", error);
    }
  };

  // Handle reporting a restaurant
  const handleReportRestaurant = async () => {
    try {
      await fetchWithAuth(`/api/restaurants/${id}/report-closed`, { method: "POST" });
      alert("Restaurant reported successfully.");
    } catch (error) {
      console.error("Failed to report restaurant:", error);
    }
  };

  // Handle editing a review
 const handleEditReview = async (reviewId) => {
  const updatedComment = prompt("Edit your review:");
  const updatedRating = prompt("Edit your rating (1-5):");

  // Validate input
  if (!updatedComment || !updatedRating) {
    alert("Both comment and rating are required!");
    return;
  }

  // Validate rating is between 1 and 5
  const numericRating = parseFloat(updatedRating);
  if (numericRating < 1 || numericRating > 5) {
    alert("Rating must be a number between 1 and 5.");
    return;
  }

  try {
    const payload = {
      comment: updatedComment,
      rating: numericRating,
      userId: currentUserId,
    };
    await fetchWithAuth(`/api/restaurants/${id}/reviews/${reviewId}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
    const updatedReviews = await fetchWithAuth(`/api/restaurants/${id}/reviews`);
    setReviews(updatedReviews); // Refresh reviews

    const updatedSummary = await fetchWithAuth(`/api/restaurants/${id}/reviews-summary`);
    setReviewSummary(updatedSummary); // Refresh review summary
  } catch (error) {
    console.error("Failed to edit review:", error);
  }
};


  // Handle deleting a review
  const handleDeleteReview = async (reviewId) => {
    try {
      await fetchWithAuth(`/api/restaurants/${id}/reviews/${reviewId}`, { method: "DELETE" });
      const updatedReviews = await fetchWithAuth(`/api/restaurants/${id}/reviews`);
      setReviews(updatedReviews); // Refresh reviews

      const updatedSummary = await fetchWithAuth(`/api/restaurants/${id}/reviews-summary`);
      setReviewSummary(updatedSummary); // Refresh review summary
    } catch (error) {
      console.error("Failed to delete review:", error);
    }
  };

  // Render text-based stars for the average rating
  const renderTextStars = (averageRating) => {
    const fullStar = "★"; // Filled star
    const emptyStar = "☆"; // Empty star
    const totalStars = 5;

    // Generate stars based on the average rating
    const filledStars = fullStar.repeat(Math.floor(averageRating)); // Full stars
    const halfStars = averageRating % 1 > 0.5 ? fullStar : ""; // Optional half-star (if needed)
    const remainingStars = emptyStar.repeat(totalStars - Math.ceil(averageRating)); // Empty stars

    return `${filledStars}${halfStars}${remainingStars}`;
  };

  if (!restaurant) {
    return <p>Loading...</p>;
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

    const renderStars = (rating) => {
      const fullStar = "★"; // Filled star
      const emptyStar = "☆"; // Empty star
      const totalStars = 5;
    
      // Generate stars based on the rating value
      const filledStars = fullStar.repeat(rating); // Full stars
      const emptyStars = emptyStar.repeat(totalStars - rating); // Remaining empty stars
    
      return `${filledStars}${emptyStars}`;
    };
    
  
  
  return (
    <div className="container mx-auto p-6 bg-gray-100 min-h-screen">
    {/* Restaurant Details */}

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
    <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
      <div className="flex justify-between items-start">
        <div>
          <h1 className="text-4xl font-bold text-gray-800">{restaurant.name}</h1>
          <div className="flex items-center mt-2">
            <span className="text-yellow-500 text-2xl">{renderTextStars(reviewSummary.averageRating)}</span>
            <span className="ml-2 text-gray-600 text-lg">
              ({reviewSummary.averageRating.toFixed(1)} from {reviewSummary.totalReviews} reviews)
            </span>
          </div>
          <p className="text-gray-600 mt-4">{restaurant.description}</p>
        </div>
        <button
          className="bg-red-600 text-white px-4 py-2 rounded-lg shadow hover:bg-red-700 transition"
          onClick={handleReportRestaurant}
        >
          Report
        </button>
      </div>
      <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <p className="text-gray-700"><strong>Address:</strong> {restaurant.address}</p>
          <p className="text-gray-700">
            <strong>Location:</strong> {restaurant.city}, {restaurant.state}, {restaurant.zipCode}
          </p>
          <p className="text-gray-700"><strong>Contact:</strong> {restaurant.contactInfo}</p>
        </div>
        <div>
          <p className="text-gray-700"><strong>Cuisine:</strong> {restaurant.cuisine}</p>
          <p className="text-gray-700"><strong>Food Type:</strong> {restaurant.foodType}</p>
          <p className="text-gray-700"><strong>Price Range:</strong> {restaurant.priceRange}</p>
        </div>
        <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
        <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
  <div >
    <div >
      <PhotoSlider photos={restaurant.photos} />
    </div>
  </div>
</div>

</div>
      </div>
      
    </div>

    {/* Add Review Section */}
    <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
      
      <h2 className="text-2xl font-bold text-gray-800 mb-4">Add a Review</h2>
      <input
  type="number"
  placeholder="Rating (1-5)"
  className="border rounded-lg p-2 w-full mb-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
  value={newReview.rating}
  onChange={(e) => {
    const value = e.target.value;
    if (value === "" || (Number(value) >= 1 && Number(value) <= 5)) {
      setNewReview({ ...newReview, rating: value });
    } else {
      alert("Rating must be a number between 1 and 5.");
    }
  }}
/>

      <textarea
        placeholder="Your comment"
        className="border rounded-lg p-2 w-full mb-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
        value={newReview.comment}
        onChange={(e) => setNewReview({ ...newReview, comment: e.target.value })}
      ></textarea>
      <button
        className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 transition"
        onClick={handleAddReview}
      >
        Submit Review
      </button>
    </div>

    {/* Reviews Section */}
    <div className="bg-white shadow-lg rounded-lg p-6">
      <h2 className="text-2xl font-bold text-gray-800 mb-4">Reviews</h2>
      {reviews.length === 0 ? (
        <p className="text-gray-500">No reviews yet. Be the first to add one!</p>
      ) : (
        <div className="space-y-4">
          {reviews.map((review) => (
            <div key={review.id} className="bg-gray-50 border border-gray-200 p-4 rounded-lg shadow hover:shadow-lg transition">
              <div className="flex justify-between items-center">
                <p className="text-lg font-bold text-gray-800">{usernames[review.userId] || "Anonymous"}</p>
                <p className="text-yellow-500 text-sm">{renderStars(review.rating)}</p>

              </div>
              <p className="text-gray-700 mt-2">{review.comment}</p>
              {review.userId === currentUserId && (
                <div className="flex space-x-2 mt-4">
                  <button
                    className="bg-yellow-500 text-white px-4 py-2 rounded-lg hover:bg-yellow-600"
                    onClick={() => handleEditReview(review.id)}
                  >
                    Edit
                  </button>
                  <button
                    className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
                    onClick={() => handleDeleteReview(review.id)}
                  >
                    Delete
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  </div>
  );
};

export default RestaurantDetails;
