<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verification Code</title>
    <style>
        /* General Resets */
        body, table, td, a {
text-size-adjust: 100%;
-ms-text-size-adjust: 100%;
-webkit-text-size-adjust: 100%;
}
        table, td {
border-collapse: collapse !important;
}
        img {
-ms-interpolation-mode: bicubic;
}

        /* Container Styles */
        body {
margin: 0;
padding: 0;
width: 100%;
background-color: #f2f2f2;
}
.container {
max-width: 600px;
margin: 0 auto;
background-color: #ffffff;
padding: 20px;
border-radius: 5px;
box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
}
.container-table {
width: 100%;
border-spacing: 0;
}

/* Content Styles */
.logo {
text-align: center;
margin-bottom: 30px;
}
.logo img {
max-width: 100px;
height: auto;
}
h2 {
text-align: center;
color: #333333;
font-family: sans-serif;
font-size: 24px;
margin-bottom: 20px;
}
p {
font-family: sans-serif;
font-size: 16px;
line-height: 1.6;
color: #555555;
margin: 0 0 20px 0;
}
.code {
text-align: center;
font-size: 30px;
font-weight: bold;
color: #333333;
margin-bottom: 20px;
}
.footer {
text-align: center;
font-family: sans-serif;
color: #777777;
font-size: 14px;
margin-top: 30px;
}
.footer a {
color: #333333;
text-decoration: none;
}

/* Outlook-Specific Fixes */
.mso-hide {
display: none;
}

/* Responsive Media Query */
@media only screen and (max-width: 600px) {
.container {
width: 100% !important;
}
}
</style>
</head>
<body>
<table role="presentation" class="container" cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <table role="presentation" class="container-table">
                    <tr>
                        <td class="logo">
                            <img src="cid:logoImage" alt="Logo">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h2>Verification Code</h2>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>Please use the verification code below to sign in.</p>
                        </td>
                    </tr>
                    <tr>
                        <td class="code">
                            ${code}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>If you didn't request this, you can ignore this email.</p>
                        </td>
                    </tr>
                    <tr>
                        <td class="footer">
                            Thanks,<br>
                            The Tech4Hope team
                        </td>
                    </tr>
                    <tr>
                        <td class="footer">
                            Made with ❤️ in India
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>
