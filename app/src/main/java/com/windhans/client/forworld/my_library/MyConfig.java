package com.windhans.client.forworld.my_library;

/*import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;*/

import androidx.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

//import okhttp3.Dispatcher;
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;

/**
 * Created by PC-2 on 07-Apr-17.
 */
public class
MyConfig {

    //public static final String JSON_BASE_URL = "http://godapark.com/";
    //static final String SUB_MASTER = "/delivery_management/";

    //http://rishiherbocare.com/siddhivinayaktraders
    /*public static final String JSON_BASE_URL = "http://godapark.com";
    public static final String JSON_BASE_URL_UPLOADS = "http://godapark.com/rfiddemo/assets/uploads/office/";
    public static final String JSON_BASE_URL_UPLOADS_Announcment = "http://godapark.com/rfiddemo/assets/uploads/announcements/";
    public static final String JSON_BASE_URL_UPLOADS_BOE = "http://godapark.com/rfiddemo/assets/uploads/boeactivities/";
    public static final String JSON_BASE_URL_UPLOADS_AWARD = "http://godapark.com/testevent/assets/uploads/awards/";
    public static final String JSON_BASE_URL_UPLOADS_PDF_BROUCHER = "http://godapark.com/testevent/assets/uploads/brochure/";
    public static final String USIWIZ = "/rfiddemo";*/

   /* public static final String JSON_BASE_URL = "http://godapark.com";
    public static final String JSON_BASE_URL_UPLOADS = "http://usiwz.in/assets/uploads/office/";
    public static final String SSWORLD = "/ssforworld";
    public static final String JSON_BusinessImage = "http://godapark.com/ssforworld/assets/uploads/product/";
    public static final String JSON_BusinessPath = "http://godapark.com/ssforworld/assets/uploads/banner/";
    public static final String JSON_CategoryPath = "http://godapark.com/ssforworld/assets/uploads/category/";*/

   /* public static final String JSON_BASE_URL = "http://wetap.in";
    public static final String SSWORLD = "/2022/forworld";*/
    public static final String JSON_BASE_URL = "https://forworldservices.com/";
    public static final String SSWORLD = "foradmin/";
    public static final String JSON_ProfileImage = "https://forworldservices.com/foradmin/assets/uploads/";
    public static final String JSON_BusinessImage = "https://forworldservices.com/foradmin/assets/uploads/product/";
    public static final String JSON_BusinessOffers = "https://forworldservices.com/foradmin/assets/uploads/offers/";
    public static final String JSON_BusinessPath = "https://forworldservices.com/foradmin/assets/uploads/banner/";
    public static final String JSON_ServicePath = "https://forworldservices.com/foradmin/assets/uploads/service/";
    public static final String JSON_CategoryPath = "https://forworldservices.com/foradmin/assets/uploads/category/";


    public static Dispatcher dispatcher;
    public static Retrofit retrofit;

    public static Retrofit getRetrofit(String BASE_URL) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(50, TimeUnit.SECONDS);
        httpClient.readTimeout(50, TimeUnit.SECONDS);
        httpClient.writeTimeout(50, TimeUnit.SECONDS);
        httpClient.cache(null);
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);
        //.addConverterFactory(GsonConverterFactory.create());
        dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        httpClient.dispatcher(dispatcher);

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit;
    }

    public static MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        File file = new File(fileUri);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse("text/plain"), s);
    }

    /*public static void cancelRetrofitAllRequests()
    {

        Log.d("my_tag", "cancelRetrofitAllRequests1: "+dispatcher.queuedCallsCount());
        Log.d("my_tag", "cancelRetrofitAllRequests2: "+dispatcher.runningCallsCount());
        for(Call call : dispatcher.queuedCalls()) {
            //if(call.request().tag().equals("sss"))

            if (dispatcher.queuedCallsCount()>0)
                call.cancel();
        }
        for(Call call : dispatcher.runningCalls()) {
            //if(call.request().tag().equals("sss"))
            if (dispatcher.runningCallsCount()>0)
                call.cancel();
        }

        if (dispatcher!=null)
        {
            Log.d("my_tag", "cancelRetrofitAllRequests: ");
            dispatcher.cancelAll();
        }
    }*/
}
