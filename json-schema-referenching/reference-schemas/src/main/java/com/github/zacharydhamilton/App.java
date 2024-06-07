package com.github.zacharydhamilton;

import java.io.IOException;

import com.github.zacharydhamilton.objects.Jelly;
import com.github.zacharydhamilton.objects.Pbj;
import com.github.zacharydhamilton.objects.PeanutButter;
import com.github.zacharydhamilton.producer.PBJProducer;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args) throws IOException {
        PeanutButter pb = new PeanutButter();
		pb.setSmooth(true);
		pb.setOrganic(true);
		Jelly j = new Jelly();
		j.setFlavor("grape");
		j.setOrganic(true);
		Pbj pbj = new Pbj();
		pbj.setBread("wheat");
		pbj.setPeanutbutter(pb);
		pbj.setJelly(j);

        PBJProducer producer = new PBJProducer();
        producer.produce(pbj);
        producer.flush();
    }
}
