import React, { useState } from "react";
import { useLocation } from "react-router-dom"; // Import useLocation
import { useNavigate } from 'react-router-dom';

const ResetPassword = () =>{
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const location = useLocation(); // Retrieve state passed via navigate
    const email = location.state?.email; // Access the email from state
    const navigate = useNavigate();
    const [isResetted, setIsResetted] = useState(false);

    async function handleSubmit  (e)  {
        e.preventDefault();
    
        try {
            
            // Sending email and new password to the backend
            const response = await fetch("http://localhost:8080/api/auth/resetPassword", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email:email, newPassword }),
            });

                const data = await response.json();

                if (response.ok) {
                    setIsResetted(true); // Show success message
                    setTimeout(() => {
                      navigate('/') // Redirect after 3 seconds
                    }, 3000); // 3-second delay
                  }
            }
         catch (error) {
            console.error(error);
            alert("An error occurred while resetting your password. Please try again.");
        }
    }
    if (isResetted) {
        // Display success message after registration
        return (
          <div className="success-message">
            <h1>🎉 Congrats! Password resetted Successful 🎉</h1>
            <p>You will be redirected to the login page shortly...</p>
          </div>
        );
      }
    

    return (
        <div style={{ maxWidth: "400px", margin: "0 auto", padding: "1rem" }}>
            <h2>Reset Password</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: "1rem" }}>
                    <label htmlFor="newPassword" style={{ display: "block", marginBottom: "0.5rem" }}>
                        New Password
                    </label>
                    <input
                        type="password"
                        id="newPassword"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        placeholder="Enter your new password"
                        required
                        style={{
                            width: "100%",
                            padding: "0.5rem",
                            border: "1px solid #ccc",
                            borderRadius: "4px",
                        }}
                    />
                </div>
                <div style={{ marginBottom: "1rem" }}>
                    <label htmlFor="confirmPassword" style={{ display: "block", marginBottom: "0.5rem" }}>
                        Confirm Password
                    </label>
                    <input
                        type="password"
                        id="confirmPassword"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Confirm your new password"
                        required
                        style={{
                            width: "100%",
                            padding: "0.5rem",
                            border: "1px solid #ccc",
                            borderRadius: "4px",
                        }}
                    />
                </div>
                <button
                    type="submit"
                    style={{
                        width: "100%",
                        padding: "0.75rem",
                        backgroundColor: "#4CAF50",
                        color: "#fff",
                        border: "none",
                        borderRadius: "4px",
                        cursor: "pointer",
                    }}
                >
                    Save New Password
                </button>
            </form>
        </div>
    );
}

export default ResetPassword;
