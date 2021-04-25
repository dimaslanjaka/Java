package com.dimaslanjaka.root;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

/**
 * https://stackoverflow.com/a/19920353
 * @usage unregisterComponentCallbacks(mMemoryBoss);
 * @usage registerComponentCallbacks(mMemoryBoss);
 */
/*
MemoryBoss mMemoryBoss;
@Override
public void onCreate() {
   super.onCreate();
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      mMemoryBoss = new MemoryBoss();
      registerComponentCallbacks(mMemoryBoss);
   }
}
 */
public class MemoryBoss implements ComponentCallbacks2 {
    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Core.Companion.setAppInBackground(true);
        }
        // you might as well implement some memory cleanup here and be a nice Android dev.
    }
}