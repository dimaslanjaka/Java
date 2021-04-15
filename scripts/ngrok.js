const config = require("./config.json");
const port = config.port;
const path = require("path");
const ngrok = require("ngrok");
const fs = require("fs");
const auth = "1Szs4cJp7MoUlFPT3nyRjD5P05v_3BREWhqf8z2NdcNHMneUm";
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

serve();
