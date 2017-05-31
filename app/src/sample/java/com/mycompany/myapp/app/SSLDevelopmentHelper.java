package com.mycompany.myapp.app;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLDevelopmentHelper {

    public static HostnameVerifier buildTrustAllHostnameVerifer() {
        return new HostnameVerifier() {
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        };
    }

    public static SSLContext buildTrustAllSSLContext() {
        // Create a trust manager that does not validate certificate chains to
        // avoid "Trust anchor for certification path not found" exception
        TrustManager[] trustAllCerts = new TrustManager[]{buildTrustAllTrustManager()};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");  //NON-NLS
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static X509TrustManager buildTrustAllTrustManager() {
        return new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
    }
}
