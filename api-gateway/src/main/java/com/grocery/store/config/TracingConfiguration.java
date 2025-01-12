//package com.grocery.store.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import io.micrometer.tracing.Tracer;
//import io.micrometer.tracing.brave.bridge.BraveTracer;
//
//@Configuration
//public class TracingConfiguration {
//
//    @Bean
//    public Tracer tracer(brave.Tracing tracing) {
//        return BraveTracer.create(tracing);
//    }
//}
//
