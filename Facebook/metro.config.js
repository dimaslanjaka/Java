const { getDefaultConfig } = require("metro-config");
const defaultConfig = getDefaultConfig(__dirname);

module.exports = (async () => {
  const {
    resolver: { sourceExts },
  } = await getDefaultConfig();
  return {
    transformer: {
      assetPlugins: ["expo-asset/tools/hashAssetFiles"],
      babelTransformerPath: require.resolve("react-native-sass-transformer"),
    },
    resolver: {
      sourceExts: [...sourceExts, "scss", "sass", "db", "jsx"],
    },
  };
})();
