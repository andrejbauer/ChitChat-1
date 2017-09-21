package si.trstenjak.chitchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class Povezava {
	
	public static List<String> seznam_uporabnikov(List<Uporabnik> uporabniki){
		List<String> imena = new ArrayList<String>();
		for (Uporabnik x : uporabniki){
			imena.add(x.getUsername());
		}
		return imena;
	}
	
	public static ArrayList<Uporabnik> prisotni() throws ClientProtocolException, IOException{
		String responseBody = Request.Get("http://chitchat.andrej.com/users").execute().returnContent().asString();
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>(){
		};
		ArrayList<Uporabnik> prisotni = mapper.readValue(responseBody,  t);
		return prisotni;	
	}
	
	
	public static void prijavi(String ime) {
		String time = Long.toString(new Date().getTime());
		URI uri;
		try {
			uri = new URIBuilder("http://chitchat.andrej.com/users").
					addParameter("username", ime).addParameter("stop-cache", time).
					build();
			String responseBody = Request.Post(uri).execute().returnContent().asString();
			System.out.println(responseBody);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public static void odjavi(String ime) {
		String time = Long.toString(new Date().getTime());
		URI uri;
		try {
			uri = new URIBuilder("http://chitchat.andrej.com/users").
					addParameter("username", ime).addParameter("stop-cache", time).
					build();
			String responseBody = Request.Delete(uri).execute().returnContent().asString();
			System.out.println(responseBody);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	
	public static List<Sporocilo> prejmi(String ime) throws URISyntaxException, ClientProtocolException, IOException{
		String time = Long.toString(new Date().getTime());
		
		ObjectMapper mapper = new ObjectMapper(); //ObjectMapper: pretvarja JSON stringe v Java objekte in obratno
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
				.addParameter("username", ime).addParameter("stop-cache", time)
				.build();
		
		String responseBody = Request.Get(uri).execute().returnContent().asString();
		TypeReference<List<Sporocilo>> t = new TypeReference<List<Sporocilo>>() {
		};
		
		List<Sporocilo> prejeto = mapper.readValue(responseBody, t);
		return prejeto;		
	}
	
	
	
	public static void poslji(Boolean javno, String prejemnik, String posiljatelj, String besedilo) {
		String time = Long.toString(new Date().getTime());
		ObjectMapper mapper = new ObjectMapper();
		URI uri;
		String responseBody = null;
		try {
			uri = new URIBuilder("http://chitchat.andrej.com/messages")
					.addParameter("username", posiljatelj).addParameter("stop-cache", time)
					.build();
			Sporocilo sporocilo = new Sporocilo (javno, prejemnik, besedilo);
			String jsonSporocilo = mapper.writeValueAsString(sporocilo);
			responseBody = Request.Post(uri).bodyString(jsonSporocilo, ContentType.APPLICATION_JSON)
					.execute().returnContent().asString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(responseBody);
	}
}
