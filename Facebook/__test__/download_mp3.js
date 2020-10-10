var YoutubeMp3Downloader = require("youtube-mp3-downloader");
var YD = new YoutubeMp3Downloader();
YD.download("Wz3k9E7eCLg");

YD.on("finished", function (err, data) {
  console.log(JSON.stringify(data));
});

YD.on("error", function (error) {
  console.log(error);
});

YD.on("progress", function (progress) {
  console.log(JSON.stringify(progress));
});
