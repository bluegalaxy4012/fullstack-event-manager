package mpp.services.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import mpp.domain.Proba;
import mpp.repository.ProbaHibernateRepository;
//import mpp.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/probe")
public class OficiuProbaController {

    // pt jsonificare
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProbaWebSocketHandler webSocketHandler;

    private static final String template = "Hey, %s!";

    @Autowired
    private ProbaHibernateRepository probaRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "Friend") String name) {
        return String.format(template, name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Proba[] getAll() {
        System.out.println("Get all probe ...");

        return ((java.util.List<Proba>) probaRepository.findAll()).toArray(new Proba[0]);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get by id " + id);

        return probaRepository.findOne(id)
                .<ResponseEntity<?>>map(proba -> new ResponseEntity<>(proba, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Proba not found", HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Proba create(@RequestBody Proba proba) {
        probaRepository.save(proba);

        try { webSocketHandler.broadcast("Proba added: " + objectMapper.writeValueAsString(proba)); }
        catch (Exception e) { System.err.println("Error broadcast: " + e.getMessage()); }

        return proba;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Proba proba, @PathVariable Integer id) {
        System.out.println("Updating proba ...");


        if (proba.getId() == null) {
            return new ResponseEntity<>("Body must contain Id", HttpStatus.BAD_REQUEST);
        }
        if (!id.equals(proba.getId())) {
            return new ResponseEntity<>("Id in URL and body must be the same", HttpStatus.BAD_REQUEST);
        }


        var p = probaRepository.update(proba);

        if (p.isEmpty()) {
            return new ResponseEntity<>("Proba not found", HttpStatus.NOT_FOUND);
        }

        try { webSocketHandler.broadcast("Proba updated: " + objectMapper.writeValueAsString(p.get())); }
        catch (Exception e) { System.err.println("Error broadcast: " + e.getMessage()); }

        return new ResponseEntity<>(p.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting proba ... " + id);
            var p = probaRepository.delete(id);

            if (p.isEmpty())
                return new ResponseEntity<>("Proba not found", HttpStatus.NOT_FOUND);


        try { webSocketHandler.broadcast("Proba deleted: " + objectMapper.writeValueAsString(p.get())); }
        catch (Exception e) { System.err.println("Error broadcast: " + e.getMessage()); }

            return new ResponseEntity<Proba>(HttpStatus.OK);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String probaError(Exception e) {
        return e.getMessage();
    }


}
