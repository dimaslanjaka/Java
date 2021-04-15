const axios = require("axios").default;
const parse_url = require("url");
const { serialize } = require("v8");
var urltobesent = "https://jitpack.io";
var silent = true;
var url = "https://jitpack.io";
var parse = parse_url.parse(
	"http://backend.webmanajemen.com/artifact/servers/index.php"
);
var proxy = require("./proxy");
const axiosCookieJarSupport = require("axios-cookiejar-support").default;
const tough = require("tough-cookie");
axiosCookieJarSupport(axios);
const cookieJar = new tough.CookieJar();
const fs = require("fs");
const path = require("path");
const qs = require("querystring");

/**
 * @type {import("axios").AxiosRequestConfig} config
 */
const config = {
	url: parse.pathname,
	method: "post",
	baseURL: `${parse.protocol}//${parse.hostname}/`,
	headers: {
		"Content-Type": "application/x-www-form-urlencoded",
		"User-Agent":
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36",
		Referer: "https://www.google.com/",
		"Accept-Language": "id-ID,id;q=0.9,en-US;q=0.8,en;q=0.7,ms;q=0.6",
		Accept:
			"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		DNT: 1,
	},
	jar: cookieJar, // tough.CookieJar or boolean
	withCredentials: true, // If true, send cookie stored in jar
	params: { upload: new Date().toLocaleDateString() },
	data: qs.stringify({ server: urltobesent }),
	timeout: 10000,
	validateStatus: (status) => status >= 200 && status < 300,
	maxRedirects: 5,
};

/**
 * modify config with new config
 * @param {string} url
 * @param {import("axios").AxiosRequestConfig} newConf
 * @return {import("axios").AxiosRequestConfig} new configuration
 */
function modifyConfig(url, newConf) {
	if (typeof url == "string") {
		parse = parse_url.parse(url);
		config.url = parse.pathname;
		config.baseURL = `${parse.protocol}//${parse.hostname}/`;
		config.params = parse.query;
	}
	if (typeof newConf == "object") {
		for (var key in newConf) {
			//console.log(`config has ${key} == ${config.hasOwnProperty(key)}`);
			//console.log(`newConf has ${key} == ${newConf.hasOwnProperty(key)}`);
			if (config.hasOwnProperty(key) || newConf.hasOwnProperty(key)) {
				if (JSON.stringify(config[key]) != JSON.stringify(newConf[key])) {
					console.log(
						`${key} = ${JSON.stringify(config[key])} > ${JSON.stringify(
							newConf[key]
						)}`
					);
					config[key] = newConf[key];
				}
			}
		}
	}
	return config;
}

/**
 * Response Handler
 * @param {import("axios").AxiosResponse} response
 */
function handleResponse(response) {
	if (!silent) {
		console.log(response.data);
		console.log(response.status, response.statusText);
		//console.log(response.headers);
		console.log(
			response.config.baseURL,
			response.config.url,
			response.config.data
		);
	}
}
/**
 * Error Handling
 * @param {import("axios").AxiosError} error
 */
const handleError = (error) => {
	if (!silent) {
		if (error.response) {
			//console.log(error.request);
			console.log(error.response.data);
			console.log(error.response.status);
			console.log(error.response.headers);
		} else {
			console.log(error.message);
		}
	}
};

/**
 *
 * @param {string} repourl
 * @param {boolean} wSilent
 * @param {import("axios").AxiosRequestConfig} overrideConfig
 */
async function sendServer(backend, repourl, wSilent, overrideConfig) {
	var nProxy = await proxy.random();
	var ip = nProxy.length > 0 ? nProxy.split(":")[0] : null;
	var port = nProxy.length > 0 ? nProxy.split(":")[1] : null;

	/**
	 * @type {import("axios").AxiosRequestConfig} buildNewConf
	 */
	var buildNewConf = {
		data: {
			server: repourl,
		},
		method: "POST",
	};
	if (port != null && ip != null) {
		buildNewConf.proxy = {};
		buildNewConf.proxy.host = ip;
		buildNewConf.proxy.port = port;
	}
	var url =
		typeof backend == "string"
			? backend
			: "http://backend.webmanajemen.com/artifact/servers/index.php";
	var newConf = modifyConfig(url, buildNewConf);
	silent = wSilent;

	if (typeof overrideConfig == "object") {
		Object.keys(overrideConfig).forEach(function (key) {
			newConf[key] = overrideConfig[key];
		});
	}

	//config.headers["Content-Type"] = "application/x-www-form-urlencoded";
	//config.data = qs.stringify({ server: repourl });

	return (
		axios(config)
			//.post(url, qs.stringify(config.data), config)
			.then(function (response) {
				saveCookie(response);
				console.log(response.data);
			})
			.catch(
				/**
				 * Error Handling
				 * @param {import("axios").AxiosError} response
				 */
				function (response) {
					console.clear();
					saveCookie(response);
					sendServer(backend, repourl, wSilent);
				}
			)
	);
}

const locationCookie = path.join(__dirname, "tmp", "cookies.json");
/**
 * Error Handling
 * @param {import("axios").AxiosError | import("axios").AxiosResponse} response
 */
function saveCookie(response) {
	//console.log(JSON.stringify(cookieJar));
	const config = response.config;
	if (!fs.existsSync(path.dirname(locationCookie)))
		fs.mkdirSync(path.dirname(locationCookie), { recursive: true });
	// axios.defaults.jar === config.jar
	fs.writeFileSync(
		locationCookie,
		JSON.stringify(config.jar.toJSON(), null, 4)
	);
}

function loadCookie() {
	if (!fs.existsSync(path.dirname(locationCookie)))
		fs.mkdirSync(path.dirname(locationCookie), { recursive: true });
	if (fs.existsSync(locationCookie)) {
		var json = JSON.parse(fs.readFileSync(locationCookie));
		json.cookies.forEach((cookie) => {
			const rawCookie = `${cookie.key}=${cookie.value}; domain=${cookie.domain}; path=${cookie.path};`;
			//cookieJar.setCookieSync(rawCookie, `http://${cookie.domain}`);
			//cookieJar.setCookieSync(rawCookie, `https://${cookie.domain}`);
		});
	} else {
		cookieJar.setCookieSync(
			"key=value; domain=google.org",
			"https://google.org"
		);
		cookieJar.setCookieSync(
			"key=value; domain=facebook.com",
			"https://facebook.com"
		);
		cookieJar.setCookieSync(
			"key=value; domain=mockbin.org",
			"https://mockbin.org"
		);
		cookieJar.setCookieSync(
			"key=value; domain=backend.webmanajemen.com",
			"http://backend.webmanajemen.com"
		);
	}
}
loadCookie();

/*

axios.defaults.jar = cookieJar;
config.headers["Content-Type"] = "application/x-www-form-urlencoded";
axios
	.post(
		"http://backend.webmanajemen.com/artifact/servers/index.php",
		qs.stringify({ server: "https://jcenter.bintray.com/" }),
		config
	)
	.catch(saveCookie)
	.then(function (response) {
		console.log(response.data);
	});

	*/

const send = function (repo) {
	sendServer(
		"http://backend.webmanajemen.com/artifact/servers/index.php?server=" + repo,
		repo,
		false,
		{
			jar: cookieJar, // tough.CookieJar or boolean
			withCredentials: true, // If true, send cookie stored in jar
			data: qs.stringify({
				server: repo,
			}),
			headers: {
				"Content-Type": "application/x-www-form-urlencoded",
			},
			method: "POST",
		}
	);
};

module.exports = send;
