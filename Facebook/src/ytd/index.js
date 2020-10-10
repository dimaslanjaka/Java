import React, { Component } from "react";
import { AppRegistry } from "react-native";
import { Scene, Router } from "react-native-router-flux";
import { createStore, applyMiddleware } from "redux";
import { connect, Provider } from "react-redux";
import reducer from "./reducers";
import App from "./containers/App";
//import {AppRegistry} from 'react-native';
//import App from './App'; // main app file of Expo
import { Surface, Shape } from "@react-native-community/art";
import { name as appName } from "./../app.json";

const RouterWithRedux = connect()(Router);
const store = createStore(reducer);

const YoutubePlayer = () => <App />;
//AppRegistry.registerComponent(appName, () => App);
//AppRegistry.registerComponent("YoutubePlayer", () => YoutubePlayer);
AppRegistry.registerComponent(appName, () => YoutubePlayer);
AppRegistry.runApplication(appName, {
  rootTag: document.getElementById("root"),
});
