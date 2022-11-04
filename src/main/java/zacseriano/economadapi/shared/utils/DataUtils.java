package zacseriano.economadapi.shared.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUtils {
    public static final String DEFAULT_FORMAT = "dd/MM/yyyy";

    public static LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DataUtils.DEFAULT_FORMAT));
    }       
    
}
