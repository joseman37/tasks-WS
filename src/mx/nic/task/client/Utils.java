package mx.nic.task.client;

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

			KeyStore keyStore = KeyStore.getInstance("JKS");
			// password
			String trustpass = "barbacoas";
			// truststore
			File truststore = new File("C:\\\\tomcat\\apache-tomcat-7.0.53\\SSL\\Mykeystore");
			keyStore.load(new FileInputStream(truststore), trustpass.toCharArray());

			// settear en true sólo en producción
			tlsParams.setDisableCNCheck(true);

			// protocolo SSL
			tlsParams.setSecureSocketProtocol("TLS");

			// trust store
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(keyStore);
			TrustManager[] tm = trustFactory.getTrustManagers();
			tlsParams.setTrustManagers(tm);

			// key store
			// (usado para autenticar el SSLSocket)
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyFactory.init(keyStore, trustpass.toCharArray());
			KeyManager[] km = keyFactory.getKeyManagers();
			tlsParams.setKeyManagers(km);

			// establecer todos los filtros de cifrado
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
