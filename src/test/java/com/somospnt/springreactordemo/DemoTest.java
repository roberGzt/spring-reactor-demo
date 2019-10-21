package com.somospnt.springreactordemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class DemoTest {

    @Test
    public void ejemploZip() {
        System.out.println("Ejemplo Zip");
        Flux<String> flux1 = Flux.range(1, 5)
                .map(numero -> "F1." + numero);
        Flux<String> flux2 = Flux.range(1, 5)
                .map(numero -> "F2." + numero);
        flux1.subscribe(System.out::println);
        flux2.subscribe(System.out::println);

        System.out.println("ZIPPED:");
        flux1.zipWith(flux2)
                .map(tupla -> tupla.getT1() + "--" + tupla.getT2())
                .subscribe(System.out::println);

    }

    @Test
    public void ejemploZip2() {
        System.out.println("Ejemplo Zip 2");
        Flux<String> flux1 = Flux.range(1, 5)
                .map(numero -> "F1." + numero);
        Flux<String> flux2 = Flux.range(1, 3)
                .map(numero -> "F2." + numero);
        Flux<String> flux3 = Flux.range(1, 10)
                .map(numero -> "F3." + numero);

        System.out.println("ZIPPED:");
        Flux<String> zipped = Flux.zip(flux1, flux2, flux3)
                .map(tupla3 -> tupla3.getT1() + "-" + tupla3.getT2() + "-" + tupla3.getT3());

        zipped.subscribe(System.out::println);

    }
    
    @Test
    public void ejemploEjecucionParalela() {
        System.out.println("Ejemplo Merge");
        Flux<String> flux1 = Flux.range(1, 2)                
                .map(numero -> "F1." + numero);
        Flux<String> flux2 = Flux.range(1, 3)                
                .map(numero -> "F2." + numero);        
        
        System.out.println("MERGED");
        Flux.merge(flux1,flux2)
                .map(string -> loguearCurrentThread(string))
                .subscribe(System.out::println);
        
        System.out.println("MERGED y PARALELIZADO");
        Flux.merge(flux1,flux2)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(string -> loguearCurrentThread(string))
                .subscribe(System.out::println);  
    }
    
    @Test
    public void ejemploEjecucionParalelaYLuegoSecuencial() {
        System.out.println("Ejemplo Ejecución paralela");
        Flux<String> flux1 = Flux.range(1, 2)                
                .map(numero -> "F1." + numero);
        Flux<String> flux2 = Flux.range(1, 3)                
                .map(numero -> "F2." + numero);        
        Flux<String> flux3 = Flux.range(1, 100)                
                .map(numero -> "SECUENCIAL." + numero);         
        
        System.out.println("PROCESAMIENTO PARALELO Y LUEGO SECUENCIAL");
        Flux.merge(flux1,flux2)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(string -> loguearCurrentThread(string))                
                .sequential()
                .zipWith(flux3)
                .map(tupla -> loguearCurrentThread(tupla.getT1() + " - " + tupla.getT2()))
                .subscribe(System.out::println);  
        
        
    }

    private static String loguearCurrentThread(String string) {
        return Thread.currentThread().getName() + " -> " + string;
    }

}
