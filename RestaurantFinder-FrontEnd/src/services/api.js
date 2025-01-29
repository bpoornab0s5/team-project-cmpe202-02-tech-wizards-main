import { Cookies } from "react-cookie";
import config from "./config"; 



const getTokenFromCookies = () => {
  const cookies = document.cookie.split(';').reduce((acc, cookie) => {
    const [key, value] = cookie.trim().split('=');
    acc[key] = value;
    return acc;
  }, {});
  return cookies['jwt']; 
}; 



const cookies = new Cookies();

// Function to make API requests with authentication
// Function to make API requests with authentication
// export const fetchWithAuth = async (endpoint, options = {}) => {
//   const token = getTokenFromCookies(); // Correctly fetch token from cookies

//   const headers = {
//     "Content-Type": "application/json",
//     Authorization: token ? `Bearer ${token}` : "",
//     ...options.headers, // Spread options.headers to include additional headers
//   };

//   try {
//     const response = await fetch(`${config.BACKEND_URL}${endpoint}`, {
//       ...options,
//       headers,
//     });

//     if (!response.ok) {
//       throw new Error("Failed to fetch data from server.");
//     }

//     return await response.json();
//   } catch (error) {
//     console.error("Fetch error:", error);
//     return null;
//   }
// };

export const fetchWithAuth = async (endpoint, options = {}) => {
  const token = document.cookie
    .split("; ")
    .find((row) => row.startsWith("jwt="))
    ?.split("=")[1];

  const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
    ...options.headers,
  };

  try {
    const response = await fetch(`${config.BACKEND_URL}${endpoint}`, {
      ...options,
      headers,
    });

    if (!response.ok) {
      throw new Error("Failed to fetch data from server.");
    }

    return await response.json();
  } catch (error) {
    console.error("Fetch error:", error);
    return null;
  }
};

// Function to store the JWT token in cookies
export const setToken = (token) => {
  cookies.set("jwt", token, { path: "/", sameSite: "strict", secure: true });
};

// Function to retrieve the JWT token from cookies
export const getToken = () => cookies.get("jwt");

// Function to remove the JWT token from cookies
export const removeToken = () => {
  cookies.remove("jwt", { path: "/" });
};

// Add Restaurant API
// api.js (No changes needed here; ensure `formData` is passed correctly)
export const addRestaurant = async (restaurantData) => {
  try {
    const token = getTokenFromCookies();
    const response = await fetch(`${config.BACKEND_URL}/api/business-owner/restaurants`, {
      method: "POST",
      headers:{
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(restaurantData),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || "Failed to add restaurant.");
    }
 
    return await response.json();
  } catch (error) {
    throw error.message || "Network error occurred.";
  }
};


// Fetch Restaurants by Business Owner ID API
export const getRestaurantsByOwner = async (ownerId) => {
try {
  const token = getTokenFromCookies();
  const response = await fetch(`${config.BACKEND_URL}/api/business-owner/restaurants?ownerId=${ownerId}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json" // Token
    }
  
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.error || "Failed to fetch restaurants.");
  }

  return await response.json();
} catch (error) {
  throw error.message || "Network error occurred.";
}
};

// Update Restaurant Details API
export const updateRestaurant = async (id, restaurantData) => {
try {
  const token = getTokenFromCookies();
  const response = await fetch(`${config.BACKEND_URL}/api/business-owner/restaurants/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json" // Token
    },
    body: JSON.stringify(restaurantData),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.error || `Failed to update restaurant with ID ${id}`);
  }

  return await response.json();
} catch (error) {
  throw error.message || "Network error occurred.";
}
};

// Delete Restaurant API
export const deleteRestaurant = async (id) => {
try {
  const token = getTokenFromCookies();
  const response = await fetch(`${config.BACKEND_URL}/api/business-owner/restaurants/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json" // Token
    },
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.error || `Failed to delete restaurant with ID ${id}`);
  }
} catch (error) {
  throw error.message || "Network error occurred.";
}
};

// Upload Restaurant Photo API
export const uploadRestaurantPhoto = async (id, file) => {
  const formData = new FormData();
  formData.append("file", file);

  try {
    const token = getTokenFromCookies();
    const response = await fetch(`${config.BACKEND_URL}/api/business-owner/restaurants/${id}/photos`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`, // Token
      },
      body: formData,
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to upload photo.");
    }

    const responseText = await response.text(); // Extract the plain-text response
    const urlMatch = responseText.match(/https?:\/\/[^\s]+/); // Extract the photo URL using regex
    if (!urlMatch) throw new Error("Photo URL not found in response.");

    return { url: urlMatch[0], message: "Photo uploaded successfully" };
  } catch (error) {
    throw error.message || "Network error occurred.";
  }
};

export const getOwnerIdFromToken = () => {
  const token = getTokenFromCookies();
  if (!token) {
    throw new Error("JWT token not found in localStorage.");
  }

  // Decode the token payload
  const payload = JSON.parse(atob(token.split(".")[1]));
  return payload.id; // Extract the `id` (ownerId) from the token
};

