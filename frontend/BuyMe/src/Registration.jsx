import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './assets/style.css';

export default function Registration() {
  const [userName, setUserName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('');
  const [error, setError] = useState(null);
  const [isRegistered, setIsRegistered] = useState(false); // Registration success state
  const navigate = useNavigate();

  async function handleSignUp(e) {
    e.preventDefault();
    setError(null); // Clear previous errors

    try {
      const response = await fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userName, email, password, role }),
      });

      const data = await response.json();

      if (response.ok) {
        console.log('User registered successfully:', data);
        setIsRegistered(true); // Show success message
        setTimeout(() => {
          navigate('/'); // Redirect after 3 seconds
        }, 3000); // 3-second delay
      } else {
        throw new Error(data.error || 'Registration failed');
      }
    } catch (err) {
      setError(err.message);
    }
  }

  if (isRegistered) {
    // Display success message after registration
    return (
      <div className="page-container">
        <div className="form-container">
          <div className="success-message">
            <h1>🎉 Congrats! Registration Successful 🎉</h1>
            <p>You will be redirected to the login page shortly...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="form-container">
        <h1 className="form-title">Register</h1>
        {error && <p className="error-message">{error}</p>}
        <form onSubmit={handleSignUp} className="form-content">
          <div className="form-group">
            <label htmlFor="username" className="form-label">Username</label>
            <input
              id="username"
              type="text"
              placeholder="Enter your username"
              value={userName}
              onChange={(e) => setUserName(e.target.value)}
              required
              className="form-input"
            />
          </div>
          <div className="form-group">
            <label htmlFor="email" className="form-label">Email</label>
            <input
              id="email"
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="form-input"
            />
          </div>
          <div className="form-group">
            <label htmlFor="password" className="form-label">Password</label>
            <input
              id="password"
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="form-input"
            />
          </div>
          <div className="form-group">
            <label htmlFor="role" className="form-label">Role</label>
            <select
              id="role"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              required
              className="form-select"
            >
              <option value="" disabled>Select your role</option>
              <option value="customer">Customer</option>
              <option value="admin">Admin</option>
            </select>
          </div>
          <button type="submit" className="form-button">Sign Up</button>
        </form>
        <p className="form-footer">
          Already a user?{' '}
          <a href="/" className="form-link">Log in here</a>
        </p>
      </div>
    </div>
  );
}
