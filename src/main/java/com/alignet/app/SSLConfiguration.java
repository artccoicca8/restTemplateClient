package com.alignet.app;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SSLConfiguration {

	// @Value("${ose.ssl.keystore}")
	public String fileLocation = "classpath:3DSC-PRD-SVR-V210-ALIGNET_S.A.C-58990.jks";

//	@Value("${ose.ssl.keystore-password}")
	public String keyStorePass = "3DSC-PRD-SVR-V210-ALIGNET_S.A.C-58990";

	private static final String CLASSPATH_LOC = "classpath:";

	@Bean
	@Scope("singleton")
	public KeyStore getKS() {
		InputStream is = null;
		KeyStore ks;
		try {
			if (fileLocation.startsWith(CLASSPATH_LOC)) {
				is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(fileLocation.substring(CLASSPATH_LOC.length()));
			}

			ks = KeyStore.getInstance("JKS");
			ks.load(is, keyStorePass.toCharArray());
			is.close();
			return ks;
		} catch (Exception e) {
			throw new RuntimeException("Error al abrir el keystore");
		}

	}

	@Bean
	public SSLContext createSSLContext(TrustManager trustManager, KeyManager keyManager) {

		try {
			SSLContext c = SSLContext.getInstance("TLSv1.2");
			c.init(new KeyManager[] { keyManager }, new TrustManager[] { trustManager }, null);
			return c;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	@Scope("singleton")
	public KeyManagerFactory getKeyManager(@Autowired KeyStore ks) {
		KeyManagerFactory kmf = null;
		try {
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, keyStorePass.toCharArray());

		} catch (Exception e) {
			throw new RuntimeException("Error al crear TrustManagerFactory");
		}
		return kmf;
	}

	@Bean
	@Scope("singleton")
	public TrustManagerFactory getTrustManager(@Autowired KeyStore ks) {
		TrustManagerFactory tmf = null;
		try {
			tmf = TrustManagerFactory.getInstance("X509");
			tmf.init(ks);

		} catch (Exception e) {
			throw new RuntimeException("Error al crear TrustManagerFactory");
		}
		return tmf;
	}

	@Bean
	public TrustManager createTrustManager(TrustManagerFactory tmf) {
		return tmf.getTrustManagers()[0];
	}
	
	@Bean
	public KeyManager createKeyManager(KeyManagerFactory kmf) {
		return kmf.getKeyManagers()[0]; 
	}

}
