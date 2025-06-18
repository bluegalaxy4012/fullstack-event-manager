package mpp.start;

import mpp.domain.Proba;
import mpp.rest.client.NewProbeClient;
import mpp.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;

public class StartRestClientClient {
    private final static NewProbeClient probeClient = new NewProbeClient();

    public static void main(String[] args) {
        Proba proba = new Proba("200m", "spate");

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
        } catch (RestClientException ex) {
            System.out.println("Exception... " + ex.getMessage());
        }

        System.out.println("\nInfo for proba with id=1");
        show(() -> System.out.println(probeClient.getById(1)));

        System.out.println("\nDeleting proba with id=" + proba.getId());
        show(() -> probeClient.delete(proba.getId()));
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception: " + e);
        }
    }
}