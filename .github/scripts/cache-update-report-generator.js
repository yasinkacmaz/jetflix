module.exports = async ({github, context}) => {
    try {
      const cachesResponse = await github.rest.actions.getActionsCacheList({
        owner: context.repo.owner,
        repo: context.repo.repo,
      });
      const caches = cachesResponse.data;
      console.log(caches);
      let report = `Found ${caches.total_count} caches for Jetflix. Here are the status of them:`;
      const locale = 'tr-TR';
      caches.actions_caches.forEach(cache => {
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
