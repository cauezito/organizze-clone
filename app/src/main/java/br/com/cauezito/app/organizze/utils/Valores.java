package br.com.cauezito.app.organizze.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Valores {
    public static String configuraValor(Double valor) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        return decimalFormat.format(valor);
    }
}
