package com.mycompany.myapp.data;

import android.content.Context;

import com.mycompany.myapp.app.ForApplicationScope;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import retrofit.client.Client;
import retrofit.client.OkClient;
import timber.log.Timber.Tree;

@Module
public class DataModule {
    static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    @Provides
    OkHttpClient provideOkHttpClient(@ForApplicationScope Context app, @ForApplicationScope Tree logger) {
        OkHttpClient client = new OkHttpClient();

        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(app.getCacheDir(), "http");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            logger.e(e, "Unable to install disk cache.");
        }

        return client;
    }


    @Provides
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }
}
