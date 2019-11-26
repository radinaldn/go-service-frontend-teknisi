package id.topapp.radinaldn.goserviceteknisi.config;

/**
 * Created by radinaldn on 22/12/18.
 */

public class ServerConfig {
    //    public static final String DOMAIN_SERVER = "http://192.168.43.217/";
//    public static final String DOMAIN_SERVER = "http://194.168.1.20/";
    public static final String DOMAIN_SERVER = "https://www.topapp.id/";
    public static final String SERVER_URL = DOMAIN_SERVER + "go-service-backend/api/v1/";
    public static final String API_ENDPOINT = SERVER_URL;
    public static final String GOOGLE_API_ENDPOINT = "https://maps.googleapis.com/maps/api/";
    public static final String GOOGLE_API_KEY = "AIzaSyD3Z9HSojIM2qZ15U5BHgJJJKmSUYstzEY";
    public static final String FILES_PATH = DOMAIN_SERVER + "go-service-backend/web/files/";
    public static final String FOTO_PEMESANAN_PATH = FILES_PATH + "foto-pemesanan/";
    public static final String MASYARAKAT_PROFIL_PATH = DOMAIN_SERVER + "go-service-backend/web/files/profil-masyarakat/";
    public static final String TEKNISI_PROFIL_PATH = DOMAIN_SERVER + "go-service-backend/web/files/profil-teknisi/";
    public static final String UPLOAD_FOTO_ENDPOINT = DOMAIN_SERVER + "go-service-backend/api/upload/upload-foto.php";
    public static final String UPLOAD_FOTO_TOPUP_ENDPOINT = DOMAIN_SERVER + "go-service-backend/api/upload/upload-foto-topup.php";
    public static final String BUKTI_TOPUP_PATH = FILES_PATH + "topup/";
}
