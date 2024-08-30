<#-- Email Template: Account Approval Notification -->

<#assign emailSubject = "🎉 Welcome to ${portalName} – Your Account is Ready!" />

<html>
<head>
<style>
body {
font-family: Arial, sans-serif;
background-color: #f8f8f8;
color: #333333;
line-height: 1.6;
}
.container {
max-width: 600px;
margin: 0 auto;
padding: 20px;
background-color: #ffffff;
border-radius: 8px;
box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.header {
text-align: center;
margin-bottom: 20px;
}
.header h1 {
color: #0066cc;
}
.content {
padding: 20px;
background-color: #f4f4f4;
border-radius: 8px;
}
.content p {
font-size: 16px;
margin: 10px 0;
}
.content a {
color: #0066cc;
text-decoration: none;
}
.footer {
text-align: center;
margin-top: 20px;
font-size: 14px;
color: #777777;
}
</style>
</head>
<body>
<div class="container">
        <div class="header">
            <h1>Welcome to ${portalName}!</h1>
        </div>
        <p>Dear ${userName},</p>

        <p>Congratulations! Your registration request has been approved. Below are your account details:</p>

        <div class="content">
            <p><strong>👤 Employee ID:</strong> ${employeeId}</p>
            <p><strong>🛠️ Role:</strong> ${roleName}</p>
            <p><strong>🔗 Login Link:</strong> <a href="${loginUrl}">Click here to login</a></p>
        </div>

        <p>You can use the Employee ID provided above to log in to the portal. If you have any questions or need further assistance, feel free to contact us.</p>

        <p>Thank you for being a part of Ekalavya Foundation. Welcome aboard! 🚀</p>

        <div class="footer">
            <p>Best Regards,</p>
        </div>
    </div>
</body>
</html>
