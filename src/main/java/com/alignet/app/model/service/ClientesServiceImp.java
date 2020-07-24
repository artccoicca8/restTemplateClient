package com.alignet.app.model.service;

import javax.net.ssl.SSLContext;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.alignet.app.model.AutenticationBean;
import com.alignet.app.model.MasterCard;
import com.alignet.app.model.VersioningObject;
import com.alignet.app.model.VersioningRequestBean;
import com.alignet.app.model.util.UtilsTresDs;
import com.google.gson.Gson;
import javax.net.ssl.SSLContext;
@Service
public class ClientesServiceImp implements ClientesService {

	private static Logger log = LoggerFactory.getLogger(ClientesServiceImp.class);

	
	private  Gson gson ;
	
	@Autowired
	private SSLContext  sslContext ; 
	@Override
	public String postDS() {
		log.info("postDS ");

 
		try {
		     
	        CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
	        
	        HttpComponentsClientHttpRequestFactory requestFactory  = new HttpComponentsClientHttpRequestFactory();
	        requestFactory.setHttpClient(client);
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
	        
	        String url = "https://3ds2.directory.mastercard.com/3ds/ds2/svc";
	        
	        
	        MasterCard request = new MasterCard("", "", "", "PReq", "2.1.0");
	        
//	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, HttpEntity.EMPTY, String.class);
	        
	        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
	        
	        log.info("Result = " + response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		     
		
		return "";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public String postVersioning(VersioningRequestBean bean) {
		log.info("postVersioning ");

		RestTemplate clienteRest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("key", bean.getKeyText());
		headers.set("ALG-API-VERSION", "1172945160");

		VersioningObject versioningObject = new VersioningObject(bean.getAcquirerMerchantID(), bean.getAcctNumber());

		HttpEntity<VersioningObject> request = new HttpEntity<>(versioningObject, headers);

		ResponseEntity<String> result = clienteRest.postForEntity(bean.getUrlEnviroment(), request, String.class);
		log.info("repuesta {} ", result.getBody());
		return result.getBody();
	}
	
	

	
	
	
 
	
	
	

	@Override
	public String postAuthentication(VersioningRequestBean bean, String body) {
		log.info("postVersioning ");
		
		gson = new Gson();
		RestTemplate clienteRest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("key", bean.getKeyText());
		headers.set("ALG-API-VERSION", "1172945160");
		headers.set("threeDSServerTransID", bean.getThreedsServerTransId());

		HttpEntity<String> request = new HttpEntity<>(body, headers);

		ResponseEntity<String> result = clienteRest.postForEntity(bean.getUrlEnviroment(), request, String.class);
		log.info("repuesta {} ", result.getBody());
		return result.getBody();
	}
	
	
	
	
	
	

}
