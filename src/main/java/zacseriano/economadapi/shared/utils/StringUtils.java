package zacseriano.economadapi.shared.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    public static String removerAcentos(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    
	// remove os espaços em branco e separa as palvaras por vírgula
	public static String[] formataString(String s) {
		String result = s.replaceAll("\\s+", "");
		String[] dadosFormatados = result.split(Pattern.quote(","));
		return dadosFormatados;
	}
}
