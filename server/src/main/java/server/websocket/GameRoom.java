package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;

public class GameRoom {
    private final ArrayList<Session> participants;

    public GameRoom() {
        participants = new ArrayList<>();
    }

    private void tellPerson(ServerMessage message, Session person) throws IOException {
        if(person != null && person.isOpen()) {
            person.getRemote().sendString(new Gson().toJson(message));
        }
    }

    public void addParticipant(Session participant) {
        participants.add(participant);
    }

    public void removeParticipant(Session participant) {
        participants.remove(participant);
    }

    public void tellEveryone(ServerMessage message, Session exclude) throws IOException {
        for(Session person : participants) {
            if(!person.equals(exclude)) {
                tellPerson(message, person);
            }
        }
    }

}
