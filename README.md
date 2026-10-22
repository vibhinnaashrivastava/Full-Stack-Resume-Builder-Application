# ResumeCraft frontend
React + Vite + Tailwind + Axios starter for the Resume Builder Spring Boot backend.

Run: copy `.env.example` to `.env`, then `npm install`, then `npm run dev`.

Endpoints used: `/auth/resume/register`, `/auth/resume/login-user`, `/auth/resume/profile`, `/auth/resume/resend-verification`, `/resume/create-resume`, `/resume/get-resume`, `/resume/{resumeId}`, `/api/templates/get-templates`, `/api/payment/create-order`, `/api/payment/verify-order`, `/api/email/send-resume`.

Check response JSON and DTO property names against your actual backend. Razorpay checkout requires the public keyId in the create-order response; never expose the secret key. CORS must allow `http://localhost:5173`.
