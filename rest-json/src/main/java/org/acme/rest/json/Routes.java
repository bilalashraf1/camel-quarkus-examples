package org.acme.rest.json;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Camel route definitions.
 */
public class Routes extends RouteBuilder {
    private final Set<CreditCard> ccList = Collections.synchronizedSet(new LinkedHashSet<>());
  
    public Routes() {

        this.ccList.add(new CreditCard("john", "xxxxxxxxxxxx" ));
        this.ccList.add(new CreditCard("smith", "xxxxxxxxxxxx" ));
        this.ccList.add(new CreditCard("mike", "xxxxxxxxxxxx" ));
    }

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/creditcard")
                .get()
                .route()
                .log("${body}")
                //.process() //validate thru bean/process
                //.marshal() //covert to third party acceptable instance
                // perform business logic
                //jdbc data source
                .setBody().constant(ccList) // set the body to be passed to third party
                .endRest()

                .post()
                .type(CreditCard.class)
                .route()
                //.process() //validate
                   //jdbc data source
                //.marshal() //covert to entity
                // perform business logic
                .process().body(CreditCard.class, ccList::add)
                .setBody().constant(ccList)
                //emial
                //sms
                .endRest();

        from("kamelet:chuck-norris-source")
        .log("${body}");
        // rest("/restendpoint")
        //         .get()
        //         .route()
        //         .setBody().constant(entity)
        //         .endRest();
    }
}
