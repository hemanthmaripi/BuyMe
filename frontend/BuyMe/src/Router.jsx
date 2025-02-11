import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Login from './Login';
import Registration from './Registration';
import ForgotPassword from './ForgotPasword';
import OtpValidation from './OtpValidation';
import PasswordReset from './PasswordReset';
import CustomerHome from './CustomerHomePage';
import CartPage from "./CartPage";
import OrderPage from './OrderPage';
import AdminLogin from "./AdminLogin"; 
import AdminDashboard from "./AdminDashBoard";



const AppRoutes = () => { 

    return ( 
    <Routes> 

        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Registration />} />
        <Route path="/forget" element={<ForgotPassword />} />
        <Route path='/validatingotp' element={<OtpValidation />} />
        <Route path='/passwordReset' element={<PasswordReset/>} /> 
        <Route path='/customerHome' element={<CustomerHome />} />
        <Route path="/UserCartPage" element={<CartPage />} />
        <Route path="/orders" element={<OrderPage />} />
        <Route path="/admin" element={<AdminLogin />} />
        <Route path="/admindashboard" element={<AdminDashboard />} />
        {/* Add more routes here as your app grows */} 

    </Routes> );
    
}; export default AppRoutes;