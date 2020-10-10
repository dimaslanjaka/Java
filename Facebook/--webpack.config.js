const path = require("path");
module.exports = {
  context: path.resolve(__dirname),
  entry: {
    home: "./index.js",
    shared: ["react", "react-dom", "redux", "react-redux"],
  },
  module: {
    rules: [
      {
        test: /\.s[ac]ss$/i,
        use: [
          "style-loader",
          "css-loader",
          {
            loader: "sass-loader",
            options: {
              implementation: require("sass"),
              sassOptions: {
                fiber: false,
              },
            },
          },
        ],
      },
    ],
  },
};
