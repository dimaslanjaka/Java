var Service = require("node-windows").Service;
var fs = require("fs");
var path = require("path");
var wincmd = require("node-windows");
var exec = require("child_process").exec;
const process = require("process");

/**
 * @type {import("node-windows").ServiceConfig} config
 */
const config = {
	name: "Maven Repository",
	description: "The nodejs maven repository web server.",
	script: path.join(__dirname, "index.js"),
};

// Create a new service object
var svc = new Service(config);

// Listen for the "install" event, which indicates the
// process is available as a service.
svc.on("install", function () {
	svc.start();
});

// Listen for the "uninstall" event so we know when it's done.
svc.on("uninstall", function () {
	console.log("Uninstall complete.");
	console.log("The service exists: ", svc.exists);
});

function install() {
	wincmd.isAdminUser(function (isAdmin) {
		if (isAdmin) {
			if (!svc.exists) svc.install();
		}
	});
}
function uninstall() {
	wincmd.isAdminUser(function (isAdmin) {
		if (isAdmin) {
			if (svc.exists) svc.uninstall();
		}
	});
}

var args = process.argv.slice(2);
if (args.length) {
	switch (args[0]) {
		case "start":
			if (svc.exists) {
				svc.restart();
			} else {
				console.log("service not exists");
			}
			break;
		case "install":
			install();
			break;
		case "uninstall":
			uninstall();
			break;
		default:
			wincmd.isAdminUser(function (isAdmin) {
				if (isAdmin) {
					wincmd.list(
						/**
						 * @param {Object} svclist
						 */
						function (svclist) {
							for (let index = 0; index < svclist.length; index++) {
								const currentService = svclist[index];
								if (currentService.ImageName == "mavenrepository.exe") {
									console.log(currentService);
								}
							}
						},
						true
					);
				} else {
					console.log("NOT AN ADMIN");
				}
			});
			break;
	}
}
