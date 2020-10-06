package com.dimaslanjaka.tools.Service.Netspeed.v2;

public interface ITrafficSpeedListener {

	void onTrafficSpeedMeasured(double upStream, double downStream);
}