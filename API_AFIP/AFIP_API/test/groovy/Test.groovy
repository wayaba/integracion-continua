import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient;
import groovyx.net.http.ContentType;
import spock.lang.Specification;


class PruebasAFIP extends Specification{
	//creo una clase del objeto RESTClient, apuntando a la URL y path base de la API
	RESTClient restClient = new RESTClient("http://172.17.0.2:7800/afip/v1.0/");
	
	//este atributo se usará para almacenar todas las respuestas http de los tests
	HttpResponseDecorator httpResponse;
	
	//este atributo se usará para almacenar los códigos de estado de los tests
	Integer httpStatus; 
	
	// setup se ejecuta antes de cada método de prueba
	public setup() {
		
	}
	
	public "consulta deudas de cliente previsional con 301 sin deudas" () {
		//defino el path de la operación a consultar
		given: 
			//seteo los headers necesarios para el servicio
			restClient.headers["X-Usuario"] = "Nico";
			restClient.headers["X-Canal"] = "HBI";
			restClient.headers["X-Id-Sesion"] = "123";
		
			String pathOperacion = "deudores/previsionales/20153192430";
		when:
			try {
				//se trata de realizar una petición HTTP a la API y se espera un status code = 2xx
				httpResponse = restClient.get(path: pathOperacion, contentType: ContentType.JSON);
				httpStatus = httpResponse.status;
			}
			//si se obtiene un código de estado = 4xx o 5xx se arroja una HttpResponseException
			// capturo a la misma y obtengo el ódigo recibido
			catch(HttpResponseException e) {
				
				httpStatus = e.statusCode;
			}
		//mediante asersión compruebo el códio obtenido, si no coincide gradle lo alertará	
		then: 
			assert httpStatus == 200;
			assert httpResponse.responseData.tiene_deuda == "true";
	}
	
	public "consulta deudas de cliente previsional sin 301 con deudas" () {
		
		given:
			restClient.headers["X-Usuario"] = "Nico";
			restClient.headers["X-Canal"] = "HBI";
			restClient.headers["X-Id-Sesion"] = "123";
			
			String pathOperacion = "deudores/previsionales/30571722131";
		when:
			try {
				
				httpResponse = restClient.get(path: pathOperacion, contentType: ContentType.JSON);
				httpStatus = httpResponse.status;
			}
			
			catch(HttpResponseException e) {
				
				httpStatus = e.statusCode;
			}
		
		then:
			assert httpStatus == 200;
			assert httpResponse.responseData.tiene_deuda == "true";
	}
	
	public "consulta deudas de cliente previsional con 301 con deudas" () {
		
		given:
			restClient.headers["X-Usuario"] = "Nico";
			restClient.headers["X-Canal"] = "HBI";
			restClient.headers["X-Id-Sesion"] = "123";
		
			String pathOperacion = "deudores/previsionales/20249130355";
		when:
			try {
				
				httpResponse = restClient.get(path: pathOperacion, contentType: ContentType.JSON);
				httpStatus = httpResponse.status;
			}
			catch(HttpResponseException e) {
				
				httpStatus = e.statusCode;
			}
		
		then:
			assert httpStatus == 200;
			assert httpResponse.responseData.tiene_deuda == "true";
	}
	
	public "consulta deudas de cliente previsional inexistente" () {
		
		given:
		
			restClient.headers["X-Usuario"] = "Nico";
			restClient.headers["X-Canal"] = "HBI";
			restClient.headers["X-Id-Sesion"] = "123";
		
			String pathOperacion = "deudores/previsionales/23012720424";
			
			//variables para asersiones
			String statusMessage;
			String afipCode;
		when:
			try {
				
				httpResponse = restClient.get(path: pathOperacion, contentType: ContentType.JSON);
				httpStatus = httpResponse.status;
			}
			
			catch(HttpResponseException e) {
				
				httpStatus = e.statusCode;
				
				statusMessage = e.response.data.error.status;
				afipCode = e.response.data.error.code;
			}
		then:
			
			assert httpStatus == 500;
			assert statusMessage == "500";
			assert afipCode == "AF04";
	}
		
	public def "consulta deudas de cliente previsional sin 301 sin deudas"() {
		
		given: 
			restClient.headers["X-Usuario"] = "Nico";
			restClient.headers["X-Canal"] = "HBI";
			restClient.headers["X-Id-Sesion"] = "123";
		
			String pathOperacion = "deudores/previsionales/23012720424";
		when:
			try {
				httpResponse = restClient.get(path: pathOperacion, contentType: ContentType.JSON);
				httpStatus = httpResponse.status;
			}
			catch(HttpResponseException e) {
				httpStatus = e.statusCode;
			}
			
		then: 
			assert httpStatus == 500;
			//httpResponse.tiene_deuda == "false";
	}

}