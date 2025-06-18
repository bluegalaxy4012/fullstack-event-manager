package mpp;

import mpp.gui.LoginWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static Properties props = new Properties();

    static {
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find db.config " + e);
        }
    }

    public static void main(String[] args) throws IOException {




//        Utilizator utilizator = new Utilizator();
//        utilizator.setNumeUtilizator("admin");
//        utilizator.setParola("admin");
//        utilizatorRepo.save(utilizator);
//
//        Participant participant = new Participant();
//        participant.setNume("Matei");
//        participant.setVarsta(25);
//        participantRepo.save(participant);
//        participant.setId(1);
//
//        Proba proba = new Proba();
//        proba.setDistanta("50m");
//        proba.setStil("liber");
//        probaRepo.save(proba);
//        proba.setId(1);
//
//        Inscriere inscriere = new Inscriere();
//        inscriere.setParticipant(participant);
//        inscriere.setProba(proba);
//        inscriereRepo.save(inscriere);
//
//        Optional<Participant> optionalParticipant = participantRepo.findOne(participant.getId());
//        if (optionalParticipant.isPresent()) {
//            Participant existingParticipant = optionalParticipant.get();
//            existingParticipant.setNume("Ioana");
//            participantRepo.update(existingParticipant);
//        }
//
//        Optional<Proba> optionalProba = probaRepo.findOne(proba.getId());
//        if (optionalProba.isPresent()) {
//            Proba existingProba = optionalProba.get();
//            existingProba.setDistanta("200m");
//            probaRepo.update(existingProba);
//        }
//

        LoginWindow.main(args);

    }
}