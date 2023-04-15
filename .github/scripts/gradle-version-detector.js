module.exports = () => {
    const fs = require('fs');
    const path = require('path');
    const projectRoot = path.resolve(process.cwd());
    const propertiesFilePath = path.join(projectRoot, 'gradle/wrapper/gradle-wrapper.properties');
    const fileContent = fs.readFileSync(propertiesFilePath, 'utf8');
    const lines = fileContent.split(/\r?\n/);

    let gradleVersion = '';
    lines.forEach((line) => {
        if (line.includes("distributionUrl")) {
            gradleVersion = line.split('/').pop();
        }
    });

    if (gradleVersion === "") {
        throw new Error('Unable to find gradle version from gradle-wrapper.properties file');
    } else {
        console.log(`Found gradle version : ${gradleVersion}`);
        return gradleVersion;
    }
}
