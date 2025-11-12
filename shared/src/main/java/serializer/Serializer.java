package serializer;

import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;

public class Serializer {
    private static final Gson SERIALIZER = new GsonBuilder()
            .registerTypeAdapter(
                    new TypeToken<HashMap<ChessPosition, ChessPiece>>(){}.getType(),
                    new TypeAdapter<HashMap<ChessPosition, ChessPiece>>() {
                        @Override
                        public void write(JsonWriter out, HashMap<ChessPosition, ChessPiece> map) throws IOException {
                            out.beginObject();
                            for (var entry : map.entrySet()) {
                                out.name(entry.getKey().toString());
                                SERIALIZER.toJson(entry.getValue(), ChessPiece.class, out);
                            }
                            out.endObject();
                        }

                        @Override
                        public HashMap<ChessPosition, ChessPiece> read(JsonReader in) throws IOException {
                            HashMap<ChessPosition, ChessPiece> map = new HashMap<>();
                            in.beginObject();
                            while (in.hasNext()) {
                                String key = in.nextName();
                                ChessPiece value = SERIALIZER.fromJson(in, ChessPiece.class);
                                map.put(ChessPosition.fromString(key), value);
                            }
                            in.endObject();
                            return map;
                        }
                    }
            )
            .serializeNulls()
            .create();
    public static Gson serializer() {
        return SERIALIZER;
    }
}
