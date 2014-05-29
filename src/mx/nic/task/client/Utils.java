package mx.nic.task;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;

/**
 * Clase con métodos de utilería.
 * 
 * @author mgonzalez
 *
 */
public final class Utils {
	private static TLSClientParameters tlsParams;

	static {
		try {
			initializeConduitForSSL();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final TLSClientParameters getTlsParams() {
		return tlsParams;
	}

	private static final void initializeConduitForSSL() throws Exception {
		try {
			tlsParams = new TLSClientParameters();

			// set up the keystore and password
			KeyStore keyStore = KeyStore.getInstance("JKS");
			// -- provide your password
			String trustpass = "barbacoas";
			// -- provide your truststore
			File truststore = new File(
					"C:\\\\tomcat\\apache-tomcat-7.0.53\\SSL\\Mykeystore");
			keyStore.load(new FileInputStream(truststore),
					trustpass.toCharArray());

			// should JSEE omit checking if the host name specified in the
			// URL matches that of the Common Name (CN) on the server's
			// certificate.
			tlsParams.setDisableCNCheck(true);

			// set the SSL protocol
			tlsParams.setSecureSocketProtocol("TLS");

			// set the trust store
			// (decides whether credentials presented by a peer should be
			// accepted)
			TrustManagerFactory trustFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(keyStore);
			TrustManager[] tm = trustFactory.getTrustManagers();
			tlsParams.setTrustManagers(tm);

			// set our key store
			// (used to authenticate the local SSLSocket to its peer)
			KeyManagerFactory keyFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyFactory.init(keyStore, trustpass.toCharArray());
			KeyManager[] km = keyFactory.getKeyManagers();
			tlsParams.setKeyManagers(km);

			// set all the needed include & exclude cipher filters
			FiltersType filter = new FiltersType();
			filter.getInclude().add(".*_WITH_3DES_.*");
			filter.getInclude().add(".*_EXPORT_.*");
			filter.getInclude().add(".*_EXPORT1024_.*");
			filter.getInclude().add(".*_WITH_DES_.*");
			filter.getInclude().add(".*_WITH_NULL_.*");
			filter.getExclude().add(".*_DH_anon_.*");
			tlsParams.setCipherSuitesFilter(filter);
		} catch (final Exception e) {
			throw new Exception("Failed to initialize conduit!", e);
		}
	}
}
