import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import RootPage from "./components/RootPage";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import UserDashboard from "./components/UserDashboard";
import BusinessOwnerDashboard from "./components/BusinessOwnerDashboard";
import AdminDashboard from "./components/AdminDashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import DuplicateRestaurants from "./components/DuplicateRestaurants";
import ProcessReportedRestaurants from "./components/ProcessReportedRestaurants";
import ManageUsers from "./components/ManageUsers";
import UserProfile from "./components/UserProfile";
import RestaurantDetails from "./components/RestaurantDetails";
import AddRestaurantForm from "./components/AddRestaurantForm"
import EditRestaurantPage from "./components/EditRestauarantPage"
import ExternalRestaurantDetails from "./components/ExternalRestaurantDetails";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<RootPage />} />
        <Route path="/login/:role" element={<LoginPage />} />
        <Route path="/register/:role" element={<RegisterPage />} />
        <Route
          path="/user/dashboard"
          element={
            <ProtectedRoute requiredRole="ROLE_RegularUser">
              <UserDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/business-owner/dashboard"
          element={
            <ProtectedRoute requiredRole="ROLE_BusinessOwner">
              <BusinessOwnerDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute requiredRole="ROLE_Admin">
              <AdminDashboard >          
              </AdminDashboard>
            </ProtectedRoute>
          }
        />
        <Route path="/admin/duplicates" element={<ProtectedRoute requiredRole="ROLE_Admin">
          <DuplicateRestaurants />
            </ProtectedRoute>} />
    
            <Route path="/admin/reported" element={<ProtectedRoute requiredRole="ROLE_Admin">
          <ProcessReportedRestaurants />
            </ProtectedRoute>} />
    
            <Route path="/admin/users" element={<ProtectedRoute requiredRole="ROLE_Admin">
          <ManageUsers />
            </ProtectedRoute>} />

            <Route path="/user/profile" element={<UserProfile />} />

            <Route path="/restaurant/:id" element={<RestaurantDetails />} />

        

        <Route
          path="/business-owner/home"
          element={
            <ProtectedRoute requiredRole="ROLE_BusinessOwner">
              <BusinessOwnerDashboard/>
            </ProtectedRoute>
          }
        />
        <Route
          path="/business-owner/restaurants"
          element={
            <ProtectedRoute requiredRole="ROLE_BusinessOwner">
              <AddRestaurantForm />
            </ProtectedRoute>
          }
        />
        <Route
          path="/edit-restaurant/:id"
          element={
            <ProtectedRoute requiredRole="ROLE_BusinessOwner">
              <EditRestaurantPage />
            </ProtectedRoute>
          }
        />

<Route path="/external-restaurant-details" element={<ExternalRestaurantDetails />} />
    
      </Routes>
          

    </Router>
  );
}

export default App;
