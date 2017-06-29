package xyz.rnovoselov.photon.di.modules;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import xyz.rnovoselov.photon.PhotonConfiguration;
import xyz.rnovoselov.photon.data.RestService;

/**
 * Created by roman on 27.11.16.
 */

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return createClient();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return createRetrofit(okHttpClient);
    }

    @Provides
    @Singleton
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

    private Retrofit createRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(PhotonConfiguration.BASE_URL)
                .addConverterFactory(createConvertFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private Converter.Factory createConvertFactory() {
        return MoshiConverterFactory.create(new Moshi.Builder()
//                .add(new PhotocardJsonAdapter())
                .build());
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(PhotonConfiguration.MAX_OKHTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(PhotonConfiguration.MAX_OKHTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(PhotonConfiguration.MAX_OKHTTP_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }
}

