package dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLtoJavaInator<T> {
    T convert(ResultSet result) throws SQLException;
}
