package id.topapp.radinaldn.goserviceteknisi.rest;

/**
 * Created by radinaldn on 22/12/18.
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyGoogleApiInterface {
    // untuk mendapatkan response dari Google Reverse Geocoding
    @GET("geocode/json")
    Call<ResponseReverseGeocoding> geocodeJson(
            @Query("latlng") String latlng,
            @Query("key") String key
    );
}
