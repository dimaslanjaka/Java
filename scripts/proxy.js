var ProxyFinder = require("public-proxy-finder");

/*
ProxyFinder.US();
ProxyFinder.UK();
ProxyFinder.socks();
ProxyFinder.anonymous();

// Convenience method to combine the above into one resultset
ProxyFinder.all();

*/

const result = [];

/**
 * @example
 * (async function () {
 *    console.log(await proxy());
 * })();
 * @return {Promise<string[]>} ip:port[]
 */
async function proxy() {
	await ProxyFinder.ssl() //Retrieve an up-to-date list of SSL proxies
		.filter(function (proxy) {
			// Filter out any proxies that haven't been checked for over X minutes
			return proxy["Last Checked"] < 360 * 60 * 1000;
		})
		.then(function (proxies) {
			//console.dir(proxies);
			proxies.forEach((proxy) => {
				result.push(`${proxy["IP Address"]}:${proxy["Port"]}`);
			});
		});
	return result;
}

/**
 * @example
 * (async function () {
 *    console.log(await randProxy());
 * })();
 * @return {Promise<string>} ip:port
 */
async function randProxy() {
	const array = await proxy();
	return array[Math.floor(Math.random() * array.length)];
}

module.exports = {
	proxy: proxy,
	random: randProxy,
};
