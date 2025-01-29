import React from "react";
import { Navigate } from "react-router-dom";
import { getToken } from "../services/api";
import { jwtDecode } from "jwt-decode";

const ProtectedRoute = ({ children, requiredRole }) => {
  const token = getToken();

  if (!token) {
    return <Navigate to="/" />;
  }

  const decoded = jwtDecode(token);

  if (decoded.role !== requiredRole) {
    return <Navigate to="/" />;
  }

  return children;
};

export default ProtectedRoute;
