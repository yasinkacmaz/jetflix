module.exports = (subject, report) => {
    const execSync = require('child_process').execSync
    execSync(`npm install nodemailer`)
    const nodemailer = require('nodemailer')
    const transporter = nodemailer.createTransport({
        service: "Hotmail",
        auth: {
            user: `${process.env.MAIL_USERNAME}`,
            pass: `${process.env.MAIL_PASSWORD}`
        }
    });

    const mailOptions = {
        from: {
            name: 'Jetflix',
            address: process.env.MAIL_USERNAME
        },
        to: 'yasinkacmaz57@gmail.com',
        subject: `${subject}`,
        text: `${report}`
    };

    transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            console.log(error)
        }
    });
}
