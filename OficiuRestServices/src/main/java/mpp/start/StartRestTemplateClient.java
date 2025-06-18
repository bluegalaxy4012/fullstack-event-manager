package mpp.start;

import mpp.domain.Proba;
import mpp.rest.client.ProbeClient;
import mpp.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;

public class StartRestTemplateClient {
    private final static ProbeClient probeClient = new ProbeClient();

    public static void main(String[] args) {
        Proba proba = new Proba("100m", "liber");

        try {
            System.out.println("Adding a new proba " + proba);
            show(() -> System.out.println(probeClient.create(proba)));

            System.out.println("\nPrinting all probe...");
            show(() -> {
                Proba[] result = probeClient.getAll();
                for (Proba p : result) {
                    System.out.println(p.getId() + ": " + p.getDistanta() + " " + p.getStil());
                }
            });

            System.out.println("\nInfo for proba with id=1");
            show(() -> System.out.println(probeClient.getById(1)));

            System.out.println("\nUpdating proba...");
            show(() -> {
                Proba toUpdate = probeClient.getById(1);
                if (toUpdate != null) {
                    toUpdate.setStil("fluture");
                    probeClient.update(toUpdate.getId(), toUpdate);
                    System.out.println("Updated proba: " + probeClient.getById(1));
                }
            });

        } catch (RestClientException ex) {
            System.out.println("Exception... " + ex.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception: " + e);
        }
    }
}