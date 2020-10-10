import YoutubeMp3Downloader from "youtube-mp3-downloader";
import path from "path";
var YD = new YoutubeMp3Downloader({
  //ffmpegPath: "/path/to/ffmpeg", // FFmpeg binary location
  outputPath: "./download", // Output file location (default: the home directory)
  youtubeVideoQuality: "highestaudio", // Desired video quality (default: highestaudio)
  queueParallelism: 2, // Download parallelism (default: 1)
  progressTimeout: 2000, // Interval in ms for the progress reports (default: 1000)
  allowWebm: false, // Enable download from WebM sources (default: false)
});
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
