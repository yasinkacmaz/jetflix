module.exports = async ({ core }) => {
    try {
        const gradleVersion = await extractGradleVersion();
        cacheGradleFiles(gradleVersion, core);
    } catch (error) {
        core.setFailed(`Action failed with error: ${error}`);
    }
};

async function extractGradleVersion() {
    const fs = require('fs');
    const path = require('path');
    const projectRoot = path.resolve(process.cwd());
    const propertiesFilePath = path.join(projectRoot, 'gradle/wrapper/gradle-wrapper.properties');
    const fileContent = fs.readFileSync(propertiesFilePath, 'utf8');
    const lines = fileContent.split(/\r?\n/);

    let gradleVersion = '';
    lines.forEach((line) => {
        if (line.includes("distributionUrl")) {
            const versionMatch = line.match(/gradle-(\d+\.\d+(?:\.\d+)?)-/);
            if (versionMatch && versionMatch[1]) {
                gradleVersion = versionMatch[1];
            }
        }
    });

    if (gradleVersion === "") {
        throw new Error('Unable to find gradle version from gradle-wrapper.properties file');
    } else {
        console.log(`Found gradle version: ${gradleVersion}`);
        return gradleVersion;
    }
}

function cacheGradleFiles(gradleVersion, core) {
    const gradleCachePaths = [
        '~/.gradle/caches',
        '~/.gradle/wrapper',
        '~/.gradle/configuration-cache',
        `~/.gradle/${gradleVersion}`
    ];

    core.setOutput('gradle-version', gradleVersion);
    core.setOutput('gradle-cache-key', `${process.platform}-gradle-${gradleVersion}`);
    core.setOutput('gradle-cache-paths', gradleCachePaths.join('\n'));
    core.setOutput('build-cache-key', `${process.platform}-build-${gradleVersion}`);
    core.setOutput('build-cache-paths', `**/build`);
}
