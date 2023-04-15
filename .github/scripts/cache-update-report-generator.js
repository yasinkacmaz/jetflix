module.exports = async () => {
    try {
      const cacheData = await getCacheData();
      let report = `Found ${cacheData.total_count} caches for Jetflix. Here are the status of them:`;
      const locale = 'tr-TR';
      cacheData.actions_caches.forEach(cache => {
        report += `\n\nBranch: ${cache.ref.split('/').pop()}\nName: ${cache.key}\n`;
        const accessed = new Date(cache.last_accessed_at);
        const created = new Date(cache.created_at);
        report += `Created: ${created.toLocaleDateString(locale)} ${created.toLocaleTimeString(locale)}\n`;
        report += `Last accessed: ${accessed.toLocaleDateString(locale)} ${accessed.toLocaleTimeString(locale)}\n`;
        const sizeInGb = cache.size_in_bytes / 1024 / 1024 / 1024;
        const sizeInMb = cache.size_in_bytes / 1024 / 1024;
        let sizeString = '';
        if (sizeInGb >= 1) {
          sizeString = `${sizeInGb.toFixed(2)} GB`;
        } else {
          sizeString = `${sizeInMb.toFixed(2)} MB`;
        }
        report += `Size: ${sizeString}`
      });
      return report;
    } catch (error) {
      return error.message;
    }
}

async function getCacheData() {
    const { Octokit } = require("@octokit/action");
    const { Octokit, App } = require("octokit");
    const octokit = new Octokit({ auth: process.env.GITHUB_TOKEN });
    const [owner, repo] = process.env.GITHUB_REPOSITORY.split("/");
    const response = await octokit.request('GET /repos/{owner}/{repo}/actions/caches', {
        owner: owner,
        repo: repo
    });
    return response.data;
}
