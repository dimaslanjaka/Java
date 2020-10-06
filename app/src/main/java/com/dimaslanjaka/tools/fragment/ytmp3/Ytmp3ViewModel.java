package com.dimaslanjaka.tools.fragment.ytmp3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Ytmp3ViewModel extends ViewModel {

	private MutableLiveData<String> mText;

	public Ytmp3ViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("Youtube MP3 Downloader");
	}

	public LiveData<String> getText() {
		return mText;
	}
}