    import React, { useState } from "react";
    import { useLocation } from "react-router-dom"; // Import useLocation
    import { useNavigate } from "react-router-dom";

    const OTPVerificationForm = () => {
    const location = useLocation(); // Retrieve state passed via navigate
    const email = location.state?.email; // Access the email from state
    const [otp, setOtp] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    async function handleSubmit(e) {
            e.preventDefault();
            setLoading(true);
            setMessage("");

            try {
                // Make API call to verify OTP
                const response = await fetch("http://localhost:8080/api/auth/sendOtp", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email,   otp }),
                });

                const data = await response.json();
                if (response.ok) {
                    setMessage(data.message || "OTP verified successfully. Proceed to reset password.");
                    // Add logic to navigate to reset password page
                    navigate('/passwordReset', { state: { email } })
                } else {
                    setMessage(data.error || "Invalid OTP. Please try again.");
                }
            } catch (error) {
                setMessage(error.body);
            } finally {
                setLoading(false);
            }
        }

    return (
        <div style={{ maxWidth: "400px", margin: "auto", textAlign: "center" }}>
        <h2>Verify OTP</h2>
        <p>Email: {email}</p> {/* Show email for reference */}
        <form onSubmit={handleSubmit}>
            <label htmlFor="otp">Enter OTP:</label>
            <input
            type="text"
            id="otp"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            required
            placeholder="Enter the OTP sent to your email"
            style={{
                width: "100%",
                padding: "10px",
                margin: "10px 0",
                borderRadius: "4px",
                border: "1px solid #ccc",
            }}
            />
            <button
            type="submit"
            disabled={loading}
            style={{
                padding: "10px 20px",
                backgroundColor: "#28a745",
                color: "#fff",
                border: "none",
                borderRadius: "4px",
                cursor: loading ? "not-allowed" : "pointer",
            }}
            >
            {loading ? "Verifying..." : "Verify OTP"}
            </button>
        </form>
        {message && <p style={{ marginTop: "10px", color: message.includes("success") ? "green" : "red" }}>{message}</p>}
        </div>
    );
    };

    export default OTPVerificationForm;
