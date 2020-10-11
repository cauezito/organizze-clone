package br.com.cauezito.app.organizze.utils;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(data);
    }

    public static String formataDataMesAno(String data){
        String dataFormatada[] = data.split("/");
        return dataFormatada[1] + dataFormatada[2];
    }
}
