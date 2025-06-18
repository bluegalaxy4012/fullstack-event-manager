package mpp.rest.client;

import mpp.domain.Proba;
import mpp.services.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class ProbeClient {
    public static final String URL = "http://localhost:8080/api/probe";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) {
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Proba[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Proba[].class));
    }

    public Proba getById(Integer id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Proba.class));
    }

    public Proba create(Proba proba) {
        return execute(() -> restTemplate.postForObject(URL, proba, Proba.class));
    }

    public void update(Integer id, Proba proba) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, id), proba);
            return null;
        });
    }

    public void delete(Integer id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}