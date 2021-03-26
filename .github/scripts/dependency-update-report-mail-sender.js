module.exports = ({ }) => {
    const execSync = require('child_process').execSync
    execSync(`npm install nodemailer`)
    const nodemailer = require('nodemailer')
    const transporter = nodemailer.createTransport({
        host: "smtp.live.com",
        port: 587,
        secureConnection: false,
        auth: {
            user: `${process.env.MAIL_USERNAME}`,
            pass: `${process.env.MAIL_PASSWORD}`
        },
        tls: {
            ciphers: 'SSLv3'
        }
    });
    const report = require('fs').readFileSync('build/dependencyUpdates/dependency_update_report.txt', 'utf8')

    const mailOptions = {
        from: {
            name: 'Jetflix',
            address: process.env.MAIL_USERNAME
        },
        to: 'yasinkacmaz57@gmail.com',
        subject: 'Dependency update report of Jetflix ¯\\_(ツ)_/¯',
        text: `${report}`
    };

    transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            console.log(error)
        }
    });
}
