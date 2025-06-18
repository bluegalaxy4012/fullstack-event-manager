package mpp;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import mpp.domain.Proba;

//tester rest api
public class Main {
    private static final String BASE_URL = "http://localhost:8080/api/probe";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Proba[]> response = restTemplate.getForEntity(BASE_URL, Proba[].class);
        System.out.println("All probe: " + java.util.Arrays.toString(response.getBody()));
        System.out.println("GET all status: " + response.getStatusCode() + "\n");




        Proba newProba = new Proba("1500m", "mixt");
        ResponseEntity<Proba> createResponse = restTemplate.postForEntity(BASE_URL, newProba, Proba.class);
        Proba created = createResponse.getBody();
        System.out.println("Created: " + created);
        System.out.println("POST status: " + createResponse.getStatusCode() + "\n");





        response = restTemplate.getForEntity(BASE_URL, Proba[].class);
        System.out.println("All probe: " + java.util.Arrays.toString(response.getBody()));
        System.out.println("GET all status: " + response.getStatusCode() + "\n");




        created.setDistanta("800m");
        created.setStil("spate");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Proba> entity = new HttpEntity<>(created, headers);

        ResponseEntity<Proba> updateResponse = restTemplate.exchange(
                BASE_URL + "/" + created.getId(), HttpMethod.PUT, entity, Proba.class);
        System.out.println("Updated: " + updateResponse.getBody());
        System.out.println("PUT status: " + updateResponse.getStatusCode() + "\n");






        response = restTemplate.getForEntity(BASE_URL, Proba[].class);
        System.out.println("All probe: " + java.util.Arrays.toString(response.getBody()));
        System.out.println("GET all status: " + response.getStatusCode() + "\n");





        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                BASE_URL + "/" + created.getId(), HttpMethod.DELETE, null, Void.class);
        System.out.println("Deleted proba with id: " + created.getId());
        System.out.println("DELETE status: " + deleteResponse.getStatusCode() + "\n");





        response = restTemplate.getForEntity(BASE_URL, Proba[].class);
        System.out.println("All probe: " + java.util.Arrays.toString(response.getBody()));
        System.out.println("GET all status: " + response.getStatusCode() + "\n");




        // invalid put/delete ( id 0 de ex )
        Proba invalidProba = new Proba(0, "0", "0");
        HttpEntity<Proba> invalidEntity = new HttpEntity<>(invalidProba, headers);

        try {
            ResponseEntity<Proba> invalidUpdateResponse = restTemplate.exchange(
                    BASE_URL + "/" + invalidProba.getId(), HttpMethod.PUT, invalidEntity, Proba.class);
            System.out.println("Invalid update: " + invalidUpdateResponse.getBody());
            System.out.println("Invalid PUT status: " + invalidUpdateResponse.getStatusCode() + "\n");
        }
        catch (org.springframework.web.client.HttpClientErrorException e) {
            System.out.println("Invalid update: " + e.getStatusCode() + " - " + e.getResponseBodyAsString() + "\n");
        }



        try
        {
            ResponseEntity<Void> invalidDeleteResponse = restTemplate.exchange(
                    BASE_URL + "/" + invalidProba.getId(), HttpMethod.DELETE, null, Void.class);
            System.out.println("Invalid delete proba with id: " + invalidProba.getId());
            System.out.println("Invalid DELETE status: " + invalidDeleteResponse.getStatusCode() + "\n");
        }
        catch (org.springframework.web.client.HttpClientErrorException e) {
            System.out.println("Invalid delete: " + e.getStatusCode() + " - " + e.getResponseBodyAsString() + "\n");
        }
    }
}