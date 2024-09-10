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
    const cacheKey = `${process.platform}-gradle-${gradleVersion}`;
    const cachePaths = [
        '~/.gradle/caches',
        '~/.gradle/wrapper',
        '~/.gradle/configuration-cache',
        `~/.gradle/${gradleVersion}`
    ];

    core.setOutput('cache-key', cacheKey);
    core.setOutput('cache-paths', cachePaths.join('\n'));
}
